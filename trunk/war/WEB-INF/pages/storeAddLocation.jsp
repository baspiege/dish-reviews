<%-- This JSP has the HTML for adjust location page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="adjustLocationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>
<input id="address" value="" onblur="onblurAddress()" onkeypress="handleKeyPressAddress(event)"></input>
<section>
<form id="store" method="get" action="storeAdd" autocomplete="off">
<%-- Update --%>
<input id="latitude" type="hidden" name="latitude" value="" />
<input id="longitude" type="hidden" name="longitude" value="" />
<input class="button" type="submit" name="action" onclick="setFieldsFromLocalStorage()" value="<fmt:message key="nextLabel"/>"/>
</form>
</section>
<script type="text/javascript">

function setFieldsFromLocalStorage() {
  document.getElementById("latitude").value=addLatitude;
  document.getElementById("longitude").value=addLongitude;
}

function handleKeyPressAddress(e){
  var key=e.keyCode;
  if (key==13){
	geocodeAddress(document.getElementById('address').value);
  }
}
function onblurAddress(){
  geocodeAddress(document.getElementById('address').value);
}

var geocoder = new google.maps.Geocoder();
var addLatitude;
var addLongitude;
var map;
var marker;

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
  var lat=localStorage.latitude;
  var lon=localStorage.longitude;
  addLatitude=lat;
  addLongitude=lon;

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
    // map.setCenter(marker.getPosition())
    addLatitude=marker.getPosition().lat();
    addLongitude=marker.getPosition().lng();
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