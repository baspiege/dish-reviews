
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

function checkForMoreDishes() {
  var moreIndicator=document.getElementById("moreIndicator");
  if (moreIndicator && elementInViewport(moreIndicator) && !gettingDishes && moreDishes) {
    gettingDishes=true;
    startIndexReview+=PAGE_SIZE;
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
    //var nameLink=document.createElement("a");
    //nameLink.setAttribute("href","#");
    //nameLink.setAttribute("onclick","return false;");
    //nameLink.setAttribute("onclick","reorderDishesByDishNameAscending();return false;");
    //nameLink.appendChild(document.createTextNode("Dish"));  
    //thName.appendChild(nameLink);
    thName.appendChild(document.createTextNode("Dish"));
    
    // Show Add link if logged in
    if (isLoggedIn=="true") {
      var addLink=document.createElement("a");
      addLink.setAttribute("href","dishAdd.jsp?storeId="+storeId);
      addLink.setAttribute("class","add addTh");
      addLink.appendChild(document.createTextNode("Add"));
      thName.appendChild(addLink);
    }
    
    // Vote
    var thVote=document.createElement("th");
    tr.appendChild(thVote);  
    thVote.appendChild(document.createTextNode("Like"));
    
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
      var tableDiv=document.getElementById("data");
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
      tr.appendChild(dishDesc);
      
      // Vote      
      if (isLoggedIn=="true") {
          var voteDisplay=document.createElement("td");
          var voteButton=document.createElement("button");
          voteDisplay.appendChild(voteButton);
          voteButton.setAttribute("id","button"+dishId);
          voteButton.setAttribute("onclick","sendYesVote("+dishId+")");
          voteButton.appendChild(document.createTextNode(vote));
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
      } else if (isLoggedIn=="true") {
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