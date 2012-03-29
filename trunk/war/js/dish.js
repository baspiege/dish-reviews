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

var xmlHttpRequest=new XMLHttpRequest();

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
// Facebook
///////////////////

function postReviewToFacebook(reviewId) {
  var reviewLink="http://dishrev.appspot.com/dish?dishId=" + dishId + "&reviewId=" + reviewId;
  var reviewImageLink="http://dishrev.appspot.com/reviewThumbNailImage?reviewId" + reviewId;
  var dishName=document.getElementById("dishName").innerHTML;
  var storeName=document.getElementById("storeName").innerHTML;
  var publish = {
    method: 'feed',
    name: dishName + " at " + storeName,
    link: reviewLink,
    picture: reviewImageLink
  };
  FB.init();
  FB.ui(publish);
}

///////////////////
// Data
///////////////////

var gettingReviews=false;
var moreReviews=false;
window.onscroll=checkForMoreReviews;
var startIndexReview=0;
var PAGE_SIZE=10; // If changes, update server count as well.

function checkForMoreReviews() {
  var moreIndicator=document.getElementById("moreIndicator");
  if (moreReviews && !gettingReviews && moreIndicator && elementInViewport(moreIndicator)) {
    gettingReviews=true;
    startIndexReview+=PAGE_SIZE;
    getReviewsData();
  }
}

function getCachedData() {
    var xmlDoc=null;
    var cachedResponse=localStorage.getItem(getDishKey());
    if (cachedResponse) {
      var parser=new DOMParser();
      xmlDoc=parser.parseFromString(cachedResponse,"text/xml");
    }
    return xmlDoc;
}

function getReviewsData() {
  // If online, get from server.  Else get from cache.
  if (navigator.onLine) {
    sendRequest('reviewsXml?dishId='+dishId+'&start=' + startIndexReview, handleReviewsDataRequest, displayCachedData);
  } else {
    displayCachedData();
  }
}

function getDishKey() {
  return "DISH_"+dishId+"_"+startIndexReview;
}

function getReviewsDataById() {
  // If online, get from server.  Else get from cache.  // TODO - Update cached data?
  if (navigator.onLine) {
    sendRequest('reviewsXml?reviewId='+reviewId, handleReviewsDataRequest, displayCachedData);
  } else {
    displayCachedData();
  }
}

function handleReviewsDataRequest(req) {
  // Save in local storage in case app goes offline
  setItemIntoLocalStorage(getDishKey(), req.responseText);

  // Process response
  var xmlDoc=req.responseXML;
  displayData(xmlDoc);
}

///////////////////
// Data Display
///////////////////

function displayCachedData() {
  var xmlDoc=getCachedData();
  if (xmlDoc) {
    displayData(xmlDoc);
  } else {
    displayTableNoCachedData();
  }
}

function displayData(xmlDoc) {
  document.getElementById("waitingForData").style.display="none";
  var table=document.getElementById("reviews");
  var newTable=false;
  if (table==null) {
    newTable=true;
    table=createTable();
    document.getElementById("data").appendChild(table);
  }
  
  // Set store name and dish name
  var dish=xmlDoc.getElementById("dish");
  if (dish) {
    var storeName=dish.getAttribute("storeName");
    if (storeName) {
      var storeNameTag=document.getElementById("storeName");
      if (storeNameTag) {
        removeChildrenFromElement(storeNameTag);
        storeNameTag.appendChild(document.createTextNode(storeName));
      }
    }
    var dishName=dish.getAttribute("dishName");
    if (dishName) {
      var dishNameTag=document.getElementById("dishName");
      if (dishNameTag) {
        removeChildrenFromElement(dishNameTag);
        dishNameTag.appendChild(document.createTextNode(dishName));
      }
      var title=document.getElementById("title");
      if (title) {
        removeChildrenFromElement(title);
        dishNameTag.appendChild(document.createTextNode(dishName));
      }
    }
  }

  // Process reviews
  var reviews=xmlDoc.getElementsByTagName("review");
  var moreIndicator=document.getElementById("moreIndicator");
  if (reviews.length==0){
    moreReviews=false;
    moreIndicator.style.display="none";
    if (newTable) {
      table.appendChild(createTableRowForNoData());
    }
  } else {
    // Check if more
    if (reviews.length<PAGE_SIZE){
      moreReviews=false;
      moreIndicator.style.display="none";
    } else {
      moreReviews=true;
    }

    // Make row for each review
    for (var i=0;i<reviews.length;i++) {
      var review=reviews[i];
      table.appendChild(createTableRowForReview(review));
    }

    // Show 'more' after table is populated
    if (moreReviews) {
      moreIndicator.style.display="inline";
    }

    // Parse for Facebook tags
    if (typeof(FB) != "undefined") {
      FB.XFBML.parse(table);
    }

    gettingReviews=false;
    checkForMoreReviews();
  }
}

