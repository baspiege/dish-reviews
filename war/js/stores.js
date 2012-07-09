///////////////////
// Global vars
///////////////////

var waitingForCoordinatesMessage="Waiting for coordinates...";
var locationNotAvailableMessage="Location Not Available";
var locationNotFoundMessage="Location Not Found";
var canEdit=false;
var isLoggedIn=false;
var geocoder;
var xmlHttpRequest=new XMLHttpRequest();

///////////////////
// Cookies
///////////////////

function getCookie(name) {
  if (document.cookie.length>0) {
    var start=document.cookie.indexOf(name+"=");
    if (start!=-1) {
      start+=name.length+1;
      var end=document.cookie.indexOf(";",start);
      if (end==-1) {
        end=document.cookie.length;
      }
      return unescape(document.cookie.substring(start,end));
    }
  }
  return "";
}

///////////////////
// Asynch
///////////////////

function sendRequest(url,callback,errorCallback,postData) {
  var req = xmlHttpRequest;
  if (!req) return;
  var method = (postData) ? "POST" : "GET";
  req.open(method,url,true);
  req.setRequestHeader('User-Agent','XMLHTTP/1.0');
  if (postData)
    req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
  req.onreadystatechange = function () {
    if (req.readyState != 4) return;
    if (req.status != 200 && req.status != 304) {
      // alert('HTTP error ' + req.status);
      errorCallback();
      return;
    }
    if (callback){
      callback(req);
    }
  }
  if (req.readyState == 4) return;
  req.send(postData);
}

///////////////////
// Data
///////////////////

function getCachedData() {
    var xmlDoc=null;
    var cachedResponse=localStorage.getItem(getStoresKey());
    if (cachedResponse) {
      var parser=new DOMParser();
      xmlDoc=parser.parseFromString(cachedResponse,"text/xml");
    }
    return xmlDoc;
}

function getStoresData() {
  // Get position and send request
  var lat=localStorage.getItem("latitude");
  var lon=localStorage.getItem("longitude");
  if (lat==null || lon==null) {
    return;
  }
  
  var progressData=document.getElementById("progressData");
  if (progressData) {
    progressData.style.display="inline";
  }
  
  // If online, get from server.  Else get from cache.
  if (navigator.onLine) {
    displayCachedDataIfExists();
    sendRequest('/storesXml?latitude='+lat+'&longitude='+lon, handleStoresDataRequest, displayCachedData);
  } else {
    displayCachedData();
  }
}

function getStoresKey() {
  var latitude=get2Decimals(parseFloat(localStorage.getItem("latitude")));
  var longitude=get2Decimals(parseFloat(localStorage.getItem("longitude")));
  return "STORES_"+latitude+"_"+longitude;
}

function handleStoresDataRequest(req) {
  var display=true;
  var cachedResponse=localStorage.getItem(getStoresKey());
  
  // Save in local storage in case app goes offline
  // TODO - Get lat/lon from result.  Might change between request and response.
  setItemIntoLocalStorage(getStoresKey(), req.responseText);
  
  if (cachedResponse!=null) {
    var display=false;
    if (cachedResponse!=req.responseText) {
      var response=confirm("Do you want to display new data?");
      if (response) {
        display=true;
      }
    }
  }

  // Process response
  if (display) {
    var xmlDoc=req.responseXML;
    displayData(xmlDoc);
  }
}

///////////////////
// Data Display
///////////////////

function displayCachedDataIfExists() {
  var xmlDoc=getCachedData();
  if (xmlDoc) {
    displayData(xmlDoc);
  }
}

function displayCachedData() {
  var xmlDoc=getCachedData();
  if (xmlDoc) {
    displayData(xmlDoc);
  } else {
    displayTableNoCachedData();
  }
}

function displayData(xmlDoc) {  
  
  // Create HTML table
  var tableDiv=document.getElementById("data");
  var table=createTable();

  var stores=xmlDoc.getElementsByTagName("store"); 
  
  // No stores
  if (stores.length==0){
    table.appendChild(createTableRowForNoData());
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
  }
  // Stores
  else {
    // Make HTML for each store
    for (var i=0;i<stores.length;i++) {
      var store=stores[i];
      table.appendChild(createTableRowForStore(store));
    }
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
    updateLocationDispay();
  }
}

