<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP votes for a dish. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="geonotes.data.DishUpdateYesNo" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<result>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!= null;
    if (isSignedIn) {
        request.setAttribute("user",request.getUserPrincipal().getName());

        Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
        RequestUtils.getAlphaInput(request,"vote","vote",true);

        if (dishId!=null) {
            new DishUpdateYesNo().execute(request);
        }
        
        List edits=(List)request.getAttribute("edits");
        if (edits!=null && edits.size()>0) {
            String message="";
            for (int i=0;i<edits.size();i++) {
                message+=HtmlUtils.escapeChars((String)edits.get(i));
            }
            out.write("<error message=\"" + message + "\"/>");
        }
    }
%>
</result>