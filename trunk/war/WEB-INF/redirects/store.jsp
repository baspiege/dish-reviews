<%@ page language="java"%>
<% 
    response.sendRedirect("/store?storeId=" + request.getAttribute("storeId"));
%>