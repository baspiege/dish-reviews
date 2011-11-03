<%-- This JSP has the HTML for store page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="geonotes.data.StoreDelete" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.StoreUpdate" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!=null;

    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",true);

    Store store=null;
    if (storeId!=null) {
        new StoreGetSingle().execute(request);
        // If note is null, forward to main page
        store=(Store)request.getAttribute("store");
        if (store==null) {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/storesRedirect.jsp"/>
            <%
        } else {
            // Can only edit own note
            if (isSignedIn) {
                isSignedIn=request.getUserPrincipal().getName().equalsIgnoreCase(store.user);
            }
            if (!isSignedIn) {
                %>
                <jsp:forward page="/storesRedirect.jsp"/>
                <%
            }
        }
    } else {
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/storesRedirect.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals(bundle.getString("updateLabel"))) {		
            // Fields
            RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
            if (!RequestUtils.hasEdits(request)) {
                new StoreUpdate().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/storesRedirect.jsp"/>
                <%
            }
        } else if (action.equals(bundle.getString("deleteLabel"))) {		
            if (!RequestUtils.hasEdits(request)) {
                new StoreDelete().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/storesRedirect.jsp"/>
                <%
            }
        }
    }

    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMM dd h:mm aa zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("storeLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="store" method="post" action="storeUpdate.jsp" autocomplete="off">
<table>
<tr><td><%=bundle.getString("nameLabel")%>:</td><td><input type="text" name="note" value="<%=HtmlUtils.escapeChars(store.note)%>" id="note" title="<%=bundle.getString("nameLabel")%>" maxlength="500"/></td></tr>
<tr><td><%=bundle.getString("lastUpdatedLabel")%>:</td><td><%=dateFormat.format(store.lastUpdateTime)%></td></tr>
</table>
<div style="margin-top:1.5em">
<input type="hidden" name="storeId" value="<%=new Long(store.getKey().getId()).toString()%>"/>
<%-- Update --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("updateLabel")%>"/>
<%-- Delete --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("deleteLabel")%>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>