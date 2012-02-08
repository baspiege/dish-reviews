<%-- This JSP has the HTML for dish vote page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="dishLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="dish" method="post" action="dishVote" autocomplete="off">
<input type="hidden" name="dishId" value="<c:out value="${dish.key.id}"/>"/>
<input type="hidden" name="vote" value="yes"/>
<input class="button" type="submit" name="action" value="<fmt:message key="likeLabel"/>"/>
<input class="button" type="submit" name="action" value="<fmt:message key="unlikeLabel"/>"/>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>