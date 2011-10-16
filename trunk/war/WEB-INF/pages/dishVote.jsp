<%-- This JSP votes for a dish. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="geonotes.data.DishUpdateYesNo" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!= null;
    if (isSignedIn) {
        request.setAttribute("user",request.getUserPrincipal().getName());

        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        RequestUtils.getAlphaInput(request,"vote","vote",true);

        if (dishId!=null)
        {
            new DishUpdateYesNo().execute(request);
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>