///////////////////
// Display
///////////////////

function createTable() {
  var table=document.createElement("table");
  table.setAttribute("id","reviews");
  var tr=document.createElement("tr");
  table.appendChild(tr);

  // Review
  var thReview=document.createElement("th");
  tr.appendChild(thReview);
  thReview.appendChild(document.createTextNode("Review"));

  // Show Add link
  if (canEdit) {
    var addLink=document.createElement("a");
    addLink.setAttribute("href","reviewAdd?dishId="+dishId);
    addLink.setAttribute("class","add addTh");
    addLink.appendChild(document.createTextNode("Add"));
    thReview.appendChild(addLink);
  }

  // Time
  var thTime=document.createElement("th");
  tr.appendChild(thTime);
  thTime.appendChild(document.createTextNode("Time Ago"));

  // Agree
  var thVote=document.createElement("th");
  tr.appendChild(thVote);
  thVote.appendChild(document.createTextNode("Agree"));

  // Image
  var thImage=document.createElement("th");
  tr.appendChild(thImage);
  thImage.appendChild(document.createTextNode("Image"));

  return table;
}

function createTableRowForReview(review) {
  var tr=document.createElement("tr");
  var currentSeconds=new Date().getTime()/1000;

  // Attributes
  var reviewId=review.getAttribute("reviewId");
  var reviewText=review.getAttribute("text");
  var vote=review.getAttribute("yes");
  var time=review.getAttribute("time");
  var usersOwn=review.getAttribute("user")=="true";
  var userId=review.getAttribute("userId");
  tr.setAttribute("id","reviewId"+reviewId);
  tr.setAttribute("reviewText",reviewText);

  // Description
  var descReview=document.createElement("td");
  tr.appendChild(descReview);
  descReview.appendChild(document.createTextNode(reviewText));
  descReview.appendChild(document.createTextNode(" "));

  // Edit and Facebook button
  if (usersOwn) {
    var editLink=document.createElement("a");
    editLink.setAttribute("href","reviewUpdate?reviewId="+reviewId);
    editLink.setAttribute("class","edit");
    editLink.appendChild(document.createTextNode("Edit"));
    descReview.appendChild(editLink);
    descReview.appendChild(document.createTextNode(" "));

    var postButton=document.createElement("button");
    postButton.setAttribute("onclick","postReviewToFacebook(\"" + reviewId + "\");return false;");
    postButton.appendChild(document.createTextNode("Share on Facebook"));
    descReview.appendChild(postButton);
  } else {
    // Add name from Facebook id.
    // Note, adding with createElementNS didn't work.  So using innerHTML.
    var fbName=document.createElement("span");
    descReview.appendChild(fbName);
    fbName.innerHTML=' - <fb:name uid="' + userId + '" useyou="false" linked="true"></fb:name>';
  }

  // Time Ago
  var timeReview=document.createElement("td");
  var elapsedTime=getElapsedTime(parseInt(review.getAttribute("time")),currentSeconds);
  timeReview.appendChild(document.createTextNode(elapsedTime));
  tr.appendChild(timeReview);

  // Vote
  if (canEdit) {
      var voteDisplay=document.createElement("td");
      var voteLink=document.createElement("a");
      voteLink.setAttribute("href","reviewVote?reviewId="+reviewId);
      voteLink.setAttribute("class","center");
      voteLink.appendChild(document.createTextNode(vote));
      voteDisplay.appendChild(voteLink);
      tr.appendChild(voteDisplay);
  } else {
      var voteDisplay=document.createElement("td");
      voteDisplay.setAttribute("class","center");
      voteDisplay.appendChild(document.createTextNode(vote));
      tr.appendChild(voteDisplay);
  }

  // Image
  var imageCell=document.createElement("td");
  if (review.getAttribute("img")=="true") {
    var imageLink=document.createElement("a");
    imageLink.setAttribute("href","reviewImageUpdate?reviewId="+reviewId);
    var image=document.createElement("img");
    image.setAttribute("src","reviewThumbNailImage?reviewId="+reviewId);
    imageLink.appendChild(image);
    imageCell.appendChild(imageLink);
  } else if (usersOwn) {
    var imageLink=document.createElement("a");
    imageLink.setAttribute("class","add");
    imageLink.setAttribute("href","reviewImageUpdate?reviewId="+reviewId);
    imageLink.appendChild(document.createTextNode("Add"));
    imageCell.appendChild(imageLink)
  }
  tr.appendChild(imageCell);

  return tr;
}

