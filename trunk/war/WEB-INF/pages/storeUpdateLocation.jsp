<%-- This JSP has the HTML for update location page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="locationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>

<%-- Hidden fields for JavaScript --%>
<div style="display:none;">
  <span id="storeLatitude"><c:out value="${store.latitude}"/></span>
  <span id="storeLongitude"><c:out value="${store.longitude}"/></span>
</div>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<input id="address" value=""></input>
<section>
<c:choose>
  <c:when test="${pageContext.request.userPrincipal.name != null}">
    <%-- Update --%>
    <form id="store" method="post" action="storeUpdateLocation" autocomplete="off">
    <input id="latitude" type="hidden" name="latitude" value="" />
    <input id="longitude" type="hidden" name="longitude" value="" />
    <input type="hidden" name="storeId" value="<c:out value="${store.key.id}"/>"/>
    <input id="submitLocation" class="button" type="submit" name="action" value="<fmt:message key="updateLabel"/>"/>
    </form>
  </c:when>
</c:choose>
</section>
<section id="mapCanvas"></section>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
<script type="text/javascript" src="/js/storeUpdateLocation.js" ></script>
</html>