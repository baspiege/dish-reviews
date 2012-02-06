<%-- This JSP has the HTML for store page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="adjustLocationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="store" method="post" action="storeUpdate" autocomplete="off">
<table>
<tr><td><fmt:message key="nameLabel"/>:</td><td><input type="text" name="note" value="<c:out value="${store.note}"/>" id="note" title="<fmt:message key="nameLabel"/>" maxlength="500"/></td></tr>
<tr><td><fmt:message key="lastUpdatedLabel"/>:</td><td><fmt:formatDate pattern="yyyy MMM dd h:mm aa zzz" timeZone="America/Chicago" value="${store.lastUpdateTime}" /></td></tr>
</table>
<div class="section">
<input type="hidden" name="storeId" value="<c:out value="${store.key.id}"/>"/>
<input class="button" type="submit" name="action" value="<fmt:message key="updateLabel"/>"/>
<input class="button" type="submit" name="action" value="<fmt:message key="deleteLabel"/>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>