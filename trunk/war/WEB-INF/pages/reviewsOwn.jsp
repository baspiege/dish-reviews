<%-- This JSP has the HTML for 'my reviews' page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
%>
<title><%=bundle.getString("myReviewsLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/reviewsOwn.js" ></script>
</head>
<body onload="getReviewsData();">

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist">
<li><a href="stores">Main</a></li>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Data --%>
<div class="data section" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>