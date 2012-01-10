<%@ page language="java"%>
<% 
    response.sendRedirect("store.jsp?storeId=" + request.getAttribute("storeId"));
%>