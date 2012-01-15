<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for reviews own table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.ReviewsGetAll" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");    
    RequestUtils.getNumericInput(request,"dishId",bundle.getString("dishId"),true);
    RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
    
    new ReviewsGetAll().execute(request);
    List<Review> reviews=(List<Review>)request.getAttribute("reviews");
    
    String user=null;
    if (request.getUserPrincipal().getName()!=null) {
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