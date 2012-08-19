///////////////////
// Global vars
///////////////////

var Stores=new Object();

Stores.waitingForCoordinatesMessage="Waiting for coordinates...";
Stores.locationNotAvailableMessage="Location Not Available";
Stores.locationNotFoundMessage="Location Not Found";

///////////////////
// Data
///////////////////

Stores.getCachedData=function() {
    var xmlDoc=null;
    var cachedResponse=localStorage.getItem(Stores.getStoresKey());
    if (cachedResponse) {
      var parser=new DOMParser();
      xmlDoc=parser.parseFromString(cachedResponse,"text/xml");
    }
    return xmlDoc;
}

Stores.getStoresData=function() {
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
    Stores.displayCachedDataIfExists();
    sendRequest('/storesXml?latitude='+lat+'&longitude='+lon, Stores.handleStoresDataRequest, Stores.displayCachedData);
  } else {
    Stores.displayCachedData();
  }
}

Stores.getStoresKey=function() {
  var latitude=get2Decimals(parseFloat(localStorage.getItem("latitude")));
  var longitude=get2Decimals(parseFloat(localStorage.getItem("longitude")));
  return "STORES_"+latitude+"_"+longitude;
}

Stores.handleStoresDataRequest=function(req) {
  var display=true;
  var cachedResponse=localStorage.getItem(Stores.getStoresKey());
  
  // Save in local storage in case app goes offline
  // TODO - Get lat/lon from result.  Might change between request and response.
  setItemIntoLocalStorage(Stores.getStoresKey(), req.responseText);
  
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
    Stores.displayData(xmlDoc);
  }
}

///////////////////
// Data Display
///////////////////

Stores.displayCachedDataIfExists=function() {
  var xmlDoc=Stores.getCachedData();
  if (xmlDoc) {
    Stores.displayData(xmlDoc);
  }
}

Stores.displayCachedData=function() {
  var xmlDoc=Stores.getCachedData();
  if (xmlDoc) {
    Stores.displayData(xmlDoc);
  } else {
    Stores.displayTableNoCachedData();
  }
}

Stores.displayData=function(xmlDoc) {
  // Create HTML table
  var tableDiv=document.getElementById("data");
  var table=Stores.createTable();

  var stores=xmlDoc.getElementsByTagName("store"); 
  
  // No stores
  if (stores.length==0){
    table.appendChild(Stores.createTableRowForNoData());
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
      table.appendChild(Stores.createTableRowForStore(store));
    }
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
    Stores.updateLocationDispay();
  }
}

Stores.createTable=function() {
  var table=document.createElement("table");
  table.setAttribute("id","stores");
  var tr=document.createElement("tr");
  table.appendChild(tr);

  // Distance
  var thDistance=document.createElement("th");
  tr.appendChild(thDistance);
  var distanceLink=document.createElement("a");
  distanceLink.setAttribute("href","#");
  distanceLink.setAttribute("onclick","Stores.reorderStoresByDistanceAscending();return false;");
  distanceLink.appendChild(document.createTextNode("Distance"));
  thDistance.appendChild(distanceLink);

  // Note
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.setAttribute("onclick","Stores.reorderStoresByNameAscending();return false;");
  nameLink.appendChild(document.createTextNode("Name"));
  thName.appendChild(nameLink);

  // Show Add link
  if (User.canEdit) {
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
  typeLink.setAttribute("onclick","Stores.reorderStoresByDishCountDescending();return false;");
  typeLink.appendChild(document.createTextNode("Dishes"));
  thType.appendChild(typeLink);

  return table;
}

Stores.createTableRowForStore=function(store) {
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

Stores.createTableRowForNoCachedData=function() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","3");
  td.appendChild(document.createTextNode("No connectivity or cached data.  Please try again later."));
  tr.appendChild(td);
  return tr;
}

Stores.createTableRowForNoData=function() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","3");
  td.appendChild(document.createTextNode("No nearby restaurants."));
  tr.appendChild(td);
  return tr;
}

Stores.displayTableNoCachedData=function() {
  var tableDiv=document.getElementById("data");
  var table=Stores.createTable();
  table.appendChild(Stores.createTableRowForNoCachedData());
  removeChildrenFromElement(tableDiv);
  tableDiv.appendChild(table);
}

///////////////////
// Coordinates
///////////////////

Stores.geocodePosition=function(pos) {
  if (geocoder ) {
    geocoder.geocode({
      latLng: pos
    }, function(responses) {
      if (responses && responses.length > 0) {
        Stores.updateGeoStatus(responses[0].formatted_address);
      } else {
        Stores.updateGeoStatus('Cannot determine address at this location.');
      }
    });
  }
}

