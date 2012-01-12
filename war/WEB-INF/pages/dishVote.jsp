<%-- This JSP has the HTML for dish update page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.DishGetSingle" %>
<%@ page import="geonotes.data.DishUpdateYesNo" %>
<%@ page import="geonotes.data.DishUpdateUndoYesNo" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal().getName()!=null;

    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);
    RequestUtils.getAlphaInput(request,"vote","vote",false);

    Dish dish=null;
    if (dishId!=null) {
        new DishGetSingle().execute(request);
        // If note is null, forward to main page
        dish=(Dish)request.getAttribute("dish");
        if (dish==null) {
        
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/stores.jsp"/>
            <%
        } else {
            if (!isSignedIn) {
            
                %>
                <jsp:forward page="/stores.jsp"/>
                <%
            }
            request.setAttribute("user",request.getUserPrincipal().getName());
            request.setAttribute("storeId",dish.storeId);
        }
    } else {
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/stores.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals(bundle.getString("likeLabel"))) {
            if (!RequestUtils.hasEdits(request)) {
                new DishUpdateYesNo().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/storeRedirect.jsp"/>
                <%
            }
        } else if (action.equals(bundle.getString("unlikeLabel"))) {		
            if (!RequestUtils.hasEdits(request)) {
                new DishUpdateUndoYesNo().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/storeRedirect.jsp"/>
                <%
            }
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="dish" method="post" action="dishVote.jsp" autocomplete="off">
<input type="hidden" name="dishId" value="<%=new Long(dish.getKey().getId()).toString()%>"/>
<input type="hidden" name="vote" value="yes"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("likeLabel")%>"/>
<input class="button" type="submit" name="action" value="<%=bundle.getString("unlikeLabel")%>"/>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>