function createTable() {
  var table=document.createElement("table");
  table.setAttribute("id","stores");
  var tr=document.createElement("tr");
  table.appendChild(tr);

  // Distance
  var thDistance=document.createElement("th");
  tr.appendChild(thDistance);
  var distanceLink=document.createElement("a");
  distanceLink.setAttribute("href","#");
  distanceLink.setAttribute("onclick","reorderStoresByDistanceAscending();return false;");
  distanceLink.appendChild(document.createTextNode("Distance"));
  thDistance.appendChild(distanceLink);

  // Note
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.setAttribute("onclick","reorderStoresByNameAscending();return false;");
  nameLink.appendChild(document.createTextNode("Name"));
  thName.appendChild(nameLink);

  // Show Add link
  if (canEdit) {
    var addLink=document.createElement("a");
    addLink.setAttribute("href","/storeAddLocation");
    addLink.setAttribute("class","add addTh");
    addLink.appendChild(document.createTextNode("Add"));
    thName.appendChild(addLink);
  }

  // Dishes
  var thType=document.createElement("th");
  tr.appendChild(thType);
  var typeLink=document.createElement("a");
  typeLink.setAttribute("href","#");
  typeLink.setAttribute("onclick","reorderStoresByDishCountDescending();return false;");
  typeLink.appendChild(document.createTextNode("Dishes"));
  thType.appendChild(typeLink);

  return table;
}

function createTableRowForStore(store) {
  var tr=document.createElement("tr");
  // Attributes
  var storeId=store.getAttribute("storeId");
  tr.setAttribute("storeId",storeId);
  tr.setAttribute("name",store.getAttribute("text").toLowerCase());
  tr.setAttribute("lat",store.getAttribute("lat"));
  tr.setAttribute("lon",store.getAttribute("lon"));
  //tr.setAttribute("yes",store.getAttribute("yes"));
  tr.setAttribute("dishCount",store.getAttribute("dishCount"));
  
  // Distance and bearing
  tr.appendChild(document.createElement("td"));

  // Desc
  var desc=document.createElement("td");
  var descLink=document.createElement("a");
  descLink.setAttribute("href","/store?storeId="+storeId);
  var text=store.getAttribute("text");
  descLink.appendChild(document.createTextNode(text));
  desc.appendChild(descLink);
  tr.appendChild(desc);

  // Count
  var type=document.createElement("td");
  type.setAttribute("class","center");
  var typeLink=document.createElement("a");
  typeLink.setAttribute("href","/store?storeId="+storeId);
  typeLink.appendChild(document.createTextNode(store.getAttribute("dishCount")));
  type.appendChild(typeLink);
  tr.appendChild(type);

  return tr;
}

function createTableRowForNoCachedData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","7");
  td.appendChild(document.createTextNode("No connectivity or cached data.  Please try again later."));
  tr.appendChild(td);
  return tr;
}

function createTableRowForNoData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","7");
  td.appendChild(document.createTextNode("No nearby restaurants."));
  tr.appendChild(td);
  return tr;
}

function displayTableNoCachedData() {
  var tableDiv=document.getElementById("data");
  var table=createTable();
  table.appendChild(createTableRowForNoCachedData());
  removeChildrenFromElement(tableDiv);
  tableDiv.appendChild(table);
}

///////////////////
// Coordinates
///////////////////

function geocodePosition(pos) {
  if (geocoder ) {
    geocoder.geocode({
      latLng: pos
    }, function(responses) {
      if (responses && responses.length > 0) {
        updateGeoStatus(responses[0].formatted_address);
      } else {
        updateGeoStatus('Cannot determine address at this location.');
      }
    });
  }
}

function getCoordinates() {
  var useGeoLocation=localStorage.getItem("useGeoLocation");
  if (useGeoLocation==null || useGeoLocation=="true") {
    updateGeoStatus(waitingForCoordinatesMessage);
    var geolocation = navigator.geolocation;
    if (geolocation) {
      geolocation.getCurrentPosition(setPosition,displayError);
    } else {
      updateGeoStatus(locationNotAvailableMessage);
    }
  } else {
    if (typeof(google)!="undefined") {
      var latLng = new google.maps.LatLng(localStorage.getItem("latitude"), localStorage.getItem("longitude"));
      geocodePosition(latLng);
    }
    getStoresData();
    // Update buttons
    var addButtonDisabled=document.getElementById("addButtonDisabled");
    if (addButtonDisabled) {
      addButtonDisabled.style.display='none';
    }
    var addButtonEnabled=document.getElementById("addButtonEnabled");
    if (addButtonEnabled) {
      addButtonEnabled.style.display='inline';
    }
  }
}

