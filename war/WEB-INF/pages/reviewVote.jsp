<%-- This JSP has the HTML for review vote page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="reviewLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="review" method="post" action="reviewVote" autocomplete="off">
<input type="hidden" name="reviewId" value="<c:out value="${review.key.id}"/>"/>
<input type="hidden" name="vote" value="yes"/>
<input class="button" type="submit" name="action" value="<fmt:message key="agreeLabel"/>"/>
<input class="button" type="submit" name="action" value="<fmt:message key="removeAgreeLabel"/>"/>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>