<%-- This JSP has the HTML for review image page. --%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<fmt:bundle basename="Text">
<title><fmt:message key="imageLabel"/></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<style>
form {margin: 0px 0px 0px 0px; display: inline;}
</style>
</head>
<body>
<%-- Image --%>
<c:if test="${review!=null && review.image!=null}">
  <img src="reviewImage?reviewId=<c:out value="${review.key.id}"/>" alt="<fmt:message key="altPictureLabel"/>"/> <br/>
</c:if>
  
<c:if test="${ pageContext.request.userPrincipal.name!=null && pageContext.request.userPrincipal.name==review.user }">
  <form method="post" enctype="multipart/form-data" action="reviewImageUpdate?action=Upload&reviewId=<c:out value="${review.key.id}"/>">
  <input style="margin-bottom:1.5em" type="file" name="imageFile">
  <br/>
  <%-- Upload --%>
  <input class="button" type="submit" name="action" value="<fmt:message key="uploadLabel"/>">
  </form>
  <form method="post" action="reviewImageUpdate?reviewId=<c:out value="${review.key.id}"/>" autocomplete="off">
  <%-- Remove --%>
  <input type="submit" name="action" value="<fmt:message key="removeLabel"/>">
  </form>
</c:if>

<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</fmt:bundle>
</html>