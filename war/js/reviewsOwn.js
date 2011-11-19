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

function getReviewsData() {
  sendRequest('reviewsOwnTable.jsp?start=0', handleReviewsDataRequest);
}

function handleReviewsDataRequest(req) {
  var table=document.createElement("table");
  table.setAttribute("id","reviews");
  var tr=document.createElement("tr");
  table.appendChild(tr);
  
  // Store
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.setAttribute("onclick","reorderReviewsByStoreNameAscending();return false;");
  nameLink.appendChild(document.createTextNode("Store"));  
  thName.appendChild(nameLink);
  
  // Dish
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.setAttribute("onclick","reorderReviewsByDishNameAscending();return false;");
  nameLink.appendChild(document.createTextNode("Dish"));  
  thName.appendChild(nameLink);
  
  // Review
  var thName=document.createElement("th");
  tr.appendChild(thName);  
  thName.appendChild(document.createTextNode("Review"));
  
  // Image
  var thName=document.createElement("th");
  tr.appendChild(thName);
  thName.appendChild(document.createTextNode("Image"));
  
  // Process request
  var xmlDoc=req.responseXML;
  var reviews=xmlDoc.getElementsByTagName("review");
  if (reviews.length==0){
    var tr=document.createElement("tr");
    var td=document.createElement("td");
    td.setAttribute("colspan","7");
    td.appendChild(document.createTextNode("No nearby restaurants."));
    tr.appendChild(td);
    table.appendChild(tr);
    var tableDiv=document.getElementById("data");
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
  } else {
    // Make HTML for each review
    for (var i=0;i<reviews.length;i++) {
      var review=reviews[i];
      var tr=document.createElement("tr");
      // Attributes
      var reviewId=review.getAttribute("reviewId");
      var dishId=review.getAttribute("dishId");
      var storeId=review.getAttribute("storeId")
      tr.setAttribute("reviewId",reviewId);
      tr.setAttribute("name",review.getAttribute("text").toLowerCase());
      tr.setAttribute("yes",review.getAttribute("yes"));
      
      // Store
      var storeDesc=document.createElement("td");
      var storeDescLink=document.createElement("a");
      storeDescLink.setAttribute("href","dishes.jsp?storeId="+storeId);
      var storeText=review.getAttribute("storeText");
      storeDescLink.appendChild(document.createTextNode(storeText));
      storeDesc.appendChild(storeDescLink);
      tr.appendChild(storeDesc);

      // Dish
      var dishDesc=document.createElement("td");
      var dishDescLink=document.createElement("a");
      dishDescLink.setAttribute("href","reviews.jsp?dishId="+dishId);
      var dishText=review.getAttribute("dishText");
      dishDescLink.appendChild(document.createTextNode(dishText));
      dishDesc.appendChild(dishDescLink);
      tr.appendChild(dishDesc);
      
      // Desc
      var desc=document.createElement("td");
      var descLink=document.createElement("a");
      descLink.setAttribute("href","reviewUpdate.jsp?reviewId="+reviewId);
      var text=review.getAttribute("text");
      descLink.appendChild(document.createTextNode(text));
      desc.appendChild(descLink);
      tr.appendChild(desc);
      
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

function sortByNameAscending(note1,note2) {
  var name1=note1.getAttribute("name");
  var name2=note2.getAttribute("name");
  return name1.localeCompare(name2);
}



function reorderReviewsByNameAscending() {
  setCookie("sortBy","name");
  reorderReviews(sortByNameAscending);
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

function updateNotesDispay() {
  // Sort
  var sortBy=getCookie("sortBy");
  if (sortBy=="name") {
    reorderReviewsByNameAscending();
  } else if (sortBy=="voteYes") {
    reorderReviewsByVoteYesDescending();
  }
}S