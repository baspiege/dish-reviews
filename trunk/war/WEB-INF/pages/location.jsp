<%-- This JSP has the HTML for location page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
%>
<title><%=bundle.getString("changeLocationLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
function setFieldsFromLocalStorage() {
  var useGeoLocation=getCookie("useGeoLocation");
  if (useGeoLocation=="" || useGeoLocation=="true") {
    document.getElementById("useGeoLocation").checked="checked";
  } else {
    document.getElementById("useOverride").checked="checked";
  }
}
function setFieldsIntoLocalStorage() {
  if (document.getElementById("useGeoLocation").checked) {
    setCookie("useGeoLocation","true");
  } else {
    setCookie("useGeoLocation","false");
    setCookie("latitude",changeLatitude);
    setCookie("longitude",changeLongitude);
  }
}
</script>
</head>
<body onload="setFieldsFromLocalStorage()">
<p>
  <input type="radio" name="location" id="useGeoLocation" value="useGeoLocation"/><label for="useGeoLocation"><%=bundle.getString("currentLocationLabel")%></label>
  <input type="radio" name="location" id="useOverride" value="useOverride"/><label for="useOverride"><%=bundle.getString("locationBelowLabel")%></label>
</p>
<table>
  <tr><td><%=bundle.getString("positionLabel")%>:</td><td><span id="info"></span></td></tr>
  <tr><td><%=bundle.getString("addressLabel")%>:</td><td><span id="address"></span></td></tr>
</table>
<div style="margin-top:1em;margin-bottom:1em;">
<%-- Update --%>
<input class="button" type="button" name="action" onclick="setFieldsIntoLocalStorage();window.location='geoNotes.jsp';return false;" value="<%=bundle.getString("updateLabel")%>"/>
</div>
<script type="text/javascript">
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
  var lat=getCookie("latitude");
  var lon=getCookie("longitude");
  
  if (lat=="" || lon=="") {
    lat="41.87580845479022";
    lon="-87.6189722061157";
  }
  
  // Temp variables
  changeLatitude=lat;
  changeLongitude=lon;
  var latLng = new google.maps.LatLng(lat, lon);
  var map = new google.maps.Map(document.getElementById('mapCanvas'), {
    zoom: 18,
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
  updateMarkerPosition(latLng);
  geocodePosition(latLng);

  // Add dragging event listeners.
  google.maps.event.addListener(marker, 'dragstart', function() {
    updateMarkerAddress('');
  });

  google.maps.event.addListener(marker, 'drag', function() {
    updateMarkerPosition(marker.getPosition());
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
<div id="mapCanvas"></div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>