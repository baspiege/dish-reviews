<%-- This JSP has the HTML for instructions page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
%>
<title><%=bundle.getString("instructionsLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>
<body>
<h2><%=bundle.getString("overviewLabel")%></h2>
<p class="instructionDetails"><%=bundle.getString("overviewDetails")%></p>
<h2><%=bundle.getString("stepsLabel")%></h2>
<ul class="instructionDetails">
<li><%=bundle.getString("instructionsStep1")%></li>
<li><%=bundle.getString("instructionsStep2")%></li>
<li><%=bundle.getString("instructionsStep3")%></li>
<li><%=bundle.getString("instructionsStep4")%></li>
</ul>
<h2><%=bundle.getString("deletionPolicyLabel")%></h2>
<p class="instructionDetails"><%=bundle.getString("deletionDetails")%></p>
<h2><%=bundle.getString("contactLabel")%></h2>
<p class="instructionDetails"><%=bundle.getString("contactDetails")%></p>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>