<%-- This JSP has the HTML for Geo Note search page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.GeoNoteGetSingle" %>
<%@ page import="geonotes.data.model.GeoNote" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean found=false; 
    GeoNote geoNote=null;
    Long geoNoteId=RequestUtils.getNumericInput(request,"id","id",false);
    if (geoNoteId==null) {
        geoNoteId=new Long(0l);
    }
    
    // Process based on action
    if (!StringUtils.isEmpty(action) && !RequestUtils.hasEdits(request)) {
        if (action.equals(bundle.getString("searchLabel"))) {		
        
            // Always not found if less than 1.
            if (geoNoteId<1) {
                found=false;
            } else if (geoNoteId!=null) {
                new GeoNoteGetSingle().execute(request);
                geoNote=(GeoNote)request.getAttribute("geoNote");
                if (geoNote!=null) {
                    found=true;
                } else {
                    found=false;
                }
            }
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("searchLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
</head>
<% if (found) { %>
<body onload='getGeoNotesDataById(<%=new Long(geoNote.getKey().getId()).toString()%>);'>
<% } else { %>
<body>
<% } %>

<form id="geoNote" method="get" action="geoNoteSearch.jsp">
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<table>
<tr><td><%=bundle.getString("idLabel")%>:</td><td><input type="text" name="id" value="<%=geoNoteId.toString()%>" title="<%=bundle.getString("idLabel")%>"/></td></tr>
</table>

<% if (!StringUtils.isEmpty(action) && !found) { %>
<p><%=bundle.getString("idNotFoundEdit")%></p>
<% } %>

<div style="margin-top:1.5em">
<%-- Main --%>
<input class="button" type="button" name="action" value="<%=bundle.getString("mainLabel")%>" onclick="window.location='geoNotes.jsp';return false;"/>
<%-- Search --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("searchLabel")%>"/>
</div>
</form>

<% if (found) { %>
<div id="geoNotesDiv" style="margin-top:1.5em">
</div>
<% } %>

<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
<script type="text/javascript" src="/js/geoNotes.js" ></script>
</body>
</html>