<%-- This JSP has the HTML for dishes page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean isSignedIn=request.getUserPrincipal().getName()!= null; 
    
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
    Store store=null;
    if (storeId!=null) {
        store=RequestUtils.getStore(request,storeId);
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript">
var storeId=<%=storeId%>;
var isLoggedIn='<%=isSignedIn%>';
</script>
</head>
<body onload="getDishesData();">

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
<div style="margin-top:1.5em"><%= HtmlUtils.escapeChars(store.note) %></div>

<%-- Data --%>
<div class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/dishes.js" ></script>
</body>
</html>