<%-- This JSP has the HTML for reviews page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.ReviewsGetAll" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;  

    List<Review> reviews = null;
    
    Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",false);
    
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
    
    if (dishId!=null) {
        new ReviewsGetAll().execute(request);
        reviews=(List<Review>)request.getAttribute("reviews");
    } else {
        // Forward to main page
    }    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("reviewsLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/header.jsp"/>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>

<%-- Data --%>
<div style="margin-top:1.5em" class="data">
<table>
<tr><th>Reviews

<%-- Add Button --%>
<% if (isSignedIn) { %>
 <a class="add addTh" href='reviewAdd.jsp?dishId=<%=dishId.toString()%>'><%=bundle.getString("addLabel")%></a>
<% } %>

</th><th>Image</th></tr>
<%
    if (reviews!=null && reviews.size()>0) {
        for (Review review:reviews) {
            long reviewId=review.getKey().getId();
            
            // Add link to reviews...
            
            // Add attributes
            out.write("<tr>");
            out.write("<td><a href=\"reviewUpdate.jsp?reviewId=" + reviewId + "\">" + HtmlUtils.escapeChars(review.note) + "</a></td>");
            out.write("<td>");
            
            if (review.imageThumbnail==null) {
                out.write("<a class=\"add\" href=\"reviewImage.jsp?reviewId=" + reviewId + "\">" + bundle.getString("addLabel") + "</a>");
            
            } else {
                out.write("<img src=\"reviewThumbNailImage?reviewId=" + reviewId + "\">");
            }
            
            out.write("</td>");
            out.write("</tr>");
        }
    }
%>
</table>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>