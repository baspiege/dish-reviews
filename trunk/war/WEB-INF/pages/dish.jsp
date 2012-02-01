<%-- This JSP has the HTML for reviews page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.JSUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean isSignedIn=request.getUserPrincipal().getName()!= null;
    Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);    
    Store store=(Store)request.getAttribute(RequestUtils.STORE);
    Long dishId=dish.getKey().getId();
    Long reviewId=(Long)request.getAttribute("reviewId");    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%= HtmlUtils.escapeChars(dish.note) %></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/dish.js" ></script>
<script type="text/javascript">
var dishId=<%=dishId%>;
var dishName="<%= JSUtils.escapeChars(dish.note) %>";
var storeName="<%= JSUtils.escapeChars(store.note) %>";
var reviewId=<%=reviewId%>;
var isLoggedIn=<%=isSignedIn%>;
</script>
</head>

<% if (reviewId!=null) { %>
<body onload="getReviewsDataById();">
<% } else { %>
<body onload="getReviewsData();">
<% } %>

<%-- Facebook login --%>
<div id="fb-root"></div>
<script type="text/javascript" src="/js/fblogin.js" ></script>

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores">Main</a></li>
<li><a href="store?storeId=<%=store.getKey().getId()%>"><%=HtmlUtils.escapeChars(store.note)%></a></li>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<div style="margin-top:1.5em">
<% if (isSignedIn) { %>
  <a href="dishUpdate?dishId=<%=dishId%>"><%= HtmlUtils.escapeChars(dish.note) %></a>
<% } else { %>
  <%= HtmlUtils.escapeChars(dish.note) %>
<% } %>

<% if (reviewId!=null) { %>
  <a class="add" href="#" onclick="window.location='dish?dishId=<%=dishId%>';return false;">All reviews</a>
<% } %>
</div>

<%-- Data --%>
<div class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>