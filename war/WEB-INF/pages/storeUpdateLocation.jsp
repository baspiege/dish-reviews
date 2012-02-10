<%-- This JSP has the HTML for update location page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="locationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<table>
  <tr><td><fmt:message key="addressLabel"/>:</td><td><span id="address"></span></td></tr>
</table>
<section>
<c:choose>
  <c:when test="${pageContext.request.userPrincipal.name != null}">
    <%-- Update --%>
    <form id="store" method="post" action="storeUpdateLocation" autocomplete="off">
    <input id="latitude" type="hidden" name="latitude" value="" />
    <input id="longitude" type="hidden" name="longitude" value="" />
    <input type="hidden" name="storeId" value="<c:out value="${store.key.id}"/>"/>
    <input class="button" type="submit" name="action" onclick="setFieldsFromLocalStorage();" value="<fmt:message key="updateLabel"/>"/>
    </form>
  </c:when>
</c:choose>
</section>
<script type="text/javascript">
function setFieldsFromLocalStorage() {
  document.getElementById("latitude").value=addLatitude;
  document.getElementById("longitude").value=addLongitude;
}

var geocoder = new google.maps.Geocoder();

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

var addLatitude;
var addLongitude;

function initialize() {
  var lat=<c:out value="${store.latitude}"/>;
  var lon=<c:out value="${store.longitude}"/>;
  addLatitude=lat;
  addLongitude=lon;
  var latLng = new google.maps.LatLng(lat, lon);
  var map = new google.maps.Map(document.getElementById('mapCanvas'), {
    zoom: 16,
    center: latLng,
    mapTypeId: google.maps.MapTypeId.HYBRID
  });
  var marker = new google.maps.Marker({
    position: latLng,
    title: 'Location',
    map: map

    <c:choose>
      <c:when test="${pageContext.request.userPrincipal.name != null}">
        , draggable: true
      </c:when>
    </c:choose>

    });

  // Update current position info.
  // updateMarkerPosition(latLng);
  geocodePosition(latLng);

  // Add dragging event listeners.
  google.maps.event.addListener(marker, 'dragstart', function() {
    updateMarkerAddress('');
  });

  google.maps.event.addListener(marker, 'drag', function() {
    // updateMarkerPosition(marker.getPosition());
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