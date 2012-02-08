<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page isELIgnored="false" %>
<c:redirect url="/store">
  <c:param name="storeId" value="${storeId}"/>
</c:redirect>