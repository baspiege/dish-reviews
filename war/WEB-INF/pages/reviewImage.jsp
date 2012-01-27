<%-- This JSP has the HTML for review image page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%> 
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
    boolean usersOwnReview=request.getUserPrincipal().getName().equalsIgnoreCase(review.user);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("imageLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<style>
form {margin: 0px 0px 0px 0px; display: inline;}
</style>
</head>
<body>
<% if (review!=null && review.image!=null) { %>
<img src="reviewImage?reviewId=<%=review.getKey().getId()%>" alt="<%=bundle.getString("altPictureLabel")%>"/> <br/>
<% } %>
<% if (usersOwnReview) { %>
<form method="post" enctype="multipart/form-data" action="reviewImageUpdate?action=Upload&reviewId=<%=review.getKey().getId()%>"> 
<input style="margin-bottom:1.5em" type="file" name="imageFile">
<br/>
<%-- Upload --%>
<input class="button" type="submit" name="action" value="Upload">
</form>
<form method="post" action="reviewImageUpdate?reviewId=<%=review.getKey().getId()%>" autocomplete="off">
<%-- Remove --%>
<input type="submit" name="action" value="Remove">
</form>
<% } %>  
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>