<?xml version="1.0" encoding="UTF-8"?>
<%-- This JSP has the HTML for dishes table. --%>
<%@page pageEncoding="UTF-8" contentType="text/xml; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%
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
            out.write(" yes=\"" + dish.getYesVote() + "\"");
            out.write(" dishText=\"" + HtmlUtils.escapeChars(dish.getNote()) + "\"");
            out.write(" lastReviewText=\"" + HtmlUtils.escapeChars(dish.getLastReview()) + "\"");
            out.write(" lastReviewUserId=\"" + dish.getLastReviewUserId() + "\"");
            out.write(" lastReviewImageId=\"" + dish.getLastReviewImageId() + "\"");

            // Thumbnail
            if (dish.getLastReviewImageId()!=null && dish.getLastReviewImageId()!=0l) {
                out.write(" img=\"true\"");
            } else {
                out.write(" img=\"false\"");
            }
            out.write("/>");
        }
    }
%>
</dishes>