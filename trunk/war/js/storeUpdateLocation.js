///////////////////
// Global vars
///////////////////

var canEdit=false;
var isLoggedIn=false;
var initialLat;
var initialLon;
var geocoder = new google.maps.Geocoder();
var addLatitude;
var addLongitude;
var map;
var marker;

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
// Local storage
///////////////////

function setFieldsFromLocalStorage() {
  document.getElementById("latitude").value=addLatitude;
  document.getElementById("longitude").value=addLongitude;
}

///////////////////
// Event handlers
///////////////////

function onchangeTypedAddress(){
  geocodeAddress(document.getElementById('address').value);
}

///////////////////
// Geo location
///////////////////

function geocodeAddress(addressToFind) {
  geocoder.geocode({
    address: addressToFind
  }, function(responses) {
    if (responses && responses.length > 0) {
      updateMarkerAddress(responses[0].formatted_address);
      addLatitude=responses[0].geometry.location.lat();
	  addLongitude=responses[0].geometry.location.lng();
	  map.panTo(responses[0].geometry.location);
	  marker.setPosition(responses[0].geometry.location);
    } else {
      updateMarkerAddress('Cannot determine address at this location.');
    }
  });
}

function geocodePosition(pos) {
  geocoder.geocode({
    latLng: pos
  }, function(responses) {
    if (responses && responses.length > 0) {
      updateMarkerAddress(responses[0].formatted_address);
    } else {
      updateMarkerAddress('Cannot determine address at this location.');
    }
  });
}

function updateMarkerAddress(str) {
  document.getElementById('address').value = str;
}

function initialize() {
  addLatitude=initialLat;
  addLongitude=initialLon;
  var latLng = new google.maps.LatLng(initialLat, initialLon);
  map = new google.maps.Map(document.getElementById('mapCanvas'), {
    zoom: 16,
    center: latLng,
    mapTypeId: google.maps.MapTypeId.HYBRID
  });
  marker = new google.maps.Marker({
    position: latLng,
    title: 'Location',
    map: map,
    draggable: canEdit
    });

  // Update current position info.
  geocodePosition(latLng);

  // Add dragging event listeners.
  google.maps.event.addListener(marker, 'dragstart', function() {
    updateMarkerAddress('');
  });

  google.maps.event.addListener(marker, 'dragend', function() {
    geocodePosition(marker.getPosition());
    // map.setCenter(marker.getPosition())
    addLatitude=marker.getPosition().lat();
    addLongitude=marker.getPosition().lng();
  });
}

///////////////////
// Set-up Page
///////////////////

function setUpPage() {

  // Set initial values
  initialLat=parseFloat(document.getElementById("storeLatitude").innerHTML);
  initialLon=parseFloat(document.getElementById("storeLongitude").innerHTML);
 
  // Check if logged in
  var dishRevUser=getCookie("dishRevUser");
  isLoggedIn=false;
  if (dishRevUser!="") {
    isLoggedIn=true;
  }
  
  // If logged in and online, can edit
  canEdit=isLoggedIn && navigator.onLine;
  
  // Set fields when submitting
  var submitLocation=document.getElementById("submitLocation");
  if (submitLocation) {
    submitLocation.onclick=setFieldsFromLocalStorage;
  }
  
  // Update address when typed over
  var address=document.getElementById("address");
  if (address) {
    address.onchange=onchangeTypedAddress;
  }
}

function setUpMap() {
  // Onload handler to intialize the map.
  google.maps.event.addDomListener(window, 'load', initialize);
}

setUpPage();
setUpMap();