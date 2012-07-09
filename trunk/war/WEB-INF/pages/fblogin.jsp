<%-- This JSP has the HTML for stores page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/htmlStart.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="logonLabel"/> / <fmt:message key="logoffLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>

<%-- Facebook login --%>
<div id="fb-root"></div>

<section id="fblogin">
  <fb:login-button autologoutlink="true"></fb:login-button>
  <fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name>
</section>

<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
<script type="text/javascript" src="/js/fblogin.js" ></script>
</html>