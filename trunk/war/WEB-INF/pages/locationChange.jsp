<%-- This JSP has the HTML for location page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStartAppCache.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="changeLocationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>
<p>
  <input type="radio" name="location" id="useGeoLocation" value="useGeoLocation"/><label for="useGeoLocation"><fmt:message key="currentLocationLabel"/></label>
</p>
<p>
  <input type="radio" name="location" id="useOverride" value="useOverride"/> <input id="address" value=""></input>
</p>
<section id="mapCanvas"></section>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
<script type="text/javascript" src="/js/locationChange.js" ></script>
</html>