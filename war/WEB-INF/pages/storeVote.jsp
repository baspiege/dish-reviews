<%-- This JSP votes for the store. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="geonotes.data.StoreUpdateYesNo" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    Long storeId=RequestUtils.getNumericInput(request,"id","id",true);
    RequestUtils.getAlphaInput(request,"vote","vote",true);
    if (storeId!=null)
    {
        new StoreUpdateYesNo().execute(request);
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>