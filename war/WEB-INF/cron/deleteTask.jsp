<%-- This JSP has the HTML for the delete task. --%>
<%@ page language="java"%>
<%@ page import="geonotes.data.DeleteOldReviews" %>
<%
//    DeleteOldReviews().execute(request);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<title> Delete Task </title>
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<p> Delete task. </p>
</body>
</html>