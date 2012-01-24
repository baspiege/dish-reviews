<%@ page language="java"%>
<% 
    response.sendRedirect("dish?dishId=" + request.getAttribute("dishId"));
%>