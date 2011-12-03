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

function setCookie(name,value,daysToExpire) {
  var date=new Date();
  date.setDate(date.getDate()+daysToExpire);
  document.cookie=name+"="+escape(value)+((daysToExpire==null)?"":";expires="+date.toUTCString());
}

///////////////////
// Asynch
///////////////////

var xmlHttpRequest=new XMLHttpRequest();

function sendRequest(url,callback,postData) {
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

function getStoresData() {
  // Get position and send request
  var lat=getCookie("latitude");
  var lon=getCookie("longitude");
  sendRequest('../data/stores.jsp?latitude='+lat+'&longitude='+lon, handleStoresDataRequest);
}

function getStoresDataById(storeId) {
  sendRequest('storesTable.jsp?storeId='+storeId, handleStoresDataRequest);
}

function handleStoresDataRequest(req) {
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

  // Show Add link if logged in
  if (isLoggedIn=="true") {
    var addLink=document.createElement("a");
    addLink.setAttribute("href","storeAddLocation.jsp");
    addLink.setAttribute("class","add addTh");
    addLink.appendChild(document.createTextNode("Add"));
    thName.appendChild(addLink);
  }
  
  // Reviews
  var thType=document.createElement("th");
  tr.appendChild(thType);
  var typeLink=document.createElement("a");
  typeLink.setAttribute("href","#");
  typeLink.setAttribute("onclick","reorderStoresByDishCountDescending();return false;");
  typeLink.appendChild(document.createTextNode("Dishes"));  
  thType.appendChild(typeLink);  
  
  // Process request
  var xmlDoc=req.responseXML;
  var stores=xmlDoc.getElementsByTagName("store");
  if (stores.length==0){
    var tr=document.createElement("tr");
    var td=document.createElement("td");
    td.setAttribute("colspan","7");
    td.appendChild(document.createTextNode("No nearby restaurants."));
    tr.appendChild(td);
    table.appendChild(tr);
    var tableDiv=document.getElementById("data");
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
  } else {
    // Make HTML for each store
    for (var i=0;i<stores.length;i++) {
      var store=stores[i];
      var tr=document.createElement("tr");
      // Attributes
      var storeId=store.getAttribute("storeId");
      tr.setAttribute("storeId",storeId);
      tr.setAttribute("name",store.getAttribute("text").toLowerCase());
      tr.setAttribute("lat",store.getAttribute("lat"));
      tr.setAttribute("lon",store.getAttribute("lon"));
      tr.setAttribute("yes",store.getAttribute("yes"));
      tr.setAttribute("dishCount",store.getAttribute("dishCount"));
      // Distance and bearing
      tr.appendChild(document.createElement("td"));
      
      // Desc
      var desc=document.createElement("td");
      var descLink=document.createElement("a");
      descLink.setAttribute("href","dishes.jsp?storeId="+storeId);
      var text=store.getAttribute("text");
      descLink.appendChild(document.createTextNode(text));
      desc.appendChild(descLink);
      if (isLoggedIn=="true") {
        var editLink=document.createElement("a");
        editLink.setAttribute("href","storeUpdate.jsp?storeId="+storeId);
        editLink.setAttribute("class","edit");
        editLink.appendChild(document.createTextNode("edit"));
        desc.appendChild(document.createTextNode(' '));
        desc.appendChild(editLink);
      }
      tr.appendChild(desc);
      table.appendChild(tr);
      
      // Count
      var type=document.createElement("td");
      type.setAttribute("class","center");
      var typeLink=document.createElement("a");
      typeLink.setAttribute("href","dishes.jsp?storeId="+storeId);
      typeLink.appendChild(document.createTextNode(store.getAttribute("dishCount")));
      type.appendChild(typeLink);
      tr.appendChild(type);
    }
    var tableDiv=document.getElementById("data");
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
    updateNotesDispay();
  }
}

///////////////////
// Votes
///////////////////

function sendYesVote(elem) {
  var tr=elem.parentNode.parentNode;
  var yes=parseInt(tr.getAttribute("yes"));
  var storeId=parseInt(tr.getAttribute("storeId"));
  tr.setAttribute("yes",yes+1);
  elem.innerHTML=yes+1;
  sendRequest('storeVote.jsp?vote=yes&storeId='+storeId);
}

///////////////////
// Coordinates
///////////////////

var geocoder;
if (typeof(google)!="undefined") {
  geocoder = new google.maps.Geocoder();
}

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
  var useGeoLocation=getCookie("useGeoLocation");
  if (useGeoLocation=="" || useGeoLocation=="true") {
    updateGeoStatus(waitingForCoordinatesMessage);
    var geolocation = navigator.geolocation;
    if (geolocation) {
      var parameters={maximumAge:300000, timeout:20000};
      geolocation.watchPosition(setPosition,displayError,parameters);
    } else {
      updateGeoStatus(locationNotAvailableMessage);
    }
  } else {      
    if (typeof(google)!="undefined") {
      var latLng = new google.maps.LatLng(getCookie("latitude"), getCookie("longitude"));
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
    setCookie("latitude", position.coords.latitude);
    setCookie("longitude", position.coords.longitude);
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

function sortByVoteYesDescending(note1,note2) {
  var vote1=parseInt(note1.getAttribute("yes"));
  var vote2=parseInt(note2.getAttribute("yes"));
  if (vote1>vote2) {
      return -1;
  } else if (vote2>vote1) {
      return 1;
  } else {
      return 0;
  }
}

function reorderStoresByDishCountDescending() {
  setCookie("sortBy","dishCount");
  reorderStores(sortByDishCountDescending);
}

function reorderStoresByDistanceAscending() {
  setCookie("sortBy","distance");
  reorderStores(sortByDistanceAscending);
}

function reorderStoresByNameAscending() {
  setCookie("sortBy","name");
  reorderStores(sortByNameAscending);
}

function reorderStoresByVoteYesDescending() {
  setCookie("sortBy","voteYes");
  reorderStores(sortByVoteYesDescending);
}

///////////////////
// Display
///////////////////

function removeChildrenFromElement(element) {
  if (element.hasChildNodes()) {
    while (element.childNodes.length>0) {
      element.removeChild(element.firstChild);
    }
  }
}

function updateNotesDispay() {
  // Current location
  var latitude=parseFloat(getCookie("latitude"));
  var longitude=parseFloat(getCookie("longitude"));
  // For each note
  var stores=document.getElementById("stores");
  var notes=stores.getElementsByTagName("tr");
  for (var i=1; i<notes.length; i++) {
    var note=notes[i];
    var noteLat=parseFloat(note.getAttribute("lat"));
    var noteLon=parseFloat(note.getAttribute("lon"));
    var display="";
    // Distance
    var distance=calculateDistance(latitude, longitude, noteLat, noteLon);
    // Save for ordering
    note.setAttribute("distance",distance);
    // Add distance to display
    if (distance<1){
      display=Math.round(distance*1000)+"m";
    } else {
      display=Math.round(distance*10)/10 +"km";
    }
    // Bearing
    var bearingDegrees=calculateBearing(latitude, longitude, noteLat, noteLon);
    display+=" " + getCardinalDirection(bearingDegrees);
    display="<a href='storeUpdateLocation.jsp?storeId=" + note.getAttribute("storeId") + "'>"+display+"</a>";
    // Update direction display
    note.getElementsByTagName("td")[0].innerHTML=display;
  }
  // Sort
  var sortBy=getCookie("sortBy");
  if (sortBy=="" || sortBy=="distance") {
    reorderStoresByDistanceAscending();
  } else if (sortBy=="name") {
    reorderStoresByNameAscending();
  } else if (sortBy=="dishCount") {
    reorderStoresByDishCountDescending();
  } else if (sortBy=="voteYes") {
    reorderStoresByVoteYesDescending();
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