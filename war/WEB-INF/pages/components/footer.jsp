<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ResourceBundle" %>
<% ResourceBundle bundle = ResourceBundle.getBundle("Text"); %>
<div class="footer" style="clear:both;padding-top:1em">
<%--
<p>
<%= bundle.getString("disclaimerLabel")%> <a href="instructions.jsp"><%= bundle.getString("instructionsLabel")%></a>
</p>
--%>
<p>
<%= bundle.getString("copyrightLabel")%> <%= Calendar.getInstance().get(Calendar.YEAR) %> Brian Spiegel
</p>
</div>