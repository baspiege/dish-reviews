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
            long storeIdTemp=store.getKey().getId();
            // Add attributes
            out.write("<store");
            out.write(" storeId=\"" + storeIdTemp + "\"");
            out.write(" lat=\"" + store.latitude + "\"");
            out.write(" lon=\"" + store.longitude + "\"");
            out.write(" text=\"" + HtmlUtils.escapeChars(store.note) + "\"");
            out.write(" dishCount=\"" + store.dishCount + "\"");
            out.write("/>");
        }
    }
%>
</stores>