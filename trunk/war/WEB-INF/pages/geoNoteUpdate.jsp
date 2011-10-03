<%-- This JSP has the HTML for Geo Note page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="geonotes.data.GeoNoteDelete" %>
<%@ page import="geonotes.data.GeoNoteGetSingle" %>
<%@ page import="geonotes.data.GeoNoteUpdate" %>
<%@ page import="geonotes.data.model.GeoNote" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!=null;

    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    Long geoNoteId=RequestUtils.getNumericInput(request,"id","id",true);

    GeoNote geoNote=null;
    if (geoNoteId!=null) {
        new GeoNoteGetSingle().execute(request);
        // If note is null, forward to main page
        geoNote=(GeoNote)request.getAttribute("geoNote");
        if (geoNote==null) {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/geoNotesRedirect.jsp"/>
            <%
        } else {
            // Can only edit own note
            if (isSignedIn) {
                isSignedIn=request.getUserPrincipal().getName().equalsIgnoreCase(geoNote.user);
            }
            if (!isSignedIn) {
                %>
                <jsp:forward page="/geoNotesRedirect.jsp"/>
                <%
            }
        
            request.setAttribute("type", geoNote.type);
        }
    } else {
        RequestUtils.resetAction(request);
        RequestUtils.removeEdits(request);
        %>
        <jsp:forward page="/geoNotesRedirect.jsp"/>
        <%
    }

    // Process based on action
    if (!StringUtils.isEmpty(action) && isSignedIn) {
        if (action.equals(bundle.getString("updateLabel"))) {		
            // Fields
            RequestUtils.getAlphaInput(request,"note",bundle.getString("noteLabel"),false);
            RequestUtils.getNumericInput(request,"type",bundle.getString("typeLabel"),true);		
            if (!RequestUtils.hasEdits(request)) {
                new GeoNoteUpdate().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/geoNotesRedirect.jsp"/>
                <%
            }
        } else if (action.equals(bundle.getString("deleteLabel"))) {		
            if (!RequestUtils.hasEdits(request)) {
                new GeoNoteDelete().execute(request);
            }
            if (!RequestUtils.hasEdits(request)) {
                %>
                <jsp:forward page="/geoNotesRedirect.jsp"/>
                <%
            }
        }
    }

    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMM dd h:mm aa zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("geoNoteLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<form id="geoNote" method="post" action="geoNoteUpdate.jsp" autocomplete="off">
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<table>
<tr><td><%=bundle.getString("typeLabel")%>:</td><td><jsp:include page="/WEB-INF/pages/components/selectType.jsp"/></td></tr>
<tr><td><%=bundle.getString("noteLabel")%>:</td><td><input type="text" name="note" value="<%=HtmlUtils.escapeChars(geoNote.note)%>" id="note" title="<%=bundle.getString("noteLabel")%>" maxlength="500"/></td></tr>
<tr><td><%=bundle.getString("lastUpdatedLabel")%>:</td><td><%=dateFormat.format(geoNote.lastUpdateTime)%></td></tr>
</table>
<div style="margin-top:1.5em">
<input type="hidden" name="id" value="<%=new Long(geoNote.getKey().getId()).toString()%>"/>
<%-- Update --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("updateLabel")%>"/>
<%-- Delete --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("deleteLabel")%>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>