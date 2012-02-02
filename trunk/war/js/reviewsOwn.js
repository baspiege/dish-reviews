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
  sendRequest('reviewsOwnXml?start=' + startIndexReview, handleReviewsDataRequest);
}

function handleReviewsDataRequest(req) {
  var tableOrig=document.getElementById("reviews");
  var table;
  var newTable=false;
  if (tableOrig==null) {
    newTable=true;
    table=document.createElement("table");
    table.setAttribute("id","reviews");
    var tr=document.createElement("tr");
    table.appendChild(tr);

    // Store
    var thStore=document.createElement("th");
    tr.appendChild(thStore);
    thStore.appendChild(document.createTextNode("Restaurant"));

    // Dish
    var thDish=document.createElement("th");
    tr.appendChild(thDish);
    thDish.appendChild(document.createTextNode("Dish"));

    // Time
    var thTime=document.createElement("th");
    tr.appendChild(thTime);
    thTime.appendChild(document.createTextNode("Time Ago"));

    // Review
    var thReview=document.createElement("th");
    tr.appendChild(thReview);
    thReview.appendChild(document.createTextNode("Review"));

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
      td.setAttribute("colspan","5");
      td.appendChild(document.createTextNode("No reviews."));
      tr.appendChild(td);
      table.appendChild(tr);
      var tableDiv=document.getElementById("data");
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
      var dishId=review.getAttribute("dishId");
      var storeId=review.getAttribute("storeId")
      var storeText=review.getAttribute("storeText");
      var dishText=review.getAttribute("dishText");
      var reviewText=review.getAttribute("text");

      // Store
      var storeDesc=document.createElement("td");
      var storeDescLink=document.createElement("a");
      storeDescLink.setAttribute("href","store?storeId="+storeId);
      storeDescLink.appendChild(document.createTextNode(storeText));
      storeDesc.appendChild(storeDescLink);
      tr.appendChild(storeDesc);

      // Dish
      var dishDesc=document.createElement("td");
      var dishDescLink=document.createElement("a");
      dishDescLink.setAttribute("href","dish?dishId="+dishId);
      dishDescLink.appendChild(document.createTextNode(dishText));
      dishDesc.appendChild(dishDescLink);
      tr.appendChild(dishDesc);

      // Time Ago
      var timeReview=document.createElement("td");
      var elapsedTime=getElapsedTime(parseInt(review.getAttribute("time")),currentSeconds);
      timeReview.appendChild(document.createTextNode(elapsedTime));
      tr.appendChild(timeReview);

      // Review
      var descReview=document.createElement("td");
      var descReviewLink=document.createElement("a");
      descReviewLink.setAttribute("href","dish?dishId="+dishId+"&reviewId="+reviewId);
      descReviewLink.appendChild(document.createTextNode(reviewText));
      descReview.appendChild(descReviewLink);
      tr.appendChild(descReview);

      // Image
      var imageCell=document.createElement("td");
      if (review.getAttribute("img")=="true") {
        var imageLink=document.createElement("a");
        imageLink.setAttribute("href","reviewImageUpdate?reviewId="+reviewId);
        var image=document.createElement("img");
        image.setAttribute("src","reviewThumbNailImage?reviewId="+reviewId);
        imageLink.appendChild(image);
        imageCell.appendChild(imageLink);
      }
      tr.appendChild(imageCell);

      table.appendChild(tr);
    }
    var tableDiv=document.getElementById("data");
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
    //updateNotesDispay();

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