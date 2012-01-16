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
  sendRequest('../data/reviewsOwn.jsp?start=' + startIndexReview, handleReviewsDataRequest);
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
    var storeLink=document.createElement("a");
    storeLink.setAttribute("href","#");
    storeLink.setAttribute("onclick","reorderReviewsByStoreNameAscending();return false;");
    storeLink.appendChild(document.createTextNode("Restaurant"));  
    thStore.appendChild(storeLink);
    
    // Dish
    var thDish=document.createElement("th");
    tr.appendChild(thDish);
    var dishLink=document.createElement("a");
    dishLink.setAttribute("href","#");
    dishLink.setAttribute("onclick","reorderReviewsByDishNameAscending();return false;");
    dishLink.appendChild(document.createTextNode("Dish"));  
    thDish.appendChild(dishLink);
    
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
      td.setAttribute("colspan","4");
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
      tr.setAttribute("reviewId",reviewId);
      tr.setAttribute("dishName",dishText);
      tr.setAttribute("storeName",storeText);
      
      // Store
      var storeDesc=document.createElement("td");
      var storeDescLink=document.createElement("a");
      storeDescLink.setAttribute("href","store.jsp?storeId="+storeId);
      storeDescLink.appendChild(document.createTextNode(storeText));
      storeDesc.appendChild(storeDescLink);
      tr.appendChild(storeDesc);

      // Dish
      var dishDesc=document.createElement("td");
      var dishDescLink=document.createElement("a");
      dishDescLink.setAttribute("href","dish.jsp?dishId="+dishId);
      dishDescLink.appendChild(document.createTextNode(dishText));
      dishDesc.appendChild(dishDescLink);
      tr.appendChild(dishDesc);
      
      // Review
      var descReview=document.createElement("td");
      var descReviewLink=document.createElement("a");
      descReviewLink.setAttribute("href","dish.jsp?dishId="+dishId+"&reviewId="+reviewId);
      descReviewLink.appendChild(document.createTextNode(reviewText));
      descReview.appendChild(descReviewLink);
      tr.appendChild(descReview);
      
      // Image
      var imageCell=document.createElement("td");
      if (review.getAttribute("img")=="true") {
        var imageLink=document.createElement("a");
        imageLink.setAttribute("href","reviewImage.jsp?reviewId="+reviewId);
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
// Sorting
///////////////////

function reorderReviews(sortFunction) {
  var reviews=document.getElementById("reviews");
  var notes=reviews.getElementsByTagName("tr");
  var notesTemp=new Array();
  for (var i=1; i<notes.length; i++) {
    notesTemp.push(notes[i]);
  }
  notesTemp.sort(sortFunction);
  for (var i=0; i<notesTemp.length; i++) {
    reviews.appendChild(notesTemp[i]);
  }
}

function sortByDishNameAscending(note1,note2) {
  var name1=note1.getAttribute("dishName");
  var name2=note2.getAttribute("dishName");
  return name1.localeCompare(name2);
}

function sortByStoreNameAscending(note1,note2) {
  var name1=note1.getAttribute("storeName");
  var name2=note2.getAttribute("storeName");
  return name1.localeCompare(name2);
}

function reorderReviewsByDishNameAscending() {
  reorderReviews(sortByDishNameAscending);
}

function reorderReviewsByStoreNameAscending() {
  reorderReviews(sortByStoreNameAscending);
}

///////////////////
// Display
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