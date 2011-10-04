<%-- This JSP has the HTML for update location page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.StoreUpdate" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!=null;

    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Long storeId=RequestUtils.getNumericInput(request,"id","id",true);

    Store store=null;
    if (storeId!=null) {
        new StoreGetSingle().execute(request);
        // If note is null, forward to main page
        store=(Store)request.getAttribute("store");
        if (store==null) {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/storesRedirect.jsp"/>
            <%
        }
        // Can only edit own note
        if (isSignedIn) {
            isSignedIn=request.getUserPrincipal().getName().equalsIgnoreCase(store.user);
        }
    } else {
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/storesRedirect.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals(bundle.getString("updateLabel"))) {		
            // Fields
            RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
            RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);		
            if (!RequestUtils.hasEdits(request)) {
                new StoreUpdate().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/storesRedirect.jsp"/>
                <%
            }
        } else {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/storesRedirect.jsp"/>
            <%
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("locationLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>
<table>
  <tr><td><%=bundle.getString("positionLabel")%>:</td><td><span id="info"></span></td></tr>
  <tr><td><%=bundle.getString("addressLabel")%>:</td><td><span id="address"></span></td></tr>
</table>
<div style="margin-top:1em;margin-bottom:1em;">
<%-- Signed In --%>
<% if (isSignedIn) { %>
<form id="store" method="post" action="storeUpdateLocation.jsp" autocomplete="off">
<%-- Update --%>
<input class="button" type="submit" name="action" onclick="setFieldsFromLocalStorage();" value="<%=bundle.getString("updateLabel")%>"/>
<input id="latitude" type="hidden" name="latitude" value="" />
<input id="longitude" type="hidden" name="longitude" value="" />
<input type="hidden" name="id" value="<%=new Long(store.getKey().getId()).toString()%>"/>
</form>
<% } %>
</div>
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
  var lat=<%=store.latitude%>;
  var lon=<%=store.longitude%>;
  addLatitude=lat;
  addLongitude=lon;
  var latLng = new google.maps.LatLng(lat, lon);
  var map = new google.maps.Map(document.getElementById('mapCanvas'), {
    zoom: 18,
    center: latLng,
    mapTypeId: google.maps.MapTypeId.HYBRID
  });
  var marker = new google.maps.Marker({
    position: latLng,
    title: 'Location',
    map: map
    <%if(isSignedIn){%>, draggable: true <%}%>
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
    // map.setCenter(marker.getPosition())
    addLatitude=marker.getPosition().lat();
    addLongitude=marker.getPosition().lng();
  });
}

// Onload handler to fire off the app.
google.maps.event.addDomListener(window, 'load', initialize);
</script>
<div id="mapCanvas"></div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>