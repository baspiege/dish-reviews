<%-- This JSP has the HTML for review update page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="reviewLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="review" method="post" action="reviewUpdate" autocomplete="off">
<table>
<tr><td><fmt:message key="noteLabel"/>:</td><td><input type="text" name="note" value="<c:out value="${review.note}"/>" id="note" title="<fmt:message key="noteLabel"/>" maxlength="500"/></td></tr>
<tr><td><fmt:message key="lastUpdatedLabel"/>:</td><td><fmt:formatDate pattern="yyyy MMM dd h:mm aa zzz" timeZone="America/Chicago" value="${review.lastUpdateTime}"/></td></tr>
</table>
<div class="section">
<input type="hidden" name="reviewId" value="<c:out value="${review.key.id}"/>"/>
<input class="button" type="submit" name="action" value="<fmt:message key="updateLabel"/>"/>
<input class="button" type="submit" name="action" value="<fmt:message key="deleteLabel"/>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>