<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for dishes own table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.DishesGetAll" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");    
    RequestUtils.getNumericInput(request,"start",bundle.getString("startLabel"),true);
    RequestUtils.getNumericInput(request,"storeId",bundle.getString("storeId"),true);
    RequestUtils.getAlphaInput(request,"sortBy",bundle.getString("sortByLabel"),false);
    
    // Get data
    new DishesGetAll().execute(request);
    List<Dish> dishes=(List<Dish>)request.getAttribute("dishes");
%>
<dishes>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%
    if (dishes!=null && dishes.size()>0) {
        for (Dish dish:dishes) {
            long dishId=dish.getKey().getId();
            // Add attributes
            out.write("<dish");
            out.write(" dishId=\"" + dishId + "\"");
            out.write(" yes=\"" + dish.yesVote + "\""); 
            out.write(" dishText=\"" + HtmlUtils.escapeChars(dish.note) + "\"");
            out.write(" lastReviewText=\"" + HtmlUtils.escapeChars(dish.lastReview) + "\"");
            out.write(" lastReviewUserId=\"" + dish.lastReviewUserId + "\"");
            out.write(" lastReviewImageId=\"" + dish.lastReviewImageId + "\"");
            
            // Thumbnail
            if (dish.lastReviewImageId!=null && dish.lastReviewImageId!=0l) {
                out.write(" img=\"true\"");
            } else {
                out.write(" img=\"false\"");
            }
            out.write("/>");
        }
    }
%>
</dishes>