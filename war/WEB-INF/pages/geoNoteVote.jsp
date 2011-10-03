<%-- This JSP votes for the geo note. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="geonotes.data.GeoNoteUpdateYesNo" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    Long geoNoteId=RequestUtils.getNumericInput(request,"id","id",true);
    RequestUtils.getAlphaInput(request,"vote","vote",true);
    if (geoNoteId!=null)
    {
        new GeoNoteUpdateYesNo().execute(request);
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>