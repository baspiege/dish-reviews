///////////////////
// Asynch
///////////////////

var xmlHttpRequest=new XMLHttpRequest();

function sendRequest(url,callback,postData) {
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
var moreReviews=true;
window.onscroll=checkForMoreReviews;
var startIndexReview=0;
var PAGE_SIZE=10; // If changes, update server count as well.

function checkForMoreReviews() {
  var moreIndicator=document.getElementById("moreIndicator");
  if (moreIndicator && elementInViewport(moreIndicator) && !gettingReviews && moreReviews) {
    gettingReviews=true;
    startIndexReview+=PAGE_SIZE;
    getReviewsData();
  }
}

function getReviewsData() {
  sendRequest('reviewsXml?dishId='+dishId+'&start=' + startIndexReview, handleReviewsDataRequest);
}

function getReviewsDataById() {
  sendRequest('reviewsXml?reviewId='+reviewId, handleReviewsDataRequest);
}

function handleReviewsDataRequest(req) {
  var tableDiv=document.getElementById("data");

  var tableOrig=document.getElementById("reviews");
  var table;
  var newTable=false;
  if (tableOrig==null) {

    newTable=true;
    table=document.createElement("table");
    table.setAttribute("id","reviews");
    var tr=document.createElement("tr");
    table.appendChild(tr);

    // Review
    var thReview=document.createElement("th");
    tr.appendChild(thReview);
    thReview.appendChild(document.createTextNode("Review"));

    // Show Add link if logged in
    if (isLoggedIn) {
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

  } else {
    table=tableOrig.cloneNode(true);
  }

  // Process request
  var xmlDoc=req.responseXML;
  var reviews=xmlDoc.getElementsByTagName("review");
  if (reviews.length==0){

    moreReviews=false;
    if (newTable) {
      var tr=document.createElement("tr");
      var td=document.createElement("td");
      td.setAttribute("colspan","4");
      td.appendChild(document.createTextNode("No reviews."));
      tr.appendChild(td);
      table.appendChild(tr);
      removeChildrenFromElement(tableDiv);
      // Update tableDiv with new table at end of processing to prevent multiple
      // requests from interfering with each other
      tableDiv.appendChild(table);
    } else {
      removeChildrenFromElement(document.getElementById("moreIndicator"));
    }
  } else {
    if (reviews.length<PAGE_SIZE){
      moreReviews=false;
    }
    // Make HTML for each review
    var currentSeconds=new Date().getTime()/1000;
    for (var i=0;i<reviews.length;i++) {

      var review=reviews[i];
      var tr=document.createElement("tr");
      // Attributes
      var reviewId=review.getAttribute("reviewId");
      var reviewText=review.getAttribute("text");
      var vote=review.getAttribute("yes");
      var time=review.getAttribute("time");
      var usersOwn=review.getAttribute("user")=="true";
      var userId=review.getAttribute("userId");
      tr.setAttribute("id","reviewId"+reviewId);
      tr.setAttribute("reviewText",reviewText);

      // Review
      var descReview=document.createElement("td");
      if (usersOwn) {

        var moreWrapper=document.createElement("span");
        moreWrapper.setAttribute("text",reviewText);
        descReview.appendChild(moreWrapper);

        // Create span... put text in attribute 'text'
        var hasMore=false;
        if (reviewText.length>20){
          reviewText=reviewText.substr(0,20);
          hasMore=true;
        }

        if (hasMore) {
        
          // Add details with summary
        }
        
        var editLink=document.createElement("a");
        editLink.setAttribute("href","reviewUpdate?reviewId="+reviewId);
        editLink.appendChild(document.createTextNode(reviewText));
        moreWrapper.appendChild(editLink);

        if (hasMore){
          var moreLink=document.createElement("a");
          moreLink.setAttribute("href","#");
          moreLink.setAttribute("class","more");
          moreLink.setAttribute("onclick","alert(this.parentNode.getAttribute('text'));return false;");
          moreLink.appendChild(document.createTextNode("more..."));
          moreWrapper.appendChild(document.createTextNode(" "));
          moreWrapper.appendChild(moreLink);
        }
      } else {
        descReview.appendChild(document.createTextNode(reviewText));
      }
      tr.appendChild(descReview);

      if (usersOwn) {
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
      if (isLoggedIn) {
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

      table.appendChild(tr);

    }
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);

    // Parse for Facebook tags
    if (typeof(FB) != "undefined") {
      FB.XFBML.parse(tableDiv);
    }

    if (moreReviews) {
      var moreIndicator=document.createElement("p");
      moreIndicator.setAttribute("id","moreIndicator");
      moreIndicator.appendChild(document.createTextNode("Loading more..."));
      tableDiv.appendChild(moreIndicator);
    }
    gettingReviews=false;
    checkForMoreReviews();
  }
}

///////////////////
// Display
///////////////////

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

///////////////////
// Is logged in
///////////////////

function setLoggedIn() {
  var dishRevUser=getCookie("dishRevUser");
  if (dishRevUser!="") {
    isLoggedIn=true;
  }

  // Show 'Edit link' if logged in
  var dishEditLink=document.getElementById("dishEditLink");  
  if (isLoggedIn) {
     dishEditLink.style.display='inline';
  } else {
     dishEditLink.style.display='none';
  }
}