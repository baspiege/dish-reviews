<%-- This JSP has the HTML for adjust location page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="adjustLocationLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>
<body>
<input id="address" value=""></input>
<section>
<form id="store" method="get" action="storeAdd" autocomplete="off">
<%-- Update --%>
<input id="latitude" type="hidden" name="latitude" value="" />
<input id="longitude" type="hidden" name="longitude" value="" />
<input id="submitLocation" class="button" type="submit" name="action" value="<fmt:message key="nextLabel"/>"/>
</form>
</section>
<section id="mapCanvas"></section>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
<script type="text/javascript" src="/js/storeAddLocation.js" ></script>
</html>