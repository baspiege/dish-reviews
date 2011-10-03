<%-- This JSP has the HTML for Geo Notes page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("geoNotesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
<script type="text/javascript">
var waitingForCoordinatesMessage="<%=bundle.getString("waitingForCoordinatesMessage")%>";
var locationNotAvailableMessage="<%=bundle.getString("locationNotAvailableMessage")%>";
var locationNotFoundMessage="<%=bundle.getString("locationNotFoundMessage")%>";
</script>
</head>
<body onload="getCoordinates();">
<jsp:include page="/WEB-INF/pages/components/header.jsp"/>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Location --%>
<div style="margin-top:1.5em"><span id="geoStatus"></span><a style="margin-left:1em" href="location.jsp"><%=bundle.getString("changeLocationLabel")%></a></div>

<%-- Data --%>
<div style="margin-top:1.5em" id="geoNotesDiv">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/geoNotes.js" ></script>
</body>
</html>