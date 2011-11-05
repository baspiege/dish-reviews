<%-- This JSP has the HTML for dish update page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="geonotes.data.DishDelete" %>
<%@ page import="geonotes.data.DishGetSingle" %>
<%@ page import="geonotes.data.DishUpdate" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!=null;

    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",true);

    Dish dish=null;
    if (dishId!=null) {
        new DishGetSingle().execute(request);
        // If note is null, forward to main page
        dish=(Dish)request.getAttribute("dish");
        if (dish==null) {
        
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/dishesRedirect.jsp"/>
            <%
        } else {
            if (!isSignedIn) {
            
                %>
                <jsp:forward page="/dishesRedirect.jsp"/>
                <%
            }
            request.setAttribute("user",request.getUserPrincipal().getName());
            request.setAttribute("storeId",dish.storeId);
        }
    } else {
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/dishesRedirect.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals(bundle.getString("updateLabel"))) {		
            // Fields
            RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
            if (!RequestUtils.hasEdits(request)) {
                new DishUpdate().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/dishesRedirect.jsp"/>
                <%
            }
        } else if (action.equals(bundle.getString("deleteLabel"))) {		
            if (!RequestUtils.hasEdits(request)) {
                new DishDelete().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/dishesRedirect.jsp"/>
                <%
            }
        }
    }

    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMM dd h:mm aa zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="dish" method="post" action="dishUpdate.jsp" autocomplete="off">
<table>
<tr><td><%=bundle.getString("nameLabel")%>:</td><td><input type="text" name="note" value="<%=HtmlUtils.escapeChars(dish.note)%>" id="note" title="<%=bundle.getString("nameLabel")%>" maxlength="500"/></td></tr>
<tr><td><%=bundle.getString("lastUpdatedLabel")%>:</td><td><%=dateFormat.format(dish.lastUpdateTime)%></td></tr>
</table>
<div style="margin-top:1.5em">
<input type="hidden" name="dishId" value="<%=new Long(dish.getKey().getId()).toString()%>"/>
<%-- Update --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("updateLabel")%>"/>
<%-- Delete --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("deleteLabel")%>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>