///////////////////
// Cookies
///////////////////

function getCookie(name) {
  if (document.cookie.length>0) {
    var start=document.cookie.indexOf(name+"=");
    if (start!=-1) {
      start+=name.length+1;
      var end=document.cookie.indexOf(";",start);
      if (end==-1) {
        end=document.cookie.length;
      }
      return unescape(document.cookie.substring(start,end));
    }
  }
  return "";
}

function setCookie(name,value,daysToExpire) {
  var date=new Date();
  date.setDate(date.getDate()+daysToExpire);
  document.cookie=name+"="+escape(value)+((daysToExpire==null)?"":";expires="+date.toUTCString());
}

///////////////////
// FB Login
///////////////////

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
    window.location='/stores';
  });

  FB.Event.subscribe('auth.logout', function(response) {
    setCookie("dishRevUser","",-1);
    window.location='/stores';
  });
};

(function(d){
  var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
  js = d.createElement('script'); js.id = id; js.async = true;
  js.src = "//connect.facebook.net/en_US/all.js";
  d.getElementsByTagName('head')[0].appendChild(js);
}(document));