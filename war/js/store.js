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

var gettingDishes=false;
var moreDishes=true;
window.onscroll=checkForMoreDishes;
var startIndexReview=0;
var PAGE_SIZE=10; // If changes, update server count as well.
var sortBy="name"

function checkForMoreDishes() {
  var moreIndicator=document.getElementById("moreIndicator");
  if (moreIndicator && elementInViewport(moreIndicator) && !gettingDishes && moreDishes) {
    gettingDishes=true;
    startIndexReview+=PAGE_SIZE;
    getDishesData();
  }
}

function getDishesData() {
  sendRequest('dishesXml?storeId='+storeId+'&start='+startIndexReview+'&sortBy='+sortBy, handleDishesDataRequest);
}

function handleDishesDataRequest(req) {
  document.getElementById("waitingForData").style.display="none";
  var table=document.getElementById("dishes");  
  var newTable=false;
  if (table==null) {
    newTable=true;
    table=createTable();
    document.getElementById("data").appendChild(table);
  }

  // Process request
  var xmlDoc=req.responseXML;
  var reviews=xmlDoc.getElementsByTagName("dish");
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
    }
    
    // Make row for each review
    for (var i=0;i<reviews.length;i++) {
      var review=reviews[i];
      table.appendChild(createTableRowForDish(review));
    }
    
    // Show 'more' after table is populated
    if (moreReviews) {
      moreIndicator.style.display="inline";
    }
    
    // Parse for Facebook tags
    //if (typeof(FB) != "undefined") {
    //  FB.XFBML.parse(table);
    //}

    gettingReviews=false;
    checkForMoreDishes();
  }
}

///////////////////
// Display
///////////////////

function createTable() {
  var table=document.createElement("table");
  table.setAttribute("id","dishes");
  var tr=document.createElement("tr");
  table.appendChild(tr);

  // Dish
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.setAttribute("onclick","sortDishesBy('name');return false;");
  nameLink.appendChild(document.createTextNode("Dish"));
  thName.appendChild(nameLink);

  // Show Add link if logged in
  if (isLoggedIn) {
    var addLink=document.createElement("a");
    addLink.setAttribute("href","dishAdd?storeId="+storeId);
    addLink.setAttribute("class","add addTh");
    addLink.appendChild(document.createTextNode("Add"));
    thName.appendChild(addLink);
  }

  // Vote
  var thVote=document.createElement("th");
  tr.appendChild(thVote);
  var voteLink=document.createElement("a");
  voteLink.setAttribute("href","#");
  voteLink.setAttribute("onclick","sortDishesBy('vote');return false;");
  voteLink.appendChild(document.createTextNode("Like"));
  thVote.appendChild(voteLink);

  // Last Review
  /*
  var thReview=document.createElement("th");
  tr.appendChild(thReview);
  thReview.appendChild(document.createTextNode("Last Review"));
  */

  // Last Image
  var thLastImage=document.createElement("th");
  tr.appendChild(thLastImage);
  thLastImage.appendChild(document.createTextNode("Last Image"));
  
  return table;
}

function createTableRowForDish(dish) {
  var tr=document.createElement("tr");
  
  // Attributes
  var dishId=dish.getAttribute("dishId");
  var dishText=dish.getAttribute("dishText");
  var vote=dish.getAttribute("yes");
  var lastReviewText=dish.getAttribute("lastReviewText");
  var lastReviewUserId=dish.getAttribute("lastReviewUserId");
  var lastReviewImageId=dish.getAttribute("lastReviewImageId");

  // Dish
  var dishDesc=document.createElement("td");
  var dishDescLink=document.createElement("a");
  dishDescLink.setAttribute("href","dish?dishId="+dishId);
  dishDescLink.appendChild(document.createTextNode(dishText));
  dishDesc.appendChild(dishDescLink);
  tr.appendChild(dishDesc);

  // Vote
  if (isLoggedIn) {
      var voteDisplay=document.createElement("td");
      var voteLink=document.createElement("a");
      voteLink.setAttribute("href","dishVote?dishId="+dishId);
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

  // Last Review
  /*
  var lastReview=document.createElement("td");
  if (lastReviewText) {
    var reviewLink=document.createElement("a");
    reviewLink.setAttribute("href","dish?dishId="+dishId);
    reviewLink.appendChild(document.createTextNode(lastReviewText));
    lastReview.appendChild(reviewLink);
  } else if (isLoggedIn) {
    var addLink=document.createElement("a");
    addLink.setAttribute("class","add");
    addLink.setAttribute("href","reviewAdd?dishId="+dishId);
    addLink.appendChild(document.createTextNode("Add"));
    lastReview.appendChild(addLink);
  }
  */

  // Add name from Facebook id.
  // Note, adding with createElementNS didn't work.  So using innerHTML.
  /*
  if (lastReviewUserId!='null') {
    var fbSpan=document.createElement("span");
    lastReview.appendChild(fbSpan);
    fbSpan.innerHTML='  - <fb:name uid="' + lastReviewUserId + '" useyou="false" linked="true"></fb:name>';
  }

  tr.appendChild(lastReview);
  */

  // Last Image
  var imageCell=document.createElement("td");
  if (dish.getAttribute("img")=="true") {
    var imageLink=document.createElement("a");
    imageLink.setAttribute("href","reviewImageUpdate?reviewId="+lastReviewImageId);
    var image=document.createElement("img");
    image.setAttribute("src","reviewThumbNailImage?reviewId="+lastReviewImageId);
    imageLink.appendChild(image);
    imageCell.appendChild(imageLink);
  }
  tr.appendChild(imageCell);
  
  return tr;
}

function createTableRowForNoData() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","3");
  td.appendChild(document.createTextNode("No dishes."));
  tr.appendChild(td);
  return tr;
}

///////////////////
// Sort
///////////////////

function sortDishesBy(fieldToSortBy) {
  // Reset data first
  removeChildrenFromElement(document.getElementById("data"));
  startIndexReview=0;
  sortBy=fieldToSortBy;
  getDishesData();
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

///////////////////
// Is logged in
///////////////////

function setLoggedIn() {
  var dishRevUser=getCookie("dishRevUser");
  if (dishRevUser!="") {
    isLoggedIn=true;
  }

  // Show 'Edit link' if logged in
  var storeEditLink=document.getElementById("storeEditLink");  
  if (isLoggedIn) {
     storeEditLink.style.display='inline';
  } else {
     storeEditLink.style.display='none';
  }
}