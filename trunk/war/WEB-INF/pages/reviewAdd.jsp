<%-- This JSP has the HTML for review add page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.ReviewAdd" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal()!= null;
    if (!isSignedIn) {
        %>
        <jsp:forward page="/geoNotesRedirect.jsp"/>
        <%    
    }
    
    request.setAttribute("user",request.getUserPrincipal().getName());
    
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");

    // Fields
    String note="";
    //String type="";
    Long dishId=RequestUtils.getNumericInput(request,"dishId",bundle.getString("dishId"),true);

    // Process based on action
    if (!RequestUtils.isForwarded(request) && !StringUtils.isEmpty(action)) {
        if (action.equals(bundle.getString("addLabel"))) {		
            // Get fields
            note=RequestUtils.getAlphaInput(request,"note",bundle.getString("noteLabel"),false);
            //RequestUtils.getNumericInput(request,"type",bundle.getString("typeLabel"),true);		
            if (!RequestUtils.hasEdits(request)) {
                new ReviewAdd().execute(request);
                RequestUtils.resetAction(request);
                %>
                <jsp:forward page="/reviewsRedirect.jsp"/>
                <%
            }
        }
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("geoNotesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Fields --%>
<form id="geoNote" method="post" action="reviewAdd.jsp" autocomplete="off">
<table>
<tr><td>Type:</td><td>
</td></tr>
<tr><td><%=bundle.getString("noteLabel")%>:</td><td><input type="text" name="note" value="<%=note%>" id="note" title="<%=bundle.getString("noteLabel")%>" maxlength="500"/></td></tr>
</table>
<p>
<%-- Cancel --%>
<input class="button" type="button" name="action" value="<%=bundle.getString("cancelLabel")%>" onclick="window.location='geoNotes.jsp';return false;"/>
<%-- Add --%>
<input id="storeId" type="hidden" name="dishId" value="<%=dishId%>" />
<input class="button" type="submit" style="display:none" id="addButtonDisabled" disabled="disabled" value="<%=bundle.getString("addLabel")%>"/>
<input class="button" type="submit" style="display:inline" id="addButtonEnabled" name="action" onclick="this.style.display='none';document.getElementById('addButtonDisabled').style.display='inline';" value="<%=bundle.getString("addLabel")%>"/>
</p>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>