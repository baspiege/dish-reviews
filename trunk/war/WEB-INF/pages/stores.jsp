<%-- This JSP has the HTML for stores page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean isSignedIn=request.getUserPrincipal().getName()!= null;    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("storesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
<script type="text/javascript" src="/js/stores.js" ></script>
<script type="text/javascript">
var waitingForCoordinatesMessage="<%=bundle.getString("waitingForCoordinatesMessage")%>";
var locationNotAvailableMessage="<%=bundle.getString("locationNotAvailableMessage")%>";
var locationNotFoundMessage="<%=bundle.getString("locationNotFoundMessage")%>";
var isLoggedIn=<%=isSignedIn%>;
</script>
</head>
<body onload="getCoordinates();">

<%-- Facebook login --%>
<div id="fb-root"></div>
<script>
  var dishRevAppId = "334986003195790";
  window.fbAsyncInit = function() {
    FB.init({
      appId      : dishRevAppId,
      channelUrl : '//dishrev.appspot.com/channel.html', // Channel File
      status     : true, 
      cookie     : true,
      xfbml      : true,
      oauth      : true,
    });
    
    FB.Event.subscribe('auth.login', function(response) {
      setCookie("dishRevUser",response.authResponse.userID);
      if (!isLoggedIn){
        window.location.reload();
      }
    });

    FB.Event.subscribe('auth.logout', function(response) {
      setCookie("dishRevUser","",-1);
      if (isLoggedIn){
        // Commenting out because some browsers fire this event even when logged in.
        //window.location.reload();
      }
    });
  };
  
  setCookie("dishRevUser","1");
  
  (function(d){
    var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
    js = d.createElement('script'); js.id = id; js.async = true;
    js.src = "//connect.facebook.net/en_US/all.js";
    d.getElementsByTagName('head')[0].appendChild(js);
  }(document));
  
</script>

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<% if (isSignedIn) { %>
<li><a href='reviewsOwn'>My Reviews</a><li>
<% } %>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<%-- Location --%>
<div style="margin-top:1.5em"><span id="geoStatus"></span><a style="margin-left:1em" href="location.jsp"><%=bundle.getString("changeLocationLabel")%></a></div>
<%-- Data --%>
<div style="margin-top:1.5em" class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>