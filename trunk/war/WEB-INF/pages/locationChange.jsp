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
</script>
</head>
<body onload="setFieldsFromLocalStorage()">
<p>
  <input type="radio" name="location" id="useGeoLocation" value="useGeoLocation"/><label for="useGeoLocation"><fmt:message key="currentLocationLabel"/></label>
  <input type="radio" name="location" id="useOverride" value="useOverride"/><label for="useOverride"><fmt:message key="locationBelowLabel"/></label>
</p>
<table>
  <tr><td><fmt:message key="addressLabel"/>:</td><td><span id="address"></span></td></tr>
</table>
<div style="margin-top:1em;margin-bottom:1em;">
<%-- Update --%>
<input class="button" type="button" name="action" onclick="setFieldsIntoLocalStorage();window.location='stores';return false;" value="<fmt:message key="updateLabel"/>"/>
</div>
<script type="text/javascript">
var geocoder = new google.maps.Geocoder();
var changeLatitude;
var changeLongitude;

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

function updateMarkerPosition(latLng) {
  document.getElementById('info').innerHTML = [
    latLng.lat(),
    latLng.lng()
  ].join(', ');
}

function updateMarkerAddress(str) {
  document.getElementById('address').innerHTML = str;
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
  var map = new google.maps.Map(document.getElementById('mapCanvas'), {
    zoom: 16,
    center: latLng,
    mapTypeId: google.maps.MapTypeId.HYBRID
  });
  var marker = new google.maps.Marker({
    position: latLng,
    title: 'Location',
    map: map,
    draggable: true
  });

  // Update current position info.
  //updateMarkerPosition(latLng);
  geocodePosition(latLng);

  // Add dragging event listeners.
  google.maps.event.addListener(marker, 'dragstart', function() {
    updateMarkerAddress('');
  });

  google.maps.event.addListener(marker, 'drag', function() {
    //updateMarkerPosition(marker.getPosition());
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
<body>
<section id="mapCanvas"></section>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>