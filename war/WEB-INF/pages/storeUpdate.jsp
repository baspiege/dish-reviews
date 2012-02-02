<%-- This JSP has the HTML for store page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy MMM dd h:mm aa zzz");
    dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
    Store store=(Store)request.getAttribute(RequestUtils.STORE);
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("storeLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<form id="store" method="post" action="storeUpdate" autocomplete="off">
<table>
<tr><td><%=bundle.getString("nameLabel")%>:</td><td><input type="text" name="note" value="<%=HtmlUtils.escapeChars(store.note)%>" id="note" title="<%=bundle.getString("nameLabel")%>" maxlength="500"/></td></tr>
<tr><td><%=bundle.getString("lastUpdatedLabel")%>:</td><td><%=dateFormat.format(store.lastUpdateTime)%></td></tr>
</table>
<div style="margin-top:1.5em">
<input type="hidden" name="storeId" value="<%=store.getKey().getId()%>"/>
<%-- Update --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("updateLabel")%>"/>
<%-- Delete --%>
<input class="button" type="submit" name="action" value="<%=bundle.getString("deleteLabel")%>"/>
</div>
</form>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>