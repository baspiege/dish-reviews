<%-- This JSP has the HTML for the data update task. --%>
<%@ page language="java"%>
<%@ page import="geonotes.data.DishesUpdateAllUtil" %>
<%@ page import="geonotes.data.ReviewsUpdateAllUtil" %>
<%
    //new DishesUpdateAllUtil().execute(request);
    //new ReviewsUpdateAllUtil().execute(request);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title> Data update </title>
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<p> Data update. </p>
</body>
</html>