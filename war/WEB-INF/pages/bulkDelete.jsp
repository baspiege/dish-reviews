<%-- This JSP has the HTML for bulk delete page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.GeoNoteBulkDelete" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");

    // Process based on action
    if (!StringUtils.isEmpty(action)) {
        if (action.equals(bundle.getString("deleteLabel"))) {		
             new GeoNoteBulkDelete().execute(request);
        } else {
            RequestUtils.resetAction(request);
            RequestUtils.removeEdits(request);
            %>
            <jsp:forward page="/geoNotesRedirect.jsp"/>
            <%
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title>Bulk Delete</title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<form method="post" action="bulkDelete.jsp" autocomplete="off">
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<div style="margin-top:1.5em">
<input class="button" type="submit" name="action" value="<%=bundle.getString("deleteLabel")%>"/>
</div>
</form>
</body>
</html>