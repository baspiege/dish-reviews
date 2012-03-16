<%-- This JSP has the HTML for dish page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStartAppCache.jsp" %>
<title><c:out value="${dish.note}"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/dish.js" ></script>
<script type="text/javascript">
var dishId=<c:out value="${dish.key.id}"/>;
var reviewId=<c:out value="${reviewId}">0</c:out>;
var canEdit=false;
var isLoggedIn=false;
</script>
</head>
<fmt:bundle basename="Text">

<%-- If review Id, load specific review.  Else load all. --%>
<c:choose>
  <c:when test="${reviewId != null}">
    <body onload="setUpPage();setOnlineListeners();getReviewsDataById();">
  </c:when>
  <c:otherwise>
    <body onload="setUpPage();setOnlineListeners();getReviewsData();">
  </c:otherwise>
</c:choose>

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist">
<li><a href="stores"><fmt:message key="mainLabel"/></a></li>
<li><a href="store?storeId=<c:out value="${store.key.id}"/>"><span id="storeName"><c:out value="${store.note}"/></span></a></li>
<li id="fblogin" style="display:none"><fb:login-button autologoutlink="true"></fb:login-button></li>
<li id="fbname" style="display:none"><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<li id="offline" style="display:none"><fmt:message key="offlineLabel"/></li>
</ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>

<section>
<%-- Dish name --%>
<span id="dishName"><c:out value="${dish.note}"/></span> 
<a href="dishUpdate?dishId=<c:out value="${dish.key.id}"/>" class="edit" style="display:none" id="dishEditLink"><fmt:message key="editLabel"/></a> 
<%-- Show 'All Reviews' link if there is specific review showing. --%> 
<c:choose>
  <c:when test="${reviewId != null}">
    <a class="add" href="#" onclick="window.location='dish?dishId=<c:out value="${dish.key.id}"/>';return false;"><fmt:message key="allReviewsLabel"/></a>
  </c:when>
</c:choose>
<%-- Data --%>
<progress id="waitingForData" title="<fmt:message key="waitingForDataLabel"/>"><fmt:message key="waitingForDataLabel"/></progress>
<div class="data" id="data"></div>
<progress id="moreIndicator" style="display:none" title="<fmt:message key="loadingMoreLabel"/>"><fmt:message key="loadingMoreLabel"/></progress>
</section>

<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</fmt:bundle>
</body>
</html>