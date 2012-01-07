<%-- This JSP has the HTML for review update page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.ReviewGetSingle" %>
<%@ page import="geonotes.data.ReviewUpdateYesNo" %>
<%@ page import="geonotes.data.ReviewUpdateUndoYesNo" %>
<%@ page import="geonotes.data.model.Review" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal().getName()!=null;

    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    Long reviewId=RequestUtils.getNumericInput(request,"reviewId","reviewId",true);
    RequestUtils.getAlphaInput(request,"vote","vote",false);

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
            if (!isSignedIn) {
            
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
            request.setAttribute("user",request.getUserPrincipal().getName());
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
        if (action.equals(bundle.getString("agreeLabel"))) {
            if (!RequestUtils.hasEdits(request)) {
                new ReviewUpdateYesNo().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
        } else if (action.equals(bundle.getString("removeAgreeLabel"))) {		
            if (!RequestUtils.hasEdits(request)) {
                new ReviewUpdateUndoYesNo().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("reviewLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="review" method="post" action="reviewVote.jsp" autocomplete="off">
<input type="hidden" name="reviewId" value="<%=new Long(review.getKey().getId()).toString()%>"/>
<input type="hidden" name="vote" value="yes"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("agreeLabel")%>"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("removeAgreeLabel")%>"/>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>