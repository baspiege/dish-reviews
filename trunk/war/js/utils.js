var geocoder;
var xmlHttpRequest=new XMLHttpRequest();

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

///////////////////
// Asynch
///////////////////

function sendRequest(url,callback,errorCallback,postData) {
  var req = xmlHttpRequest;
  if (!req) return;
  var method = (postData) ? "POST" : "GET";
  req.open(method,url,true);
  req.setRequestHeader('User-Agent','XMLHTTP/1.0');
  if (postData)
    req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
  req.onreadystatechange = function () {
    if (req.readyState != 4) return;
    if (req.status != 200 && req.status != 304) {
      // alert('HTTP error ' + req.status);
      errorCallback();
      return;
    }
    if (callback){
      callback(req);
    }
  }
  if (req.readyState == 4) return;
  req.send(postData);
}

///////////////////
// Math utils
///////////////////

function get2Decimals(number) {
  return Math.round(number*100)/100;
}

if (typeof(Number.prototype.toRad) === "undefined") {
  Number.prototype.toRad = function() {
    return this * Math.PI / 180;
  }
}
// Converts radians to numeric (signed) degrees
if (typeof(Number.prototype.toDeg) === "undefined") {
  Number.prototype.toDeg = function() {
    return this * 180 / Math.PI;
  }
}

///////////////////
// Coordinate utils
///////////////////

function calculateDistance(lat1, lon1, lat2, lon2) {
  var R = 6371; // km
  var dLat = (lat2-lat1).toRad();
  var dLon = (lon2-lon1).toRad();
  var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) *
          Math.sin(dLon/2) * Math.sin(dLon/2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  var d = R * c;
  return d;
}

function calculateBearing(lat1, lon1, lat2, lon2) {
  var dLon=(lon2-lon1).toRad();
  var lat1r=lat1.toRad();
  var lat2r=lat2.toRad();
  var y = Math.sin(dLon) * Math.cos(lat2r);
  var x = Math.cos(lat1r)*Math.sin(lat2r) -
          Math.sin(lat1r)*Math.cos(lat2r)*Math.cos(dLon);
  var bearing = Math.atan2(y, x).toDeg();
  return (bearing+360)%360;
}

///////////////////
// DOM
///////////////////

function removeChildrenFromElement(element) {
  if (element.hasChildNodes()) {
    while (element.childNodes.length>0) {
      element.removeChild(element.firstChild);
    }
  }
}

///////////////////
// Display
///////////////////

function elementInViewport(el) {
  var rect = el.getBoundingClientRect();
  return (rect.top >= 0 && rect.bottom <= window.innerHeight);
}

///////////////////
// Local storage
///////////////////

function setItemIntoLocalStorage(key, value) {
  try {
    localStorage.setItem(key, value);
  } catch (e) {
    if (e == QUOTA_EXCEEDED_ERR) {
      // Clear old entries - TODO - In future, just clear oldest?
      localStorage.clear();
    }
  }
}

///////////////////
// URL
///////////////////

function getQueryStrings() {
  var qsParm = new Array();
  var query = window.location.search.substring(1);
  var parms = query.split('&');
  for (var i=0; i<parms.length; i++) {
    var pos = parms[i].indexOf('=');
      if (pos > 0) {
      var key = parms[i].substring(0,pos);
      var val = parms[i].substring(pos+1);
      qsParm[key] = val;
    }
  }
  return qsParm;
}