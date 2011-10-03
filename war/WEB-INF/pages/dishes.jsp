<%-- This JSP has the HTML for dishes page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.DishesGetAll" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;  

    List<Dish> dishes = null;
    
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
    if (storeId!=null) {
        new DishesGetAll().execute(request);
        dishes=(List<Dish>)request.getAttribute("dishes");
    } else {
        // Forward to main page
    }    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/header.jsp"/>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<!--
<div style="margin-top:1.5em">

</div>
-->

<%-- Data --%>
<div style="margin-top:1.5em" id="dishes">
<table>
<tr><th>Dish

<%-- Add Button --%>
<% if (isSignedIn) { %>

 <a class="add addTh" href='dishAdd.jsp?storeId=<%=storeId.toString()%>'><%=bundle.getString("addLabel")%></a>

<% } %>


</th><th>Reviews</th></tr>
<%
    if (dishes!=null && dishes.size()>0) {
        for (Dish dish:dishes) {
            long dishId=dish.getKey().getId();
            
            // Add link to reviews...
            
            // Add attributes
            out.write("<tr>");
            out.write("<td>" + HtmlUtils.escapeChars(dish.note) + "</td>");
            out.write("<td><a href=\"reviews.jsp?storeId=" + storeId + "&dishId=" + dishId + "\">" + 22 + "</a></td>");
            out.write("</tr>");
        }
    }
%>
</table>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>