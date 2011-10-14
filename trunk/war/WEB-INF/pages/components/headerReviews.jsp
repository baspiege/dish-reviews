<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores.jsp">Main</a></li>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;
    
    Store store=(Store)request.getAttribute("store");
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