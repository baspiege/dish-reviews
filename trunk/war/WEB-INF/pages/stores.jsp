<%-- This JSP has the HTML for stores page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStartAppCache.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="storesLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="/js/stores.js" ></script>
<script type="text/javascript">
var waitingForCoordinatesMessage="<fmt:message key="waitingForCoordinatesMessage"/>";
var locationNotAvailableMessage="<fmt:message key="locationNotAvailableMessage"/>";
var locationNotFoundMessage="<fmt:message key="locationNotFoundMessage"/>";
var canEdit=false;
var isLoggedIn=false;
</script>
</head>
<body onload="setUpPage();setOnlineListeners();getCoordinates();">

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist">
<li id="myReviews" style="display:none"><a href='/reviewsOwn'><fmt:message key="myReviewsLabel"/></a></li>
<li id="fblogin" style="display:none"><fb:login-button autologoutlink="true"></fb:login-button></li>
<li id="fbname" class="nw" style="display:none"><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<li id="offline" style="display:none"><fmt:message key="offlineLabel"/></li>
</ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>

<%-- Location --%>
<section><span id="geoStatus"></span><a class="nw" style="margin-left:1em" href="/locationChange"><fmt:message key="changeLocationLabel"/></a></section>

<%-- Data --%>
<section class="data" id="data">
<progress id="progressData" style="display:none" title="<fmt:message key="waitingForDataLabel"/>"><fmt:message key="waitingForDataLabel"/></progress>
</section>

<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>