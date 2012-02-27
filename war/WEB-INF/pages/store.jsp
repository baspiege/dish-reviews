<%-- This JSP has the HTML for store page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStartAppCache.jsp" %>
<title><c:out value="${store.note}"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/store.js" ></script>
<script type="text/javascript">
var storeId=<c:out value="${store.key.id}"/>;
var canEdit=false;
var isLoggedIn=false;
</script>
</head>
<body onload="setUpPage();setOnlineListeners();getDishesData();">
<fmt:bundle basename="Text">

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist">
<li><a href="stores"><fmt:message key="mainLabel"/></a></li>
<li id="fblogin" style="display:none"><fb:login-button autologoutlink="true"></fb:login-button></li>
<li id="fbname" style="display:none"><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<li id="offline" style="display:none"><fmt:message key="offlineLabel"/></li>
</ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>

<section>
<%-- Store name --%>
<c:out value="${store.note}"/> 
<a href="storeUpdate?storeId=<c:out value="${store.key.id}"/>" class="edit" style="display:none" id="storeEditLink"><fmt:message key="editLabel"/></a> 
<a href="storeUpdateLocation?storeId=<c:out value="${store.key.id}"/>" class="edit"><fmt:message key="locationLabel"/></a>
<%-- Data --%>
<p id="waitingForData"><fmt:message key="waitingForDataLabel"/></p>
<div class="data" id="data"></div>
<p id="moreIndicator" style="display:none"><fmt:message key="loadingMoreLabel"/></p>
</section>

<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>