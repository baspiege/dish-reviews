<%-- This JSP has the HTML for stores page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.DishAdd" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%@ page import="geonotes.utils.StringUtils" %>
<%
    // Check if signed in
    boolean isSignedIn=request.getUserPrincipal().getName()!= null;
    if (!isSignedIn) {
        pageContext.forward("/storesRedirect.jsp");
    }
    
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");

    // Fields
    String note="";
    //String type="";
    Long storeId=RequestUtils.getNumericInput(request,"storeId",bundle.getString("storeId"),true);

    // Process based on action
    if (!StringUtils.isEmpty(action)) {
        if (action.equals(bundle.getString("addLabel"))) {		
            // Get fields
            note=RequestUtils.getAlphaInput(request,"note",bundle.getString("nameLabel"),true);
            //RequestUtils.getNumericInput(request,"type",bundle.getString("typeLabel"),true);		
            if (!RequestUtils.hasEdits(request)) {
                new DishAdd().execute(request);
                Dish dish=(Dish)request.getAttribute("dish");
                request.setAttribute("dishId",dish.getKey().getId());
                pageContext.forward("/dishRedirect.jsp");
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
<%-- Fields --%>
<form id="store" method="post" action="dishAdd.jsp" autocomplete="off">
<table>
<tr><td><%=bundle.getString("nameLabel")%>:</td><td><input type="text" name="note" value="<%=note%>" id="note" title="<%=bundle.getString("nameLabel")%>" maxlength="500"/></td></tr>
</table>
<p>
<%-- Add --%>
<input id="storeId" type="hidden" name="storeId" value="<%=storeId%>" />
<input class="button" type="submit" style="display:none" id="addButtonDisabled" disabled="disabled" value="<%=bundle.getString("addLabel")%>"/>
<input class="button" type="submit" style="display:inline" id="addButtonEnabled" name="action" onclick="this.style.display='none';document.getElementById('addButtonDisabled').style.display='inline';" value="<%=bundle.getString("addLabel")%>"/>
</p>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>