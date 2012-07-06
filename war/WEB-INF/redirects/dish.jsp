<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page isELIgnored="false" %>
<c:redirect url="/dish">
  <c:param name="dishId" value="${dishId}"/>
  <c:param name="reload" value="true"/>
</c:redirect>