<%-- This JSP has the HTML for reviews page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><c:out value="${dish.note}"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/dish.js" ></script>
<script type="text/javascript">
var dishId=<c:out value="${dish.key.id}"/>;
var reviewId=<c:out value="${requestScope.reviewId}">0</c:out>;

// TODO - Put these in hidden divs?
var dishName="update this";
var storeName="update this";

<c:choose>
  <c:when test="${pageContext.request.userPrincipal != null}">
    var isLoggedIn=true;
  </c:when>
  <c:otherwise>
    var isLoggedIn=false;
  </c:otherwise>
</c:choose>

</script>
</head>
<fmt:bundle basename="Text">
<c:choose>
  <c:when test="${requestScope.reviewId != null}">
    <body onload="getReviewsDataById();">
  </c:when>
  <c:otherwise>
    <body onload="getReviewsData();">
  </c:otherwise>
</c:choose>

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores">Main</a></li>
<li><a href="store?storeId=<c:out value="${store.key.id}"/>"><c:out value="${store.key.id}"/></a></li>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<div style="margin-top:1.5em">

<%-- If logged in, link to edit page. --%> 
<c:choose>
  <c:when test="${pageContext.request.userPrincipal != null}">
    <a href="dishUpdate?dishId=<c:out value="${dish.key.id}"/>"><c:out value="${dish.note}"/></a>
  </c:when>
  <c:otherwise>
    <c:out value="${dish.note}"/>
  </c:otherwise>
</c:choose>

<%-- All reviews. --%> 
<c:choose>
  <c:when test="${requestScope.reviewId != null}">
    <a class="add" href="#" onclick="window.location='dish?dishId=<c:out value="${dish.key.id}"/>';return false;">All reviews</a>
  </c:when>
</c:choose>
</div>

<%-- Data --%>
<div class="data" id="data">
<p>
<fmt:message key="waitingForDataLabel"/>
</p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</fmt:bundle>
</body>
</html>