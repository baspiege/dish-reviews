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