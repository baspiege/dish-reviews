///////////////////
// Global vars
///////////////////

var Store=new Object();

var storeId;
var gettingDishes=false;
var moreDishes=false;
window.onscroll=Store.checkForMoreDishes;
var startIndexDish=0;
var PAGE_SIZE=10; // If changes, update server count as well.
var sortBy="name"

///////////////////
// Data
///////////////////

Store.checkForMoreDishes=function() {
  var moreIndicator=document.getElementById("moreIndicator");
  if (moreDishes && !gettingDishes && moreIndicator && elementInViewport(moreIndicator)) {
    gettingDishes=true;
    startIndexDish+=PAGE_SIZE;
    Store.getDishesData();
  }
}

Store.getCachedData=function() {
    var xmlDoc=null;
    var cachedResponse=localStorage.getItem(Store.getStoreKey());
    if (cachedResponse) {
      var parser=new DOMParser();
      xmlDoc=parser.parseFromString(cachedResponse,"text/xml");
    }
    return xmlDoc;
}

Store.getDishesData=function() {
  var qsString=getQueryStrings();
  var reload=false;
  if (qsString && qsString.reload && qsString.reload=="true") {
    reload=true;
  }

  // If online, get from server.  Else get from cache.
  if (navigator.onLine) {
    if (!reload) {
      var xmlDoc=Store.getCachedData();
      if (xmlDoc) {
        Store.displayData(xmlDoc);
      }
    }
    sendRequest('/dishesXml?storeId='+storeId+'&start='+startIndexDish+'&sortBy='+sortBy, Store.handleDishesDataRequest, Store.displayCachedData);
  } else {
    Store.displayCachedData();
  }
}

Store.getStoreKey=function() {
  return "STORE_"+storeId+"_"+startIndexDish+"_"+sortBy;
}

Store.handleDishesDataRequest=function(req) {
  var display=true;
  var qsString=getQueryStrings();
  var reload=qsString && qsString.reload && qsString.reload=="true";
  if (!reload) {
    var cachedResponse=localStorage.getItem(Store.getStoreKey());
    if (cachedResponse!=null) {
      var display=false;
      if (cachedResponse!=req.responseText) {
        var response=confirm("Do you want to display new data?");
        if (response) {
          window.location.href=window.location.href+"&reload=true";
        }
      }
    }
  }

  // Save in local storage in case app goes offline
  setItemIntoLocalStorage(Store.getStoreKey(), req.responseText);

  // Process response
  if (display) {
    var xmlDoc=req.responseXML;
    Store.displayData(xmlDoc);
  }
}

///////////////////
// Data Display
///////////////////

Store.displayCachedData=function() {
  var xmlDoc=Store.getCachedData();
  if (xmlDoc) {
    Store.displayData(xmlDoc);
  } else {
    Store.displayTableNoCachedData();
  }
}

Store.displayData=function(xmlDoc) {
  document.getElementById("waitingForData").style.display="none";
  var table=document.getElementById("dishes");  
  var newTable=false;
  if (table==null) {
    newTable=true;
    table=Store.createTable();
    document.getElementById("data").appendChild(table);
  }
  
  // Set store name
  var store=xmlDoc.getElementsByTagName("store")[0];
  var storeName=store.getAttribute("storeName");
  var title=document.getElementById("title");
  removeChildrenFromElement(title);
  title.appendChild(document.createTextNode(storeName));
  title.appendChild(document.createTextNode(storeName));
  var storeNameTag=document.getElementById("storeName");
  removeChildrenFromElement(storeNameTag);
  storeNameTag.appendChild(document.createTextNode(storeName));

  // Process dishes
  var dishes=xmlDoc.getElementsByTagName("dish");
  var moreIndicator=document.getElementById("moreIndicator");
  if (dishes.length==0){
    moreDishes=false;
    moreIndicator.style.display="none";
    if (newTable) {
      table.appendChild(Store.createTableRowForNoData());
    }
  } else {
    // Check if more
    if (dishes.length<PAGE_SIZE){
      moreDishes=false;
      moreIndicator.style.display="none";
    } else {
      moreDishes=true;
    }
    
    // Make row for each dish
    for (var i=0;i<dishes.length;i++) {
      var dish=dishes[i];
      table.appendChild(Store.createTableRowForDish(dish));
    }
    
    // Show 'more' after table is populated
    if (moreDishes) {
      moreIndicator.style.display="inline";
    }
    
    // Parse for Facebook tags
    if (typeof(FB) != "undefined") {
      FB.XFBML.parse(table);
    }

    gettingDishes=false;
    Store.checkForMoreDishes();
  }
}

