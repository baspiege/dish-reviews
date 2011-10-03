<%@ page language="java"%>
<% 
    response.sendRedirect("reviews.jsp?dishId=" + request.getAttribute("dishId"));
%>