// Set global variables holding the position
function setPosition(position){
  var display="N/A";
  if (position){
    // Set global variables
    setItemIntoLocalStorage("latitude",position.coords.latitude);
    setItemIntoLocalStorage("longitude",position.coords.longitude);
    display="";
    // Update buttons
    var addButtonDisabled=document.getElementById("addButtonDisabled");
    if (addButtonDisabled) {
      addButtonDisabled.style.display='none';
    }
    var addButtonEnabled=document.getElementById("addButtonEnabled");
    if (addButtonEnabled) {
      addButtonEnabled.style.display='inline';
    }
    getStoresData();
    if (google) {
      var latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
      geocodePosition(latLng);
    }
  }
  updateGeoStatus(display);
}

if (typeof(Number.prototype.toRad) === "undefined") {
  Number.prototype.toRad = function() {
    return this * Math.PI / 180;
  }
}

// Converts radians to numeric (signed) degrees
if (typeof(Number.prototype.toDeg) === "undefined") {
  Number.prototype.toDeg = function() {
    return this * 180 / Math.PI;
  }
}

function calculateDistance(lat1, lon1, lat2, lon2) {
  var R = 6371; // km
  var dLat = (lat2-lat1).toRad();
  var dLon = (lon2-lon1).toRad();
  var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) *
          Math.sin(dLon/2) * Math.sin(dLon/2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  var d = R * c;
  return d;
}

function calculateBearing(lat1, lon1, lat2, lon2) {
  var dLon=(lon2-lon1).toRad();
  var lat1r=lat1.toRad();
  var lat2r=lat2.toRad();
  var y = Math.sin(dLon) * Math.cos(lat2r);
  var x = Math.cos(lat1r)*Math.sin(lat2r) -
          Math.sin(lat1r)*Math.cos(lat2r)*Math.cos(dLon);
  var bearing = Math.atan2(y, x).toDeg();
  return (bearing+360)%360;
}

///////////////////
// Sorting
///////////////////

function reorderStores(sortFunction) {
  var stores=document.getElementById("stores");
  var notes=stores.getElementsByTagName("tr");
  var notesTemp=new Array();
  for (var i=1; i<notes.length; i++) {
    notesTemp.push(notes[i]);
  }
  notesTemp.sort(sortFunction);
  for (var i=0; i<notesTemp.length; i++) {
    stores.appendChild(notesTemp[i]);
  }
}

function sortByDishCountDescending(note1,note2) {
  var dishCount1=parseFloat(note1.getAttribute("dishCount"));
  var dishCount2=parseFloat(note2.getAttribute("dishCount"));
  if (dishCount1>dishCount2) {
      return -1;
  } else if (dishCount2>dishCount1) {
      return 1;
  } else {
      return 0;
  }
}

function sortByDistanceAscending(note1,note2) {
  var distance1=parseFloat(note1.getAttribute("distance"));
  var distance2=parseFloat(note2.getAttribute("distance"));
  if (distance2>distance1) {
      return -1;
  } else if (distance1>distance2) {
      return 1;
  } else {
      return 0;
  }
}

function sortByNameAscending(note1,note2) {
  var name1=note1.getAttribute("name");
  var name2=note2.getAttribute("name");
  return name1.localeCompare(name2);
}

function reorderStoresByDishCountDescending() {
  setItemIntoLocalStorage("sortBy","dishCount");
  reorderStores(sortByDishCountDescending);
}

function reorderStoresByDistanceAscending() {
  setItemIntoLocalStorage("sortBy","distance");
  reorderStores(sortByDistanceAscending);
}

function reorderStoresByNameAscending() {
  setItemIntoLocalStorage("sortBy","name");
  reorderStores(sortByNameAscending);
}

///////////////////
// Display
///////////////////

