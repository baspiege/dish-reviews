<%-- This JSP has the HTML for store page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><c:out value="${store.note}"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/store.js" ></script>
<script type="text/javascript">
var storeId=<c:out value="${store.key.id}"/>;
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
<fmt:bundle basename="Text">
<body onload="getDishesData();">

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores">Main</a></li>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>

<%-- Store name --%>
<div class="section">
<%-- If logged in, link to edit page. --%> 
<c:choose>
  <c:when test="${pageContext.request.userPrincipal.name != null}">
    <a href="storeUpdate?storeId=<c:out value="${store.key.id}"/>"><c:out value="${store.note}"/></a>
  </c:when>
  <c:otherwise>
    <c:out value="${store.note}"/>
  </c:otherwise>
</c:choose>
<a href="storeUpdateLocation?storeId=<c:out value="${store.key.id}"/>" class="edit"><fmt:message key="locationLabel"/></a>
</div>

<%-- Data --%>
<div class="data" id="data">
<p><fmt:message key="waitingForDataLabel"/></p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</fmt:bundle>
</body>
</html>