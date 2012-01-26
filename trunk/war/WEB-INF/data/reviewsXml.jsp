<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for reviews table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%    
    List<Review> reviews=(List<Review>)request.getAttribute("reviews");
    String user=(String)request.getAttribute("user");
%>
<reviews>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%
    if (reviews!=null && reviews.size()>0) {
        for (Review review:reviews) {
            long reviewIdTemp=review.getKey().getId();
            // Add attributes
            out.write("<review");
            out.write(" reviewId=\"" + reviewIdTemp + "\"");
            out.write(" yes=\"" + review.yesVote + "\"");
            out.write(" time=\"" + review.lastUpdateTime.getTime()/1000 + "\"");
            out.write(" text=\"" + HtmlUtils.escapeChars(review.note) + "\"");
            out.write(" userId=\"" + review.user + "\"");
                        
            // User
            if (user!=null && user.equalsIgnoreCase(review.user)) {
                out.write(" user=\"true\"");
            } else {
                out.write(" user=\"false\"");
            }
            
            // Thumbnail
            if (review.imageThumbnail!=null) {
                out.write(" img=\"true\"");
            } else {
                out.write(" img=\"false\"");
            }
            out.write("/>");
        }
    }
%>
</reviews>