<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for stores table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.StoreGetAll" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    List<Store> stores = null;
    
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
    if (storeId!=null) {
       new StoreGetSingle().execute(request);
       // Add to list.
       stores=new ArrayList<Store>();
       stores.add((Store)request.getAttribute("store"));
    } else { 
        RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
        RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
        new StoreGetAll().execute(request);
        stores=(List<Store>)request.getAttribute("stores");
    }
    
    String user=null;
    if (request.getUserPrincipal()!=null) {
        user=request.getUserPrincipal().getName();
    }
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
            out.write(" yes=\"" + store.yes + "\""); 
            out.write(" text=\"" + HtmlUtils.escapeChars(store.note) + "\"");
            out.write(" dishCount=\"" + store.dishCount + "\"");
            
            // User
            /*
            if (user!=null && user.equalsIgnoreCase(store.user)) {
                out.write(" user=\"true\"");
            } else {
                out.write(" user=\"false\"");
            }*/
            
            // Thumbnail
            if (store.imageThumbnail!=null) {
                out.write(" img=\"true\"");
            } else {
                out.write(" img=\"false\"");
            }
            out.write("/>");
        }
    }
%>
</stores>