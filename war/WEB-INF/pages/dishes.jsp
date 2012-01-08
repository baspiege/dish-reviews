<%-- This JSP has the HTML for dishes page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.StoreGetSingle" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean isSignedIn=request.getUserPrincipal().getName()!= null; 
    
    Long storeId=RequestUtils.getNumericInput(request,"storeId","storeId",false);
    Store store=null;
    if (storeId!=null) {
        store=RequestUtils.getStore(request,storeId);
    }
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%=bundle.getString("dishesLabel")%></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/dishes.js" ></script>
<script type="text/javascript">
var storeId=<%=storeId%>;
var isLoggedIn=<%=isSignedIn%>;
</script>
</head>
<body onload="getDishesData();">

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
      isLoggedIn=true;
      // If data retrieved without login, get again.
      var data=document.getElementById("data");
      if (data && !data.isLoggedIn){
        getDishesData();
      }
    });

    FB.Event.subscribe('auth.logout', function(response) {
      setCookie("dishRevUser","",-1);
      isLoggedIn=false;
      // If data retrieved with login, get again.
      var data=document.getElementById("data");
      if (data && data.isLoggedIn){
        getDishesData();
      }
    });
  };
  
  (function(d){
    var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
    js = d.createElement('script'); js.id = id; js.async = true;
    js.src = "//connect.facebook.net/en_US/all.js";
    d.getElementsByTagName('head')[0].appendChild(js);
  }(document));
      
</script>

<nav>
<ul id="navlist" style="margin:0;padding:0;">
<li><a href="stores.jsp">Main</a></li>

<li>
<fb:login-button autologoutlink="true"></fb:login-button>
</li>

<li>
<fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name>
</li>

<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<div style="margin-top:1.5em"><%= HtmlUtils.escapeChars(store.note) %></div>

<%-- Data --%>
<div class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>