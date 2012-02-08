<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for reviews table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%
    List<Review> reviews=(List<Review>)request.getAttribute("reviews");
    String user="";
    if (request.getUserPrincipal()!=null) {
        user=request.getUserPrincipal().getName();
    }
%>
<reviews>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%
    if (reviews!=null && reviews.size()>0) {
        for (Review review:reviews) {
            long reviewId=review.getKey().getId();
            // Add attributes
            out.write("<review");
            out.write(" reviewId=\"" + reviewId + "\"");
            out.write(" yes=\"" + review.getYesVote() + "\"");
            out.write(" time=\"" + review.getLastUpdateTime().getTime()/1000 + "\"");
            out.write(" text=\"" + HtmlUtils.escapeChars(review.getNote()) + "\"");
            out.write(" userId=\"" + review.getUser() + "\"");

            // User
            if (user!=null && user.equalsIgnoreCase(review.getUser())) {
                out.write(" user=\"true\"");
            } else {
                out.write(" user=\"false\"");
            }

            // Thumbnail
            if (review.getImageThumbnail()!=null) {
                out.write(" img=\"true\"");
            } else {
                out.write(" img=\"false\"");
            }
            out.write("/>");
        }
    }
%>
</reviews>