function updateLocationDispay() {
  // Current location
  var latitude=parseFloat(localStorage.getItem("latitude"));
  var longitude=parseFloat(localStorage.getItem("longitude"));
  // For each store
  var stores=document.getElementById("stores");
  var storeRows=stores.getElementsByTagName("tr");
  for (var i=1; i<storeRows.length; i++) {
    var store=storeRows[i];
    var storeLat=parseFloat(store.getAttribute("lat"));
    var storeLon=parseFloat(store.getAttribute("lon"));
    var display="";
    // Distance
    var distance=calculateDistance(latitude, longitude, storeLat, storeLon);
    // Save for ordering
    store.setAttribute("distance",distance);
    // Add distance to display
    if (distance<1){
      display=Math.round(distance*1000)+"m";
    } else {
      display=Math.round(distance*10)/10 +"km";
    }
    // Bearing
    var bearingDegrees=calculateBearing(latitude, longitude, storeLat, storeLon);
    display+=" " + getCardinalDirection(bearingDegrees);
    display="<a href='/storeUpdateLocation?storeId=" + store.getAttribute("storeId") + "'>"+display+"</a>";
    // Update direction display
    store.getElementsByTagName("td")[0].innerHTML=display;
  }
  // Sort
  var sortBy=localStorage.getItem("sortBy");
  if (sortBy==null || sortBy=="name") {
    reorderStoresByNameAscending();
  } else if (sortBy=="distance") {
    reorderStoresByDistanceAscending();
  } else if (sortBy=="dishCount") {
    reorderStoresByDishCountDescending();
  }
}

function getCardinalDirection(degrees) {
  var value;
  if (degrees>=22.5 && degrees<=67.5){
    value="NE";
  } else if (degrees>67.5 && degrees<112.5) {
    value="E";
  } else if (degrees>=112.5 && degrees<=157.5) {
    value="SE";
  } else if (degrees>157.5 && degrees<202.5) {
    value="S";
  } else if (degrees>=202.5 && degrees<=247.5) {
    value="SW";
  } else if (degrees>247.5 && degrees<292.5) {
    value="W";
  } else if (degrees>=292.5 && degrees<=337.5) {
    value="NW";
  } else {
    value="N";
  }
  return value;
}

function displayError(error){
  updateGeoStatus(locationNotFoundMessage + ": (" + error.code + ") " + error.message);
}

function updateGeoStatus(text) {
  document.getElementById("geoStatus").innerHTML=text;
}

///////////////////
// Utils
///////////////////

function get2Decimals(number) {
  return Math.floor(number*100)/100;
}

function removeChildrenFromElement(element) {
  if (element.hasChildNodes()) {
    while (element.childNodes.length>0) {
      element.removeChild(element.firstChild);
    }
  }
}

function setItemIntoLocalStorage(key, value) {
  try {
    localStorage.setItem(key, value);
  } catch (e) {
    if (e == QUOTA_EXCEEDED_ERR) {
      // Clear old entries - TODO - In future, just clear oldest?
      localStorage.clear();
    }
  }
}

///////////////////
// Set-up page
///////////////////

function setUpPage() {
  if (typeof(google)!="undefined") {
    geocoder = new google.maps.Geocoder();
  }

  // Check if logged in
  var dishRevUser=getCookie("dishRevUser");
  isLoggedIn=false;
  if (dishRevUser!="") {
    isLoggedIn=true;
  }

  // Show 'My Reviews' if logged in
  var myReviews=document.getElementById("myReviews");  
  if (isLoggedIn) {
     myReviews.style.display='inline';
  } else {
     myReviews.style.display='none';
  }
  
  // If online, show FB login
  // If offline, show offline
  var fblogin=document.getElementById("fblogin");  
  var fbname=document.getElementById("fbname");  
  var offline=document.getElementById("offline");  
  if (navigator.onLine) {
    fblogin.style.display="inline";
    fbname.style.display="inline";
    offline.style.display="none";
  } else {
    fblogin.style.display="none";  
    fbname.style.display="none";
    offline.style.display="inline";
  }
  
  // Vary login link
  var login=document.getElementById("logonLink");
  if (isLoggedIn) {
    login.innerHTML="Log Off";
    login.addEventListener('click', function(){ FB.logout(); }); 
  } else {
    login.innerHTML="Logon";
    login.addEventListener('click', function(){ FB.login(); });
  }
  
  // If logged in and online, can edit
  canEdit=isLoggedIn && navigator.onLine;
}

function setOnlineListeners() {
  document.body.addEventListener("offline", setUpPage, false)
  document.body.addEventListener("online", setUpPage, false);
}

setOnlineListeners();
setUpPage();
displayCachedDataIfExists();
getCoordinates();