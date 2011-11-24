<%-- This JSP has the HTML for the dishes reset task. --%>
<%@ page language="java"%>
<%@ page import="geonotes.data.DishesUpdateAllUtil" %>
<%
    new DishesUpdateAllUtil().execute(request);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title> Dishes reset </title>
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<p> Dishes reset. </p>
</body>
</html>