<%-- This JSP has the HTML for reviews page. --%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page language="java"%>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="geonotes.data.model.Dish" %>
<%@ page import="geonotes.data.model.Store" %>
<%@ page import="geonotes.utils.HtmlUtils" %>
<%@ page import="geonotes.utils.RequestUtils" %>
<%
    ResourceBundle bundle = ResourceBundle.getBundle("Text");
    boolean isSignedIn=request.getUserPrincipal().getName()!= null;
    
    Long dishId=RequestUtils.getNumericInput(request,"dishId","dishId",false);
    
    Dish dish=null;
    Store store=null;
    if (dishId!=null) {
        dish=RequestUtils.getDish(request,dishId);
        store=RequestUtils.getStore(request,dish.storeId);
    } else {
          %>
          <jsp:forward page="/storesRedirect.jsp"/>
          <%
    }    
%>
<%@ include file="/WEB-INF/pages/components/noCache.jsp" %>
<%@ include file="/WEB-INF/pages/components/docType.jsp" %>
<title><%= HtmlUtils.escapeChars(dish.note) %></title>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<script type="text/javascript" src="/js/dish.js" ></script>
<script type="text/javascript">
var dishId=<%=dishId%>;
var isLoggedIn=<%=isSignedIn%>;
</script>
</head>
<body onload="getReviewsData();">

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
<%
    out.write("<li><a href=\"store.jsp?storeId=" + store.getKey().getId() + "\">" + HtmlUtils.escapeChars(store.note) + "</a></li>");
%>
<li><fb:login-button autologoutlink="true"></fb:login-button></li>
<li><fb:name uid="loggedinuser" useyou="false" linked="true"></fb:name></li>
<ul>
</nav>

<jsp:include page="/WEB-INF/pages/components/edits.jsp"/>
<div style="margin-top:1.5em"><a href="dishUpdate.jsp?dishId=<%=dishId%>"><%= HtmlUtils.escapeChars(dish.note) %></a></div>

<%-- Data --%>
<div class="data" id="data">
<p> <%=bundle.getString("waitingForDataLabel")%> </p>
</div>
<jsp:include page="/WEB-INF/pages/components/footer.jsp"/>
</body>
</html>