///////////////////
// Display
///////////////////

Store.createTable=function() {
  var table=document.createElement("table");
  table.setAttribute("id","dishes");
  var tr=document.createElement("tr");
  table.appendChild(tr);

  // Dish
  var thName=document.createElement("th");
  tr.appendChild(thName);
  var nameLink=document.createElement("a");
  nameLink.setAttribute("href","#");
  nameLink.addEventListener('click', function(e){e.preventDefault();Store.sortDishesBy('name')}, false);
  nameLink.appendChild(document.createTextNode("Dish"));
  thName.appendChild(nameLink);

  // Show Add link
  if (User.canEdit) {
    var addLink=document.createElement("a");
    addLink.setAttribute("href","/dishAdd?storeId="+storeId);
    addLink.setAttribute("class","add addTh");
    addLink.appendChild(document.createTextNode("Add"));
    thName.appendChild(addLink);
  }

  // Vote
  var thVote=document.createElement("th");
  tr.appendChild(thVote);
  var voteLink=document.createElement("a");
  voteLink.setAttribute("href","#");
  voteLink.addEventListener('click', function(e){e.preventDefault();Store.sortDishesBy('vote')}, false);
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
  
  return table;
}

Store.createTableRowForDish=function(dish) {
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
  dishDescLink.setAttribute("href","/dish?dishId="+dishId);
  dishDescLink.appendChild(document.createTextNode(dishText));
  dishDesc.appendChild(dishDescLink);
  tr.appendChild(dishDesc);

  // Vote
  if (User.canEdit) {
      var voteDisplay=document.createElement("td");
      var voteLink=document.createElement("a");
      voteLink.setAttribute("href","/dishVote?dishId="+dishId);
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
    reviewLink.setAttribute("href","/dish?dishId="+dishId);
    reviewLink.appendChild(document.createTextNode(lastReviewText));
    lastReview.appendChild(reviewLink);
  } else if (User.canEdit) {
    var addLink=document.createElement("a");
    addLink.setAttribute("class","add");
    addLink.setAttribute("href","/reviewAdd?dishId="+dishId);
    addLink.appendChild(document.createTextNode("Add"));
    lastReview.appendChild(addLink);
  }

  // Add name from Facebook id.
  // Note, adding with createElementNS didn't work.  So using innerHTML.
  if (lastReviewUserId) {
    var fbSpan=document.createElement("span");
    lastReview.appendChild(fbSpan);
    fbSpan.innerHTML='  - <fb:name uid="' + lastReviewUserId + '" useyou="false" linked="true"></fb:name>';
  }

  tr.appendChild(lastReview);

  // Last Image
  var imageCell=document.createElement("td");
  if (dish.getAttribute("img")=="true") {
    var imageLink=document.createElement("a");
    imageLink.setAttribute("href","/reviewImageUpdate?reviewId="+lastReviewImageId);
    var image=document.createElement("img");
    image.setAttribute("src","/reviewThumbNailImage?reviewId="+lastReviewImageId);
    imageLink.appendChild(image);
    imageCell.appendChild(imageLink);
  }
  tr.appendChild(imageCell);
  
  return tr;
}

Store.createTableRowForNoCachedData=function() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","4");
  td.appendChild(document.createTextNode("No connectivity or cached data.  Please try again later."));
  tr.appendChild(td);
  return tr;
}

