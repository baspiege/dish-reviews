<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ResourceBundle" %>
<% ResourceBundle bundle = ResourceBundle.getBundle("Text"); %>
<div class="footer">
<p>
<%= bundle.getString("copyrightLabel")%> <%= Calendar.getInstance().get(Calendar.YEAR) %> Brian Spiegel
</p>
</div>