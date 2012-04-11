<%-- This JSP has the HTML for location page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStartAppCache.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="changeLocationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
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
window.onunload=setFieldsIntoLocalStorage;
function handleKeyPressAddress(e){
  var key=e.keyCode;
  if (key==13){
	geocodeAddress(document.getElementById('address').value);
    document.getElementById("useOverride").checked="checked";
  }
}
function onblurAddress(){
  geocodeAddress(document.getElementById('address').value);
  document.getElementById("useOverride").checked="checked";
}
</script>
</head>
<body onload="setFieldsFromLocalStorage()">
<p>
  <input type="radio" name="location" id="useGeoLocation" value="useGeoLocation"/><label for="useGeoLocation"><fmt:message key="currentLocationLabel"/></label>
</p>
<p>
  <input type="radio" name="location" id="useOverride" value="useOverride"/> <input id="address" value="" onblur="onblurAddress()" onkeypress="handleKeyPressAddress(event)"></input>
</p>
<script type="text/javascript">
var geocoder = new google.maps.Geocoder();
var changeLatitude;
var changeLongitude;
var map;
var marker;

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

// Onload handler to fire off the app.
google.maps.event.addDomListener(window, 'load', initialize);
</script>
<section id="mapCanvas"></section>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>