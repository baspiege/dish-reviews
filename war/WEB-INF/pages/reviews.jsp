<%-- This JSP has the HTML for reviews page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.DishGetSingle" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.ReviewsGetAll" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.DisplayUtils" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;  

    List<Review> reviews = null;
    
    Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",false);
    
    Dish dish=null;
    if (dishId!=null) {
        new ReviewsGetAll().execute(request);
        reviews=(List<Review>)request.getAttribute("reviews");
        
        new DishGetSingle().execute(request);
        dish=(Dish)request.getAttribute("dish");
        
        request.setAttribute("id",dish.storeId);    
        new StoreGetSingle().execute(request);
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
</head>
<body>
<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores.jsp">Main</a></li>
<%
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

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>

<%-- Data --%>
<div style="margin-top:1.5em" class="data">

<table id="reviews">
<caption>
<%= HtmlUtils.escapeChars(dish.note) %>
</caption>
<tr>

<th><a href="#" onclick="reorderReviewsByNameAscending();return false;">Reviews</a>

<%-- Add Button --%>
<% if (isSignedIn) { %>
 <a class="add addTh" href='reviewAdd.jsp?dishId=<%=dishId.toString()%>'><%=bundle.getString("addLabel")%></a>
<% } %>

<th><a href="#" onclick="reorderReviewsByTimeDescending();return false;">Time Ago</a></th>
<th><a href="#" onclick="reorderReviewsByVoteYesDescending();return false;">Agree</a></th>

</th><th>Image</th></tr>
<%
    long currentTime=new Date().getTime()/1000;
    if (reviews!=null && reviews.size()>0) {
        for (Review review:reviews) {
            long reviewId=review.getKey().getId();
            
            out.write("<tr");
            out.write(" id=\"" + reviewId + "\"");            
            out.write(" name=\"" + HtmlUtils.escapeChars(review.note).toLowerCase() + "\"");            
            out.write(" yes=\"" + review.yes + "\"");
            out.write(" time=\"" + review.lastUpdateTime.getTime() + "\"");
            out.write(">");
            
            // Note
            out.write("<td>");
            if (isSignedIn && request.getUserPrincipal().getName().equalsIgnoreCase(review.user)) {
                out.write("<a href=\"reviewUpdate.jsp?&reviewId=" + reviewId + "\">" + HtmlUtils.escapeChars(review.note) + "</a>");
            } else {
                out.write(HtmlUtils.escapeChars(review.note));
            }
            out.write("</td>");
            
            // Time Ago
            out.write("<td>" + DisplayUtils.displayElapsedTime(review.lastUpdateTime.getTime()/1000,currentTime) + "</td>");
            
            // Like
            out.write("<td>");
            if (isSignedIn) {
                out.write("<button id=\"button" + reviewId + "\" onclick=\"sendYesVote(" + reviewId + ")\">" + review.yes +  "</button></td>");
            } else {
                out.write(new Long(review.yes).toString());
            }
            out.write("</td>");
            
            // Image
            out.write("<td>");
            if (review.imageThumbnail==null) {
                out.write("<a class=\"add\" href=\"reviewImage.jsp?reviewId=" + reviewId + "\">" + bundle.getString("addLabel") + "</a>");
            } else {
                out.write("<a href=\"reviewImage.jsp?reviewId=" + reviewId + "\">");
                out.write("<img src=\"reviewThumbNailImage?reviewId=" + reviewId + "\">");
                out.write("</a>");
            }
            out.write("</td>");
            
            out.write("</tr>");
        }
    }
%>
</table>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/reviews.js" ></script>
</body>
</html>