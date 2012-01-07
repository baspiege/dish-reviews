<%-- This JSP has the HTML for reviews page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal().getName()!= null;
    
    Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",false);
    
    Dish dish=null;
    Store store=null;
    if (dishId!=null) {
        dish=RequestUtils.getDish(request,dishId);
        store=RequestUtils.getStore(request,dish.storeId);
    } else {
          %>
          <jsp:forward page="/storesRedirect.jsp"/>
          <%
    }    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("reviewsLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript">
var dishId=<%=dishId%>;
var isLoggedIn='<%=isSignedIn%>';
</script>
</head>
<body onload="getReviewsData();">
<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores.jsp">Main</a></li>
<%
    out.write("<li><a href=\"dishes.jsp?storeId=" + store.getKey().getId() + "\">" + HtmlUtils.escapeChars(store.note) + "</a></li>");
%>
<li>
<% if (!isSignedIn) { %>
<a href='<%=userService.createLoginURL("../stores.jsp")%>'><%=bundle.getString("logonLabel")%></a>
<% } else { %>
<a href='<%=userService.createLogoutURL("../stores.jsp")%>'><%=bundle.getString("logoffLabel")%></a>
<% } %>
</li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<div style="margin-top:1.5em"><%= HtmlUtils.escapeChars(dish.note) %></div>

<%-- Data --%>
<div class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/reviews.js" ></script>
</body>
</html>