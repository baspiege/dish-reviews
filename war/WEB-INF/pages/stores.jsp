<%-- This JSP has the HTML for stores page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="storesLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="/js/stores.js" ></script>
<script type="text/javascript">
var waitingForCoordinatesMessage="<fmt:message key="waitingForCoordinatesMessage"/>";
var locationNotAvailableMessage="<fmt:message key="locationNotAvailableMessage"/>";
var locationNotFoundMessage="<fmt:message key="locationNotFoundMessage"/>";
<c:choose>
  <c:when test="${pageContext.request.userPrincipal.name != null}">
    var isLoggedIn=true;
  </c:when>
  <c:otherwise>
    var isLoggedIn=false;
  </c:otherwise>
</c:choose>
</script>
</head>
<body onload="getCoordinates();">

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist">
<c:if test="${pageContext.request.userPrincipal.name != null}">
  <li><a href='/reviewsOwn'><fmt:message key="myReviewsLabel"/></a></li>
</c:if>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Location --%>
<section><span id="geoStatus"></span><a style="margin-left:1em" href="locationChange"><fmt:message key="changeLocationLabel"/></a></section>
<%-- Data --%>
<section class="data" id="data">
<p><fmt:message key="waitingForDataLabel"/></p>
</section>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>