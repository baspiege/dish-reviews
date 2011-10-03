<%@ page language="java"%>
<% 
    response.sendRedirect("dishes.jsp?storeId=" + request.getAttribute("storeId"));
%>