<%-- This JSP has the HTML for dishes page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.DishesGetAll" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;

    List<Dish> dishes = null;
    Store store = null;
    
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
    if (storeId!=null) {
        new DishesGetAll().execute(request);
        dishes=(List<Dish>)request.getAttribute("dishes");
        
        // Get store
        request.setAttribute("id",storeId);
        new StoreGetSingle().execute(request);
        store=(Store)request.getAttribute("store");
    } else {
          %>
          <jsp:forward page="/storesRedirect.jsp"/>
          <%
    }    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores.jsp">Main</a></li>
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

<%-- Data --%>
<div style="margin-top:1.5em" class="data">
<table id="dishes">
<caption><%= HtmlUtils.escapeChars(store.note) %></caption>
<tr>

<th><a href="#" onclick="reorderDishesByNameAscending();return false;">Dish</a>
<%-- Add Button --%>
<% if (isSignedIn) { %>
 <a class="add addTh" href='dishAdd.jsp?storeId=<%=storeId.toString()%>'><%=bundle.getString("addLabel")%></a>
<% } %>
</th>

<th><a href="#" onclick="reorderDishesByVoteYesDescending();return false;">Like</a></th>
<th><a href="#" onclick="reorderDishesByReviewCountDescending();return false;">Reviews</a></th>
</tr>
<%
    if (dishes!=null && dishes.size()>0) {
        for (Dish dish:dishes) {
            long dishId=dish.getKey().getId();
            
            out.write("<tr ");
            out.write("id=\"" + dishId + "\"");            
            out.write("name=\"" + HtmlUtils.escapeChars(dish.note).toLowerCase() + "\"");            
            out.write("yes=\"" + dish.yes + "\""); 
            out.write("reviewCount=\"" + dish.reviewCount+ "\"");             
            out.write(">");
            
            // Note
            out.write("<td>");
            if (isSignedIn && request.getUserPrincipal().getName().equalsIgnoreCase(dish.user)) {
                out.write("<a href=\"dishUpdate.jsp?&dishId=" + dishId + "\">" + HtmlUtils.escapeChars(dish.note) + "</a>");
            } else {
                out.write(HtmlUtils.escapeChars(dish.note));
            }
            out.write("</td>");
            
            // Like
            out.write("<td>");
            if (isSignedIn) {
                out.write("<button onclick=\"sendYesVote(this," + dishId +")\">" + dish.yes +  "</button></td>");
            } else {
                out.write(new Long(dish.yes).toString());
            }
            out.write("</td>");
                        
            // Review count and link
            out.write("<td class=\"center\"><a href=\"reviews.jsp?storeId=" + storeId + "&dishId=" + dishId + "\">" + dish.reviewCount + "</a></td>");
            out.write("</tr>");
        }
    }
%>
</table>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/dishes.js" ></script>
</body>
</html>