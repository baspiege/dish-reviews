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

function getGeoNotesData() {
  // Get position and send request
  var lat=getCookie("latitude");
  var lon=getCookie("longitude");
  sendRequest('geoNotesTable.jsp?latitude='+lat+'&longitude='+lon, handleGeoNotesDataRequest);
}

function getGeoNotesDataById(id) {
  sendRequest('geoNotesTable.jsp?id='+id, handleGeoNotesDataRequest);
}

function handleGeoNotesDataRequest(req) {
  var table=document.createElement("table");
  table.setAttribute("id","geoNotes");
  var tr=document.createElement("tr");
  // Distance
  var thDistance=document.createElement("th");
  tr.appendChild(thDistance);
  var distanceLink=document.createElement("a");
  distanceLink.setAttribute("href","#");
  distanceLink.setAttribute("onclick","reorderGeoNotesByDistanceAscending();return false;");
  distanceLink.appendChild(document.createTextNode("Distance"));  
  thDistance.appendChild(distanceLink);
  
  // Note
  var thNote=document.createElement("th");
  tr.appendChild(thNote);
  thNote.appendChild(document.createTextNode("Name"));  

  // Add
  /*
  var addButton=document.createElement("input");
  addButton.setAttribute("class","button");
  addButton.setAttribute("type","button");
  addButton.setAttribute("id","addButtonEnabled");
  addButton.setAttribute("name","action");
  addButton.setAttribute("value","Add");
  addButton.setAttribute("onclick","window.location='geoNoteAddLocation.jsp';");
  thNote.appendChild(addButton);  
  */
  
  var addLink=document.createElement("a");
  addLink.setAttribute("href","geoNoteAddLocation.jsp");
  addLink.setAttribute("class","add addTh");
  addLink.appendChild(document.createTextNode("Add"));
  thNote.appendChild(addLink);
  
  table.appendChild(tr);
  
  // Id
  //var thId=document.createElement("th");
  //tr.appendChild(thId);
  //thId.appendChild(document.createTextNode("Id"));
  // Vote
  var thVote=document.createElement("th");
  tr.appendChild(thVote);
  var voteLink=document.createElement("a");
  voteLink.setAttribute("href","#");
  voteLink.setAttribute("onclick","reorderGeoNotesByVoteYesDescending();return false;");
  voteLink.appendChild(document.createTextNode("Vote"));  
  thVote.appendChild(voteLink);
  // Image
  var thImage=document.createElement("th");
  tr.appendChild(thImage);
  thImage.appendChild(document.createTextNode("Image"));
  // Type
  /*
  var thType=document.createElement("th");
  tr.appendChild(thType);
  var typeLink=document.createElement("a");
  typeLink.setAttribute("href","#");
  typeLink.setAttribute("onclick","reorderGeoNotesByTypeAscending();return false;");
  typeLink.appendChild(document.createTextNode("Type"));  
  thType.appendChild(typeLink);  
  */
  
  // Reviews
  var thType=document.createElement("th");
  tr.appendChild(thType);
  var typeLink=document.createElement("a");
  typeLink.setAttribute("href","#");
  typeLink.setAttribute("onclick","reorderGeoNotesByReviewAscending();return false;");
  typeLink.appendChild(document.createTextNode("Dishes"));  
  thType.appendChild(typeLink);  
  
  // Process request
  var xmlDoc=req.responseXML;
  var geoNotes=xmlDoc.getElementsByTagName("geoNote");
  if (geoNotes.length==0){
    var tr=document.createElement("tr");
    var td=document.createElement("td");
    td.setAttribute("colspan","7");
    td.appendChild(document.createTextNode("No nearby restaurants."));
    tr.appendChild(td);
    table.appendChild(tr);
    var tableDiv=document.getElementById("geoNotesDiv");
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
  } else {
    // Make HTML for each geoNote
    for (var i=0;i<geoNotes.length;i++) {
      var geoNote=geoNotes[i];
      // User
      var user=geoNote.getAttribute("user")=="true";
      var tr=document.createElement("tr");
      // Attributes
      var id=geoNote.getAttribute("id");
      tr.setAttribute("id",id);
      tr.setAttribute("lat",geoNote.getAttribute("lat"));
      tr.setAttribute("lon",geoNote.getAttribute("lon"));
      tr.setAttribute("yes",geoNote.getAttribute("yes"));
      // Distance and bearing
      tr.appendChild(document.createElement("td"));
      
      // Desc
      var desc=document.createElement("td");
      if (user) {
        var descLink=document.createElement("a");
        descLink.setAttribute("href","geoNoteUpdate.jsp?id="+id);
        var text=geoNote.getAttribute("text");
        if (text=="") {
          text="Add";
          descLink.setAttribute("class","add");
        }
        descLink.appendChild(document.createTextNode(text));
        desc.appendChild(descLink);
      } else {
        var text=geoNote.getAttribute("text");
        desc.appendChild(document.createTextNode(text));
      }
      tr.appendChild(desc);
      table.appendChild(tr);
      
      // Vote
      var vote=document.createElement("td")
      var voteButton=document.createElement("button");
      voteButton.setAttribute("onclick","sendYesVote(this)");
      voteButton.appendChild(document.createTextNode(geoNote.getAttribute("yes")));
      vote.appendChild(voteButton);
      tr.appendChild(vote);
      // Image
      var imageCell=document.createElement("td");
      if (geoNote.getAttribute("img")=="true") {
        var imageLink=document.createElement("a");
        imageLink.setAttribute("href","geoNoteImage.jsp?id="+id);
        var image=document.createElement("img");
        image.setAttribute("src","geoNoteThumbNailImage?id="+id);
        imageLink.appendChild(image);
        imageCell.appendChild(imageLink);
      } else if (user) {
        var imageLink=document.createElement("a");
        imageLink.setAttribute("href","geoNoteImage.jsp?id="+id);
        imageLink.setAttribute("class","add");
        imageLink.appendChild(document.createTextNode("Add"));
        imageCell.appendChild(imageLink);
      }
      tr.appendChild(imageCell);      
      // Type
      /*
      var type=document.createElement("td");
      if (user) {
        var typeLink=document.createElement("a");
        typeLink.setAttribute("href","geoNoteUpdate.jsp?id="+id);
        typeLink.appendChild(document.createTextNode(geoNote.getAttribute("type")));
        type.appendChild(typeLink);
      } else {
        type.appendChild(document.createTextNode(geoNote.getAttribute("type")));
      }
      tr.appendChild(type);
      */
      
      var type=document.createElement("td");
      var typeLink=document.createElement("a");
      typeLink.setAttribute("href","dishes.jsp?storeId="+id);
      typeLink.appendChild(document.createTextNode(geoNote.getAttribute("dishCount")));
      type.appendChild(typeLink);
      tr.appendChild(type);
    }
    var tableDiv=document.getElementById("geoNotesDiv");
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
  var id=parseInt(tr.getAttribute("id"));
  tr.setAttribute("yes",yes+1);
  elem.innerHTML=yes+1;
  sendRequest('GeoNoteVote.jsp?vote=yes&id='+id);
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
      var parameters={enableHighAccuracy:true, maximumAge:20000, timeout:20000};
      geolocation.watchPosition(setPosition,displayError,parameters);
    } else {
      updateGeoStatus(locationNotAvailableMessage);
    }
  } else {      
    if (typeof(google)!="undefined") {
      var latLng = new google.maps.LatLng(getCookie("latitude"), getCookie("longitude"));
      geocodePosition(latLng);
    }
    getGeoNotesData();
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
    getGeoNotesData();
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

function reorderGeoNotes(sortFunction) {
  var geoNotes=document.getElementById("geoNotes");
  var notes=geoNotes.getElementsByTagName("tr");
  var notesTemp=new Array();
  for (var i=1; i<notes.length; i++) {
    notesTemp.push(notes[i]);
  }
  notesTemp.sort(sortFunction);
  for (var i=0; i<notesTemp.length; i++) {
    geoNotes.appendChild(notesTemp[i]);
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

function sortByTimeDescending(note1,note2) {
  var time1=parseFloat(note1.getAttribute("time"));
  var time2=parseFloat(note2.getAttribute("time"));
  if (time1>time2) {
      return -1;
  } else if (time2>time1) {
      return 1;
  } else {
      return 0;
  }
}

function sortByTypeAscending(note1,note2) {
  var type1=note1.getAttribute("type");
  var type2=note2.getAttribute("type");
  return type1.localeCompare(type2);
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

function reorderGeoNotesByTimeDescending() {
  setCookie("sortBy","time");
  reorderGeoNotes(sortByTimeDescending);
}

function reorderGeoNotesByDistanceAscending() {
  setCookie("sortBy","distance");
  reorderGeoNotes(sortByDistanceAscending);
}

function reorderGeoNotesByTypeAscending() {
  setCookie("sortBy","type");
  reorderGeoNotes(sortByTypeAscending);
}

function reorderGeoNotesByVoteYesDescending() {
  setCookie("sortBy","voteYes");
  reorderGeoNotes(sortByVoteYesDescending);
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
  var geoNotes=document.getElementById("geoNotes");
  var notes=geoNotes.getElementsByTagName("tr");
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
    display="<a href='geoNoteUpdateLocation.jsp?id=" + note.getAttribute("id") + "'>"+display+"</a>";
    // Update direction display
    note.getElementsByTagName("td")[0].innerHTML=display;
  }
  // Sort
  var sortBy=getCookie("sortBy");
  if (sortBy=="" || sortBy=="distance") {
    reorderGeoNotesByDistanceAscending();
  } else if (sortBy=="time") {
    reorderGeoNotesByTimeDescending();
  } else if (sortBy=="type") {
    reorderGeoNotesByTypeAscending();
  } else if (sortBy=="voteYes") {
    reorderGeoNotesByVoteYesDescending();
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

function getElapsedTime(oldSeconds,newSeconds){
  var display="";
  var seconds=newSeconds-oldSeconds;
  if (seconds<60){
    display=Math.round(seconds)+" sec";
  } else {
    var minutes=seconds/60;
    if (minutes<60) {
      display=Math.round(minutes)+" min";
    } else {
      var hours=minutes/60;
      if (hours<24) {
        display=Math.round(hours)+" hr";
      } else {
        var days=hours/24;
        display=Math.round(days)+" days";
      }
    }
  }
  return display;
}

function displayError(error){
  updateGeoStatus(locationNotFoundMessage + ": (" + error.code + ") " + error.message);
}

function updateGeoStatus(text) {
  document.getElementById("geoStatus").innerHTML=text;
}