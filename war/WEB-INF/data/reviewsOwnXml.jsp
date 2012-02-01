<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for reviews own table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    List<Review> reviews=(List<Review>)request.getAttribute("reviews");    
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
            out.write(" yes=\"" + review.yes + "\""); 
            out.write(" text=\"" + HtmlUtils.escapeChars(review.note) + "\"");
            out.write(" time=\"" + review.lastUpdateTime.getTime()/1000 + "\"");
            
            // Dish attributes
            Dish dish=RequestUtils.getDish(review.dishId);
            out.write(" dishId=\"" + dish.getKey().getId() + "\"");
            out.write(" dishText=\"" + HtmlUtils.escapeChars(dish.note) + "\"");
            
            // Store attributes
            Store store=RequestUtils.getStore(dish.storeId);
            out.write(" storeId=\"" + store.getKey().getId() + "\"");
            out.write(" storeText=\"" + HtmlUtils.escapeChars(store.note) + "\"");
            
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