<%-- This JSP has the HTML for review vote page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Review review=(Review)request.getAttribute(RequestUtils.REVIEW);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("reviewLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="review" method="post" action="reviewVote" autocomplete="off">
<input type="hidden" name="reviewId" value="<%=review.getKey().getId()%>"/>
<input type="hidden" name="vote" value="yes"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("agreeLabel")%>"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("removeAgreeLabel")%>"/>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>