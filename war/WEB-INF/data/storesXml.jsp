<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for stores table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%
    List<Store> stores=(List<Store>)request.getAttribute("stores");
%>
<stores>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%
    if (stores!=null && stores.size()>0) {
        for (Store store:stores) {
            long storeId=store.getKey().getId();
            // Add attributes
            out.write("<store");
            out.write(" storeId=\"" + storeId + "\"");
            out.write(" lat=\"" + store.getLatitude() + "\"");
            out.write(" lon=\"" + store.getLongitude() + "\"");
            out.write(" text=\"" + HtmlUtils.escapeChars(store.getNote()) + "\"");
            out.write(" dishCount=\"" + store.getDishCount() + "\"");
            out.write("/>");
        }
    }
%>
</stores>