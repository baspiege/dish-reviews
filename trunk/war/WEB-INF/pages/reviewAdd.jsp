<%-- This JSP has the HTML for review add page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    String action=RequestUtils.getAlphaInput(request,"action","Action",false);
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("reviewLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Fields --%>
<form id="store" method="post" action="reviewAdd" autocomplete="off">
<table>
<tr><td><%=bundle.getString("noteLabel")%>:</td><td><input type="text" name="note" value="<%=request.getAttribute("note")%>" id="note" title="<%=bundle.getString("noteLabel")%>" maxlength="500"/></td></tr>
</table>
<p>
<%-- Add --%>
<input class="button" type="submit" style="display:none" id="addButtonDisabled" disabled="disabled" value="<%=bundle.getString("addLabel")%>"/>
<input class="button" type="submit" style="display:inline" id="addButtonEnabled" name="action" onclick="this.style.display='none';document.getElementById('addButtonDisabled').style.display='inline';" value="<%=bundle.getString("addLabel")%>"/>
<input id="dishId" type="hidden" name="dishId" value="<%=request.getAttribute("dishId")%>" />
</p>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>