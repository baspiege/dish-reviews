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

function getReviewsOwnKey() {
  return "REVIEWS_OWN_"+startIndexReview;
}

function getReviewsData() {
  // If online, get from server.  Else get from cache.
  if (navigator.onLine) {
    sendRequest('reviewsOwnXml?start=' + startIndexReview, handleReviewsDataRequest, displayCachedData);
  } else {
    displayCachedData();
  }

}

function handleReviewsDataRequest(req) {
  // Save in local storage in case app goes offline
  setItemIntoLocalStorage(getReviewsOwnKey(), req.responseText);

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

  // Process request
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
  
  return table;
}

function createTableRowForReview(review) {
  var tr=document.createElement("tr");
  var currentSeconds=new Date().getTime()/1000;

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
  return tr;
}

function createTableRowForNoCachedData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","5");
  td.appendChild(document.createTextNode("No connectivity or cached data.  Please try again later."));
  tr.appendChild(td);
  return tr;
}

function createTableRowForNoData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","5");
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
}

function setOnlineListeners() {
  document.body.addEventListener("offline", setUpPage, false)
  document.body.addEventListener("online", setUpPage, false);
}

///////////////////
// Util
///////////////////

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