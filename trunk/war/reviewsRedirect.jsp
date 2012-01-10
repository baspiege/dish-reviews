<%@ page language="java"%>
<% 
    response.sendRedirect("dish.jsp?dishId=" + request.getAttribute("dishId"));
%>