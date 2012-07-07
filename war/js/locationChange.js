///////////////////
// Global vars
///////////////////

var geocoder = new google.maps.Geocoder();
var changeLatitude;
var changeLongitude;
var map;
var marker;

///////////////////
// Local storage
///////////////////

function setFieldsFromLocalStorage() {
  var useGeoLocation=localStorage.useGeoLocation;
  if (typeof(useGeoLocation)=="undefined" || useGeoLocation=="true") {
    document.getElementById("useGeoLocation").checked="checked";
  } else {
    document.getElementById("useOverride").checked="checked";
  }
}

function setFieldsIntoLocalStorage() {
  if (document.getElementById("useGeoLocation").checked) {
    localStorage.useGeoLocation="true";
  } else {
    localStorage.useGeoLocation="false";
    localStorage.latitude=changeLatitude;
    localStorage.longitude=changeLongitude;
  }
}

///////////////////
// Event handlers
///////////////////

function onchangeTypedAddress(){
  geocodeAddress(document.getElementById('address').value);
  document.getElementById("useOverride").checked="checked";
}

function onchangeCurrentLocation(){
  if (!document.getElementById("useOverride").checked) {
	getCoordinates();
  }
}

///////////////////
// Geo location
///////////////////

function getCoordinates() {
  var geolocation = navigator.geolocation;
  if (geolocation) {
    geolocation.getCurrentPosition(setPosition);
  }
}

function setPosition(position){
  if (position){
    var latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
    map.panTo(latLng);
    marker.setPosition(latLng);
    geocodePosition(latLng);
  }
}

function geocodeAddress(addressToFind) {
  geocoder.geocode({
    address: addressToFind
  }, function(responses) {
    if (responses && responses.length > 0) {
      updateMarkerAddress(responses[0].formatted_address);
      changeLatitude=responses[0].geometry.location.lat();
	    changeLongitude=responses[0].geometry.location.lng();
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
  var lat=localStorage.latitude;
  var lon=localStorage.longitude;

  if (typeof(lat)=="undefined" || typeof(lon)=="undefined") {
    lat="41.87580845479022";
    lon="-87.6189722061157";
  }

  // Temp variables
  changeLatitude=lat;
  changeLongitude=lon;
  var latLng = new google.maps.LatLng(lat, lon);
  map = new google.maps.Map(document.getElementById('mapCanvas'), {
    zoom: 16,
    center: latLng,
    mapTypeId: google.maps.MapTypeId.HYBRID
  });
  marker = new google.maps.Marker({
    position: latLng,
    title: 'Location',
    map: map,
    draggable: true
  });

  // Update current position info.
  geocodePosition(latLng);

  // Add dragging event listeners.
  google.maps.event.addListener(marker, 'dragstart', function() {
    updateMarkerAddress('');
  });

  google.maps.event.addListener(marker, 'dragend', function() {
    geocodePosition(marker.getPosition());
    changeLatitude=marker.getPosition().lat();
    changeLongitude=marker.getPosition().lng();
    document.getElementById("useOverride").checked="checked";
  });
}

///////////////////
// Set-up page
///////////////////

function setUpPage() {
  // Initialize fields from local storage
  setFieldsFromLocalStorage();
  
  // When using geolocation, get current location.
  var useGeoLocation=document.getElementById("useGeoLocation");
  useGeoLocation.onchange=onchangeCurrentLocation;
  
  // Update address when typed over
  var address=document.getElementById("address");
  address.onchange=onchangeTypedAddress;

  // When leaving the page, set fields into local storage.
  window.onunload=setFieldsIntoLocalStorage;
}

function setUpMap() {
  // Onload handler to intialize the map.
  google.maps.event.addDomListener(window, 'load', initialize);
}

setUpPage();
setUpMap();