Store.createTableRowForNoData=function() {
  var tr=document.createElement("tr");
  var td=document.createElement("td");
  td.setAttribute("colspan","4");
  td.appendChild(document.createTextNode("No dishes."));
  tr.appendChild(td);
  return tr;
}

Store.displayTableNoCachedData=function() {
  document.getElementById("waitingForData").style.display="none";
  document.getElementById("moreIndicator").style.display="none";
  var table=document.getElementById("dishes");  
  if (table==null) {
    table=Store.createTable();
    document.getElementById("data").appendChild(table);
  }
  table.appendChild(Store.createTableRowForNoCachedData());
}

///////////////////
// Sort
///////////////////

Store.sortDishesBy=function(fieldToSortBy) {
  // Reset indicators and data
  document.getElementById("waitingForData").style.display="block";
  document.getElementById("moreIndicator").style.display="none";
  removeChildrenFromElement(document.getElementById("data"));
  startIndexDish=0;
  sortBy=fieldToSortBy;
  Store.getDishesData();
}

///////////////////
// Set-up page
///////////////////

Store.createStoresLayout=function () {
  // Clear content
  var content=document.getElementById("content");  
  removeChildrenFromElement(content);

  //Store.createStoresNav();
  Store.createStoreSections(); 
}

Store.createStoreSections=function() {
  var content=document.getElementById("content");  
  var sectionData=document.createElement("section");
  content.appendChild(sectionData);

  // Store name
  var storeName=document.createElement("span");
  sectionData.appendChild(storeName);
  storeName.setAttribute("id","storeName");

  // Edit link
  var editLink=document.createElement("a");
  sectionData.appendChild(editLink);
  editLink.setAttribute("href","/storeUpdate?storeId=ADD_ID");
  editLink.setAttribute("edit","class");
  editLink.setAttribute("style","display:none");
  editLink.setAttribute("id","storeEditLink");
  editLink.appendChild(document.createTextNode("Edit"));
  
  // Add space between edit and location
  sectionData.appendChild(document.createTextNode(" "));
  
  // Location link
  var locationLink=document.createElement("a");
  sectionData.appendChild(locationLink);
  locationLink.setAttribute("href","/storeUpdateLocation?storeId=ADD_ID");
  locationLink.setAttribute("edit","class");
  locationLink.appendChild(document.createTextNode("Location"));
  
  // Waiting for data
  var waitingForData=document.createElement("progress");
  sectionData.appendChild(waitingForData);
  waitingForData.setAttribute("style","display:none");
  waitingForData.setAttribute("id","waitingForData");
  waitingForData.setAttribute("title","Waiting for data");
  
  // Store data
  var storeData=document.createElement("div");
  sectionData.appendChild(storeData);
  storeData.setAttribute("class","data");
  storeData.setAttribute("id","data");

  // More indicator
  var moreIndicator=document.createElement("progress");
  sectionData.appendChild(moreIndicator);
  moreIndicator.setAttribute("style","display:none");
  moreIndicator.setAttribute("id","moreIndicator");
  moreIndicator.setAttribute("title","Loading more");
}

Store.setUpPage=function() {
  var qsString=getQueryStrings();
  if (qsString && qsString.storeId) {
    storeId=qsString.storeId;
  }

  // Check if logged in
  var dishRevUser=getCookie("dishRevUser");
  User.isLoggedIn=false;
  if (dishRevUser!="") {
    User.isLoggedIn=true;
  }
    
  // If logged in and online, can edit
  User.canEdit=User.isLoggedIn && navigator.onLine;

  // Show 'Edit link' if can edit
  var storeEditLink=document.getElementById("storeEditLink");  
  if (User.canEdit) {
     storeEditLink.style.display='inline';
  } else {
     storeEditLink.style.display='none';
  }
}

Store.setOnlineListeners=function() {
  document.body.addEventListener("offline", Store.setUpPage, false)
  document.body.addEventListener("online", Store.setUpPage, false);
}

Store.setOnlineListeners();
Store.createStoresLayout();
Store.setUpPage();
Store.getDishesData();