Stores.getCoordinates=function() {
  var useGeoLocation=localStorage.getItem("useGeoLocation");
  if (useGeoLocation==null || useGeoLocation=="true") {
    Stores.updateGeoStatus(Stores.waitingForCoordinatesMessage);
    var geolocation = navigator.geolocation;
    if (geolocation) {
      geolocation.getCurrentPosition(Stores.setPosition,Stores.displayError);
    } else {
      Stores.updateGeoStatus(Stores.locationNotAvailableMessage);
    }
  } else {
    if (typeof(google)!="undefined") {
      var latLng = new google.maps.LatLng(localStorage.getItem("latitude"), localStorage.getItem("longitude"));
      Stores.geocodePosition(latLng);
    }
    Stores.getStoresData();
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
Stores.setPosition=function(position) {
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
    Stores.getStoresData();
    if (google) {
      var latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
      Stores.geocodePosition(latLng);
    }
  }
  Stores.updateGeoStatus(display);
}

///////////////////
// Sorting
///////////////////

Stores.reorderStores=function(sortFunction) {
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

Stores.sortByDishCountDescending=function(note1,note2) {
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

Stores.sortByDistanceAscending=function(note1,note2) {
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

Stores.sortByNameAscending=function(note1,note2) {
  var name1=note1.getAttribute("name");
  var name2=note2.getAttribute("name");
  return name1.localeCompare(name2);
}

Stores.reorderStoresByDishCountDescending=function() {
  setItemIntoLocalStorage("sortBy","dishCount");
  Stores.reorderStores(Stores.sortByDishCountDescending);
}

Stores.reorderStoresByDistanceAscending=function() {
  setItemIntoLocalStorage("sortBy","distance");
  Stores.reorderStores(Stores.sortByDistanceAscending);
}

Stores.reorderStoresByNameAscending=function() {
  setItemIntoLocalStorage("sortBy","name");
  Stores.reorderStores(Stores.sortByNameAscending);
}

///////////////////
// Display
///////////////////

Stores.updateLocationDispay=function() {
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
    display+=" " + Stores.getCardinalDirection(bearingDegrees);
    display="<a href='/storeUpdateLocation?storeId=" + store.getAttribute("storeId") + "'>"+display+"</a>";
    // Update direction display
    store.getElementsByTagName("td")[0].innerHTML=display;
  }
  // Sort
  var sortBy=localStorage.getItem("sortBy");
  if (sortBy==null || sortBy=="name") {
    Stores.reorderStoresByNameAscending();
  } else if (sortBy=="distance") {
    Stores.reorderStoresByDistanceAscending();
  } else if (sortBy=="dishCount") {
    Stores.reorderStoresByDishCountDescending();
  }
}

Stores.getCardinalDirection=function(degrees) {
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

Stores.displayError=function(error) {
  Stores.updateGeoStatus(Stores.locationNotFoundMessage + ": (" + error.code + ") " + error.message);
}

Stores.updateGeoStatus=function(text) {
  document.getElementById("geoStatus").innerHTML=text;
}

Stores.createStoresNav=function() {
  var content=document.getElementById("content");  
  var nav=document.createElement("nav");
  content.appendChild(nav);  
  
  // List
  var navUl=document.createElement("ul");
  nav.appendChild(navUl);
  navUl.setAttribute("id","navlist");

  // My Reviews
  var navItem=document.createElement("li");
  navUl.appendChild(navItem);  
  navItem.setAttribute("id","myReviews");
  navItem.setAttribute("style","display:none");
  var navItemLink=document.createElement("a");
  navItem.appendChild(navItemLink);  
  navItemLink.setAttribute("href","TestLink");
  navItemLink.appendChild(document.createTextNode("Log On")); 
  
  // Fb login
  var navItem=document.createElement("li");
  navUl.appendChild(navItem);  
  navItem.setAttribute("id","fblogin");
  navItem.setAttribute("style","display:none");
  var navItemLink=document.createElement("a");
  navItem.appendChild(navItemLink);  
  navItemLink.setAttribute("id","logonLink");
  navItemLink.setAttribute("href","#");
  navItemLink.appendChild(document.createTextNode("Log On"));  
  
  // Fb name
  var navItem=document.createElement("li");
  navUl.appendChild(navItem);  
  navItem.setAttribute("id","fbname");
  navItem.setAttribute("style","display:none");
  navItem.setAttribute("class","nw");
  //<fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name>
  navItem.appendChild(document.createTextNode("Test name"));
  
  // OffLink
  var navItem=document.createElement("li");
  navUl.appendChild(navItem);  
  navItem.setAttribute("id","offline");
  navItem.setAttribute("style","display:none");
  navItem.setAttribute("class","nw");
  navItem.appendChild(document.createTextNode("Offline"));
}

Stores.createStoresSections=function() {
  var content=document.getElementById("content");  
  
  var sectionLocation=document.createElement("section");
  content.appendChild(sectionLocation);
  var geoStatus=document.createElement("span");
  sectionLocation.appendChild(geoStatus);
  geoStatus.setAttribute("id","geoStatus");
  // TODO - <a class="nw" style="margin-left:1em" href="/locationChange">Change Location</a>
  
  var sectionData=document.createElement("section");
  content.appendChild(sectionData);
  sectionData.setAttribute("class","data");
  sectionData.setAttribute("id","data");
  // TODO - Add <progress id="progressData" style="display:none" title="Waiting for data"></progress>
}

///////////////////
// Set-up page
///////////////////

Stores.createStoresLayout=function () {
  // Clear content
  var content=document.getElementById("content");  
  removeChildrenFromElement(content);

  Stores.createStoresNav();
  Stores.createStoresSections(); 
}

Stores.setUpPage=function() {
  if (typeof(google)!="undefined") {
    geocoder = new google.maps.Geocoder();
  }

  // Check if logged in
  var dishRevUser=getCookie("dishRevUser");
  User.isLoggedIn=false;
  if (dishRevUser!="") {
    User.isLoggedIn=true;
  }

  // Show 'My Reviews' if logged in
  var myReviews=document.getElementById("myReviews");  
  if (User.isLoggedIn) {
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
  if (User.isLoggedIn) {
    login.innerHTML="Log Off";
    login.onclick=fbLogout; 
  } else {
    login.innerHTML="Logon";
    login.onclick=fbLogin;
  }
  
  // If logged in and online, can edit
  User.canEdit=User.isLoggedIn && navigator.onLine;
}

Stores.setOnlineListeners=function() {
  document.body.addEventListener("offline", Stores.setUpPage, false)
  document.body.addEventListener("online", Stores.setUpPage, false);
}

Stores.setOnlineListeners();
Stores.createStoresLayout();
Stores.setUpPage();
Stores.displayCachedDataIfExists();
Stores.getCoordinates();