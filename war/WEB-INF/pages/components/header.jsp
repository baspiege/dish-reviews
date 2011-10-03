<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="geonotes.data.GeoNoteGetSingle" %>
<%@ page import="geonotes.data.model.GeoNote" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<div>
<ul id="navlist" style="margin:0;padding:0;">

<% //if (request.getServletPath().indexOf("geoNotes.jsp")==-1) { %>

<li><a href="geoNotes.jsp">Main</a></li>
<% //} %>


<%

    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    UserService userService = UserServiceFactory.getUserService();
    boolean isSignedIn=request.getUserPrincipal()!= null;    

    Long storeId=RequestUtils.getNumericInput(request,"storeId",bundle.getString("storeId"),false);
    Long dishId=RequestUtils.getNumericInput(request,"dishId",bundle.getString("dishId"),false);
    
    if (storeId!=null && dishId==null) {
    
        request.setAttribute("id",storeId);
    
        new GeoNoteGetSingle().execute(request);
        GeoNote geoNote=(GeoNote)request.getAttribute("geoNote");
        if (geoNote!=null) {
            out.write("<li>" + geoNote.note + "</li>");
        }
    } else if (storeId!=null && dishId!=null) {
    
        request.setAttribute("id",storeId);
        
//        new GeoNoteGetSingle().execute(request);
    
        new GeoNoteGetSingle().execute(request);
        GeoNote geoNote=(GeoNote)request.getAttribute("geoNote");
        if (geoNote!=null) {
            out.write("<li><a href=\"dishes.jsp?storeId=" + storeId.toString() + "\">" + geoNote.note + "</a></li>");
        }
    }
%>
<li>    
<% if (!isSignedIn) { %>
<a href='<%=userService.createLoginURL("../geoNotes.jsp")%>'><%=bundle.getString("logonLabel")%></a>
<% } else { %>
<a href='<%=userService.createLogoutURL("../geoNotes.jsp")%>'><%=bundle.getString("logoffLabel")%></a>
<% } %>
    
</li>



<ul>
</div>