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
  sendRequest('reviewsOwnTable.jsp', handleReviewsDataRequest);
}

function handleReviewsDataRequest(req) {
  var table=document.createElement("table");
  table.setAttribute("id","reviews");
  var tr=document.createElement("tr");
  table.appendChild(tr);
  
  // Note
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.setAttribute("onclick","reorderReviewsByNameAscending();return false;");
  nameLink.appendChild(document.createTextNode("Review"));  
  thName.appendChild(nameLink);
  
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
      tr.setAttribute("reviewId",reviewId);
      tr.setAttribute("name",review.getAttribute("text").toLowerCase());
      tr.setAttribute("yes",review.getAttribute("yes"));
      
      // Desc
      var desc=document.createElement("td");
      var descLink=document.createElement("a");
      descLink.setAttribute("href","reviewUpdate.jsp?reviewId="+reviewId);
      var text=review.getAttribute("text");
      descLink.appendChild(document.createTextNode(text));
      desc.appendChild(descLink);
      tr.appendChild(desc);
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

function sortByVoteYesDescending(note1,note2) {
  var vote1=parseInt(note1.getAttribute("yes"));
  var vote2=parseInt(note2.getAttribute("yes"));
  if (vote1>vote2) {
      return -1;
  } else if (vote2>vote1) {
      return 1;
  } else {
      return 0;
  }
}

function reorderReviewsByNameAscending() {
  setCookie("sortBy","name");
  reorderReviews(sortByNameAscending);
}

function reorderReviewsByVoteYesDescending() {
  setCookie("sortBy","voteYes");
  reorderReviews(sortByVoteYesDescending);
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