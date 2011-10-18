<%-- This JSP has the HTML for review update page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="geonotes.data.ReviewDelete" %>
<%@ page import="geonotes.data.ReviewGetSingle" %>
<%@ page import="geonotes.data.ReviewUpdate" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!=null;

    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",true);

    Review review=null;
    if (reviewId!=null) {
        new ReviewGetSingle().execute(request);
        // If note is null, forward to main page
        review=(Review)request.getAttribute("review");
        if (review==null) {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/reviewsRedirect.jsp"/>
            <%
        } else {
            // Can only edit own note
            if (isSignedIn) {
                isSignedIn=request.getUserPrincipal().getName().equalsIgnoreCase(review.user);
            }
            if (!isSignedIn) {
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
            request.setAttribute("dishId",review.dishId);
        }
    } else {
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/reviewsRedirect.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals(bundle.getString("updateLabel"))) {		
            // Fields
            RequestUtils.getAlphaInput(request,"note",bundle.getString("noteLabel"),true);
            if (!RequestUtils.hasEdits(request)) {
                new ReviewUpdate().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
        } else if (action.equals(bundle.getString("deleteLabel"))) {		
            if (!RequestUtils.hasEdits(request)) {
                new ReviewDelete().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
        }
    }

    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMM dd h:mm aa zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("reviewLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="review" method="post" action="reviewUpdate.jsp" autocomplete="off">
<table>
<tr><td><%=bundle.getString("noteLabel")%>:</td><td><input type="text" name="note" value="<%=HtmlUtils.escapeChars(review.note)%>" id="note" title="<%=bundle.getString("noteLabel")%>" maxlength="500"/></td></tr>
<tr><td><%=bundle.getString("lastUpdatedLabel")%>:</td><td><%=dateFormat.format(review.lastUpdateTime)%></td></tr>
</table>
<div style="margin-top:1.5em">
<input type="hidden" name="reviewId" value="<%=new Long(review.getKey().getId()).toString()%>"/>
<%-- Update --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("updateLabel")%>"/>
<%-- Delete --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("deleteLabel")%>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>