<%-- This JSP has the HTML for the data update task. --%>
<%@ page language="java"%>
<%@ page import="geonotes.data.DishesUpdateAllUtil" %>
<%@ page import="geonotes.data.ReviewsUpdateAllUtil" %>
<%
    // Clear mem cache
    com.google.appengine.api.memcache.MemcacheServiceFactory.getMemcacheService().clearAll();

    //new DishesUpdateAllUtil().execute(request);
    //new ReviewsUpdateAllUtil().execute(request);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<title> Data update </title>
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<p> Data update. </p>
</body>
</html>