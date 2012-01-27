<%-- This JSP has the HTML for dish update page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Dish dish=(Dish)request.getAttribute(RequestUtils.DISH);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="dish" method="post" action="dishVote" autocomplete="off">
<input type="hidden" name="dishId" value="<%=dish.getKey().getId()%>"/>
<input type="hidden" name="vote" value="yes"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("likeLabel")%>"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("unlikeLabel")%>"/>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>