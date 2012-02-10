<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<fmt:bundle basename="Text">
<footer>
<jsp:useBean id="currentDate" class="java.util.Date" scope="page" />
<fmt:message key="copyrightLabel"/> <fmt:formatDate pattern="yyyy" value="${currentDate}"/> Brian Spiegel
</footer>
</fmt:bundle>