function createTableRowForNoCachedData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","4");
  td.appendChild(document.createTextNode("No connectivity or cached data.  Please try again later."));
  tr.appendChild(td);
  return tr;
}

function createTableRowForNoData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","4");
  td.appendChild(document.createTextNode("No reviews."));
  tr.appendChild(td);
  return tr;
}

function getElapsedTime(oldSeconds,newSeconds){
  var display="";
  var seconds=newSeconds-oldSeconds;
  if (seconds<60){
    display=Math.round(seconds)+" sec";
  } else {
    var minutes=seconds/60;
    if (minutes<60) {
      display=Math.round(minutes)+" min";
    } else {
      var hours=minutes/60;
      if (hours<24) {
        display=Math.round(hours)+" hr";
      } else {
        var days=hours/24;
        display=Math.round(days)+" days";
      }
    }
  }
  return display;
}

function displayTableNoCachedData() {
  document.getElementById("waitingForData").style.display="none";
  document.getElementById("moreIndicator").style.display="none";
  var table=document.getElementById("reviews");
  if (table==null) {
    table=createTable();
    document.getElementById("data").appendChild(table);
  }
  table.appendChild(createTableRowForNoCachedData());
}

///////////////////
// Set-up page
///////////////////

function setUpPage() {
  // Check if logged in
  var dishRevUser=getCookie("dishRevUser");
  isLoggedIn=false;
  if (dishRevUser!="") {
    isLoggedIn=true;
  }

  // If online, show FB login
  // If offline, show offline
  var fblogin=document.getElementById("fblogin");
  var fbname=document.getElementById("fbname");
  var offline=document.getElementById("offline");
  if (navigator.onLine) {
    fblogin.style.display="inline";
    fbname.style.display="inline";
    offline.style.display="none";
  } else {
    fblogin.style.display="none";
    fbname.style.display="none";
    offline.style.display="inline";
  }

  // If logged in and online, can edit
  canEdit=isLoggedIn && navigator.onLine;

  // Show 'Edit link' if can edit
  var dishEditLink=document.getElementById("dishEditLink");
  if (canEdit) {
     dishEditLink.style.display='inline';
  } else {
     dishEditLink.style.display='none';
  }
}

function setOnlineListeners() {
  document.body.addEventListener("offline", setUpPage, false)
  document.body.addEventListener("online", setUpPage, false);
}

///////////////////
// Utils
///////////////////

function removeChildrenFromElement(element) {
  if (element.hasChildNodes()) {
    while (element.childNodes.length>0) {
      element.removeChild(element.firstChild);
    }
  }
}

function elementInViewport(el) {
  var rect = el.getBoundingClientRect();
  return (rect.top >= 0 && rect.bottom <= window.innerHeight);
}

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