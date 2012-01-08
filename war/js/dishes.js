
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
  sendRequest('../data/dishes.jsp?storeId='+storeId+'&start='+startIndexReview+'&sortBy='+sortBy, handleDishesDataRequest);
}

function handleDishesDataRequest(req) {
  var tableDiv=document.getElementById("data");

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
    nameLink.setAttribute("onclick","sortDishesBy('name');return false;");
    nameLink.appendChild(document.createTextNode("Dish"));  
    thName.appendChild(nameLink);
    
    // Show Add link if logged in
    if (isLoggedIn) {
      var addLink=document.createElement("a");
      addLink.setAttribute("href","dishAdd.jsp?storeId="+storeId);
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
    var thReview=document.createElement("th");
    tr.appendChild(thReview);  
    thReview.appendChild(document.createTextNode("Last Review"));
    
    // Last Image
    var thLastImage=document.createElement("th");
    tr.appendChild(thLastImage);
    thLastImage.appendChild(document.createTextNode("Last Image"));
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
      removeChildrenFromElement(tableDiv);
      // Update tableDiv with new table at end of processing to prevent multiple
      // requests from interfering with each other
      tableDiv.appendChild(table);
    } else {
      removeChildrenFromElement(document.getElementById("moreIndicator"));
    }
  } else {
    if (dishes.length<PAGE_SIZE){
      moreDishes=false;
    }
    // Make HTML for each dish
    for (var i=0;i<dishes.length;i++) {
      var dish=dishes[i];
      var tr=document.createElement("tr");
      // Attributes
      var dishId=dish.getAttribute("dishId");
      var dishText=dish.getAttribute("dishText");
      var vote=dish.getAttribute("yes");
      var lastReviewText=dish.getAttribute("lastReviewText");
      var lastReviewImageId=dish.getAttribute("lastReviewImageId");
      tr.setAttribute("dishName",dishText);

      // Dish
      var dishDesc=document.createElement("td");
      var dishDescLink=document.createElement("a");
      dishDescLink.setAttribute("href","reviews.jsp?dishId="+dishId);
      dishDescLink.appendChild(document.createTextNode(dishText));
      dishDesc.appendChild(dishDescLink);
      
      if (isLoggedIn) {
        var editLink=document.createElement("a");
        editLink.setAttribute("href","dishUpdate.jsp?dishId="+dishId);
        editLink.setAttribute("class","edit");
        editLink.appendChild(document.createTextNode("edit"));
        dishDesc.appendChild(document.createTextNode(' '));
        dishDesc.appendChild(editLink);
      }
      
      tr.appendChild(dishDesc);
      
      // Vote      
      if (isLoggedIn) {
          var voteDisplay=document.createElement("td");
          var voteLink=document.createElement("a");
          voteLink.setAttribute("href","dishVote.jsp?dishId="+dishId);
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
      var lastReview=document.createElement("td");
      if (lastReviewText) {
        var reviewLink=document.createElement("a");
        reviewLink.setAttribute("href","reviews.jsp?dishId="+dishId);
        reviewLink.appendChild(document.createTextNode(lastReviewText));
        lastReview.appendChild(reviewLink);
      } else if (isLoggedIn) {
        var addLink=document.createElement("a");
        addLink.setAttribute("class","add");
        addLink.setAttribute("href","reviewAdd.jsp?dishId="+dishId);
        addLink.appendChild(document.createTextNode("Add"));
        lastReview.appendChild(addLink);
      }
      tr.appendChild(lastReview);
      
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
    removeChildrenFromElement(tableDiv);
    // Update tableDiv with new table at end of processing to prevent multiple
    // requests from interfering with each other
    tableDiv.appendChild(table);
    
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
// Sort
///////////////////

function sortDishesBy(fieldToSortBy) {
  removeChildrenFromElement(document.getElementById("data"));  // Reset data
  sortBy=fieldToSortBy;
  getDishesData();
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