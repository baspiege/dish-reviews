<%-- This JSP has the HTML for 'my reviews' page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean isSignedIn=request.getUserPrincipal().getName()!= null;    
    if (!isSignedIn) {
        %>
        <jsp:forward page="/storesRedirect.jsp"/>
        <%    
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("myReviewsLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body onload="getReviewsData();">

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores.jsp">Main</a></li>

<li>
<fb:login-button autologoutlink="true"></fb:login-button>
</li>

<li>
<fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name>
</li>

<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Data --%>
<div style="margin-top:1.5em" class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/reviewsOwn.js" ></script>
</body>
</html>