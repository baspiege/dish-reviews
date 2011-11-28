
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

function checkForMoreDishes() {
  var moreIndicator=document.getElementById("moreIndicator");
  if (elementInViewport(moreIndicator) && !gettingDishes && moreDishes) {
    gettingDishes=true;
    startIndexReview+=10;
    getDishesData();
  }
}

function getDishesData() {
  sendRequest('../data/dishes.jsp?storeId='+storeId+'&start=' + startIndexReview, handleDishesDataRequest);
}

function handleDishesDataRequest(req) {
  var tableOrig=document.getElementById("dishes");
  var table;
  var newTable=false;
  if (tableOrig==null) {
    newTable=true;
    table=document.createElement("table");
    table.setAttribute("id","dishes");    
    var tr=document.createElement("tr");
    table.appendChild(tr);
      
    // Dish
    var thName=document.createElement("th");
    tr.appendChild(thName);
    var nameLink=document.createElement("a");
    nameLink.setAttribute("href","#");
    nameLink.setAttribute("onclick","reorderDishesByDishNameAscending();return false;");
    nameLink.appendChild(document.createTextNode("Dish"));  
    thName.appendChild(nameLink);
    
    // Vote
    
    // Last Review
    var thName=document.createElement("th");
    tr.appendChild(thName);  
    thName.appendChild(document.createTextNode("Review"));
    
    // Last Image
    var thName=document.createElement("th");
    tr.appendChild(thName);
    thName.appendChild(document.createTextNode("Image"));
  } else {
    table=tableOrig.cloneNode(true);
  }
  
  // Process request
  var xmlDoc=req.responseXML;
  var dishes=xmlDoc.getElementsByTagName("dish");
  if (dishes.length==0){
    moreDishes=false;
    if (newTable) {
      var tr=document.createElement("tr");
      var td=document.createElement("td");
      td.setAttribute("colspan","4");
      td.appendChild(document.createTextNode("No dishes."));
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
    // Make HTML for each dish
    for (var i=0;i<dishes.length;i++) {
      var dish=dishes[i];
      var tr=document.createElement("tr");
      // Attributes
      var dishId=dish.getAttribute("dishId");
      var dishText=dish.getAttribute("dishText");
      var lastReviewText=dish.getAttribute("lastReviewText");
      var lastReviewImageId=dish.getAttribute("lastReviewImageId");
      tr.setAttribute("dishName",dishText);

      // Dish
      var dishDesc=document.createElement("td");
      var dishDescLink=document.createElement("a");
      dishDescLink.setAttribute("href","dishes.jsp?dishId="+dishId);
      dishDescLink.appendChild(document.createTextNode(dishText));
      dishDesc.appendChild(dishDescLink);
      tr.appendChild(dishDesc);
      
      // Vote
      
      // Last Review
      var desc=document.createElement("td");
      var descLink=document.createElement("a");
      descLink.setAttribute("href","dishes.jsp?dishId="+dishId);
      var text=dish.getAttribute("text");
      descLink.appendChild(document.createTextNode(lastReviewText));
      desc.appendChild(descLink);
      tr.appendChild(desc);
      
      // Last Image
      var imageCell=document.createElement("td");
      if (dish.getAttribute("img")=="true") {
        var imageLink=document.createElement("a");
        imageLink.setAttribute("href","reviewImage.jsp?reviewId="+lastReviewImageId);
        var image=document.createElement("img");
        image.setAttribute("src","reviewThumbNailImage?reviewId="+lastReviewImageId);
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
    
    if (moreDishes) {
      var moreIndicator=document.createElement("p");
      moreIndicator.setAttribute("id","moreIndicator");
      moreIndicator.appendChild(document.createTextNode("Loading more..."));
      tableDiv.appendChild(moreIndicator);
    }
    gettingDishes=false;
    checkForMoreDishes();
  }
}

///////////////////
// Votes
///////////////////

function handleYesVote(req) {
  var xmlDoc=req.responseXML;
  var error=xmlDoc.getElementsByTagName("error");
  if (error.length>0){
    var message=error[0].getAttribute("message");
    alert(message);
  } else {
    var success=xmlDoc.getElementsByTagName("success");
    var dishId=success[0].getAttribute("dishId");
    var button=document.getElementById("button" + dishId);
    var yes=parseInt(button.innerHTML);
    button.innerHTML=yes+1;
  }
}

function sendYesVote(id) {
  sendRequest('dishVote.jsp?vote=yes&dishId='+id,handleYesVote);
}

///////////////////
// Sorting
///////////////////

function reorderDishes(sortFunction) {
  var dishes=document.getElementById("dishes");
  var notes=dishes.getElementsByTagName("tr");
  var notesTemp=new Array();
  for (var i=1; i<notes.length; i++) {
    notesTemp.push(notes[i]);
  }
  notesTemp.sort(sortFunction);
  for (var i=0; i<notesTemp.length; i++) {
    dishes.appendChild(notesTemp[i]);
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

function reorderDishesByNameAscending() {
  reorderDishes(sortByNameAscending);
}

function reorderDishesByVoteYesDescending() {
  reorderDishes(sortByVoteYesDescending);
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
  var rect = el.getBoundingClientRect()

  return (
    rect.top >= 0 &&
    rect.left >= 0 &&
    rect.bottom <= window.innerHeight &&
    rect.right <= window.innerWidth 
    );
}