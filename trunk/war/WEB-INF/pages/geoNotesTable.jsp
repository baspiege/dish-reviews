<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for Geo Notes table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.GeoNoteGetAll" %>
<%@ page import="geonotes.data.GeoNoteGetSingle" %>
<%@ page import="geonotes.data.model.GeoNote" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    List<GeoNote> geoNotes = null;
    
    Long geoNoteId=RequestUtils.getNumericInput(request,"id","id",false);
    if (geoNoteId!=null) {
       new GeoNoteGetSingle().execute(request);
       // Add to list.
       geoNotes=new ArrayList<GeoNote>();
       geoNotes.add((GeoNote)request.getAttribute("geoNote"));
    } else { 
        RequestUtils.getNumericInputAsDouble(request,"latitude",bundle.getString("latitudeLabel"),true);
        RequestUtils.getNumericInputAsDouble(request,"longitude",bundle.getString("longitudeLabel"),true);
        new GeoNoteGetAll().execute(request);
        geoNotes=(List<GeoNote>)request.getAttribute("geoNotes");
    }
    
    String user=null;
    if (request.getUserPrincipal()!=null) {
        user=request.getUserPrincipal().getName();
    }
%>
<geoNotes>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%
    if (geoNotes!=null && geoNotes.size()>0) {
        for (GeoNote geoNote:geoNotes) {
            long geoId=geoNote.getKey().getId();
            // Add attributes
            out.write("<geoNote");
            out.write(" id=\"" + geoId + "\"");
            out.write(" lat=\"" + geoNote.latitude + "\"");
            out.write(" lon=\"" + geoNote.longitude + "\"");
            out.write(" yes=\"" + geoNote.yes + "\""); 
            out.write(" text=\"" + HtmlUtils.escapeChars(geoNote.note) + "\"");
            //out.write(" type=\"" + bundle.getString("type_"+geoNote.type) + "\"");
            
            out.write(" dishCount=\"" + 11 + "\"");
            
            // User
            if (user!=null && user.equalsIgnoreCase(geoNote.user)) {
                out.write(" user=\"true\"");
            } else {
                out.write(" user=\"false\"");
            }
            
            // Thumbnail
            if (geoNote.imageThumbnail!=null) {
                out.write(" img=\"true\"");
            } else {
                out.write(" img=\"false\"");
            }
            out.write(" time=\"" + geoNote.lastUpdateTime.getTime()/1000 + "\"/>");
        }
    }
%>
</geoNotes>