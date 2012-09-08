var Stores = (function(){

  ///////////////////
  // Vars
  ///////////////////

  waitingForCoordinatesMessage="Waiting for coordinates...";
  locationNotAvailableMessage="Location Not Available";
  locationNotFoundMessage="Location Not Found";
  storesSortBy="stores-sortBy";

  ///////////////////
  // Stores Controller
  ///////////////////

  var create=function() {
    //setOnlineListeners();
    DishRev.initialize();
    createStoresLayout();
    displayCachedDataIfExists();
    getCoordinates();
  }

  var getCachedData=function() {
    var xmlDoc=null;
    var cachedResponse=localStorage.getItem(getStoresKey());
    if (cachedResponse) {
      var parser=new DOMParser();
      xmlDoc=parser.parseFromString(cachedResponse,"text/xml");
    }
    return xmlDoc;
  }

  var getStoresData=function() {
    // Get position and send request
    var lat=localStorage.getItem("latitude");
    var lon=localStorage.getItem("longitude");
    if (lat==null || lon==null) {
      return;
    }
    
    showProgressData();
    
    // If online, get from server.  Else get from cache.
    if (navigator.onLine) {
      //displayCachedDataIfExists();
      sendRequest('/storesXml?latitude='+lat+'&longitude='+lon, handleStoresDataRequest, displayCachedData);
    } else {
      displayCachedData();
    }
  }

  var getStoresKey=function() {
    var latitude=get2Decimals(parseFloat(localStorage.getItem("latitude")));
    var longitude=get2Decimals(parseFloat(localStorage.getItem("longitude")));
    return "STORES_"+latitude+"_"+longitude;
  }

  var handleStoresDataRequest=function(req) {
    var display=true;
    var cachedResponse=localStorage.getItem(getStoresKey());
    
    // Save in local storage in case app goes offline
    // TODO - Get lat/lon from result.  Might change between request and response.
    setItemIntoLocalStorage(getStoresKey(), req.responseText);
    
    if (cachedResponse!=null) {
      var display=false;
      if (cachedResponse!=req.responseText) {
        var response=confirm("Do you want to display new data?");
        if (response) {
          display=true;
        }
      }
    }

    // Process response
    if (display) {
      var xmlDoc=req.responseXML;
      displayData(xmlDoc);
    }
  }

  var setOnlineListeners=function() {
  //  document.body.addEventListener("offline", Stores.setUpPage, false)
  //  document.body.addEventListener("online", Stores.setUpPage, false);
  }

  var getCoordinates=function() {
    var useGeoLocation=localStorage.getItem("useGeoLocation");
    if (useGeoLocation==null || useGeoLocation=="true") {
      updateGeoStatus(waitingForCoordinatesMessage);
      var geolocation = navigator.geolocation;
      if (geolocation) {
        geolocation.getCurrentPosition(setPosition,displayLocationError);
      } else {
        updateGeoStatus(locationNotAvailableMessage);
      }
    } else {
      if (geocoder) {
        var latLng = new google.maps.LatLng(localStorage.getItem("latitude"), localStorage.getItem("longitude"));
        geocodePosition(latLng);
      }
      updateAddButtons();
      getStoresData();
    }
  }

  // Set global variables holding the position
  var setPosition=function(position) {
    if (position){
      // Set global variables
      setItemIntoLocalStorage("latitude",position.coords.latitude);
      setItemIntoLocalStorage("longitude",position.coords.longitude);
      updateGeoStatus("Getting address...");
      if (geocoder) {
        var latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        geocodePosition(latLng);
      }
      updateAddButtons();
      getStoresData();
    } else {
      updateGeoStatus("N/A");
    }
  }

  var geocodePosition=function(pos) {
    if (geocoder ) {
      geocoder.geocode({
        latLng: pos
      }, function(responses) {
        if (responses && responses.length > 0) {
          updateGeoStatus(responses[0].formatted_address);
        } else {
          updateGeoStatus('Cannot determine address at this location.');
        }
      });
    }
  }

  var reorderStoresByDishCountDescending=function() {
    setItemIntoLocalStorage(storesSortBy,"dishCount");
    reorderStores(sortByDishCountDescending);
  }

  var reorderStoresByDistanceAscending=function() {
    setItemIntoLocalStorage(storesSortBy,"distance");
    reorderStores(sortByDistanceAscending);
  }

  var reorderStoresByNameAscending=function() {
    setItemIntoLocalStorage(storesSortBy,"name");
    reorderStores(sortByNameAscending);
  }

  ///////////////////
  // View
  ///////////////////

  var createStoresLayout=function () {
    setPageTitle();
    createStoresNav();
    createStoresSections();   
  }

  var setPageTitle=function () {
    var title=document.getElementById("title");
    removeChildrenFromElement(title);
    title.appendChild(document.createTextNode("Dish Reviews!"));
  }

  var createStoresNav=function() {
    var nav=document.getElementById("nav");  
    removeChildrenFromElement(nav);
    
    // List
    var navUl=document.createElement("ul");
    nav.appendChild(navUl);
    navUl.setAttribute("id","navlist");

    // My Reviews
    var navItem=document.createElement("li");
    navUl.appendChild(navItem);  
    navItem.setAttribute("id","myReviews");
    navItem.setAttribute("style","display:none");
    var navItemLink=document.createElement("a");
    navItem.appendChild(navItemLink);  
    navItemLink.setAttribute("href","/reviewsOwn");
    navItemLink.appendChild(document.createTextNode("My Reviews")); 
    
    // Fb login
    var navItem=document.createElement("li");
    navUl.appendChild(navItem);  
    navItem.setAttribute("id","fblogin");
    navItem.setAttribute("style","display:none");
    var navItemLink=document.createElement("a");
    navItem.appendChild(navItemLink);  
    navItemLink.setAttribute("id","logonLink");
    navItemLink.setAttribute("href","#");
    navItemLink.appendChild(document.createTextNode("Log On"));  
    
    // Fb name
    var navItem=document.createElement("li");
    navUl.appendChild(navItem);  
    navItem.setAttribute("id","fbname");
    navItem.setAttribute("style","display:none");
    navItem.setAttribute("class","nw");
    navItem.innerHTML="<fb:name uid=\"loggedinuser\" useyou=\"false\" linked=\"true\"></fb:name>";
    
    // OffLink
    var navItem=document.createElement("li");
    navUl.appendChild(navItem);  
    navItem.setAttribute("id","offline");
    navItem.setAttribute("style","display:none");
    navItem.setAttribute("class","nw");
    navItem.appendChild(document.createTextNode("Offline"));
    
    // Show 'My Reviews' if logged in
    var myReviews=document.getElementById("myReviews");  
    if (DishRevUser.isLoggedIn) {
       myReviews.style.display='inline';
    } else {
       myReviews.style.display='none';
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
    
    // Vary login link
    var login=document.getElementById("logonLink");
    if (DishRevUser.isLoggedIn) {
      login.innerHTML="Log Off";
      login.removeEventListener('click', DishRev.fbLoginLink, false);  
      login.addEventListener('click', DishRev.fbLogoutLink, false);
    } else {
      login.innerHTML="Log On";
      login.removeEventListener('click', DishRev.fbLogoutLink, false);  
      login.addEventListener('click', DishRev.fbLoginLink, false);
    }
  }

  var createStoresSections=function() {
    var content=document.getElementById("content");
    removeChildrenFromElement(content);
    
    var sectionLocation=document.createElement("section");
    content.appendChild(sectionLocation);
    var geoStatus=document.createElement("span");
    sectionLocation.appendChild(geoStatus);
    geoStatus.setAttribute("id","geoStatus");
    
    var changeLocationLink=document.createElement("a");
    sectionLocation.appendChild(changeLocationLink);
    changeLocationLink.setAttribute("class","nw");
    changeLocationLink.setAttribute("style","margin-left:1em"); 
    changeLocationLink.setAttribute("href","/locationChange");
    changeLocationLink.appendChild(document.createTextNode("Change Location"));
    
    var sectionData=document.createElement("section");
    content.appendChild(sectionData);
    sectionData.setAttribute("class","data");
    sectionData.setAttribute("id","data");
    var waitingForData=document.createElement("progress");
    sectionData.appendChild(waitingForData);
    //waitingForData.setAttribute("style","display:none");
    waitingForData.setAttribute("id","waitingForData");
    waitingForData.setAttribute("title","Waiting for data");
  }

  var displayCachedDataIfExists=function() {
    var xmlDoc=getCachedData();
    if (xmlDoc) {
      displayData(xmlDoc);
    }
  }

  var displayCachedData=function() {
    var xmlDoc=getCachedData();
    if (xmlDoc) {
      displayData(xmlDoc);
    } else {
      displayTableNoCachedData();
    }
  }

  var displayData=function(xmlDoc) {
    // Create HTML table
    var tableDiv=document.getElementById("data");
    var table=createTable();

    var stores=xmlDoc.getElementsByTagName("store"); 
    
    // No stores
    if (stores.length==0){
      table.appendChild(createTableRowForNoData());
      removeChildrenFromElement(tableDiv);
      // Update tableDiv with new table at end of processing to prevent multiple
      // requests from interfering with each other
      tableDiv.appendChild(table);
    }
    // Stores
    else {
      // Make HTML for each store
      for (var i=0;i<stores.length;i++) {
        var store=stores[i];
        table.appendChild(createTableRowForStore(store));
      }
      removeChildrenFromElement(tableDiv);
      // Update tableDiv with new table at end of processing to prevent multiple
      // requests from interfering with each other
      tableDiv.appendChild(table);
      updateLocationDispay();
    }
  }

  var createTable=function() {
    var table=document.createElement("table");
    table.setAttribute("id","stores");
    var tr=document.createElement("tr");
    table.appendChild(tr);

    // Distance
    var thDistance=document.createElement("th");
    tr.appendChild(thDistance);
    var distanceLink=document.createElement("a");
    distanceLink.setAttribute("href","#");
    distanceLink.setAttribute("onclick","Store.reorderStoresByDistanceAscending();return false;");
    distanceLink.appendChild(document.createTextNode("Distance"));
    thDistance.appendChild(distanceLink);

    // Note
    var thName=document.createElement("th");
    tr.appendChild(thName);
    var nameLink=document.createElement("a");
    nameLink.setAttribute("href","#");
    nameLink.setAttribute("onclick","Store.reorderStoresByNameAscending();return false;");
    nameLink.appendChild(document.createTextNode("Name"));
    thName.appendChild(nameLink);

    // Show Add link
    if (DishRevUser.canEdit) {
      var addLink=document.createElement("a");
      addLink.setAttribute("href","/storeAddLocation");
      addLink.setAttribute("class","add addTh");
      addLink.appendChild(document.createTextNode("Add"));
      thName.appendChild(addLink);
    }

    // Dishes
    var thType=document.createElement("th");
    tr.appendChild(thType);
    var typeLink=document.createElement("a");
    typeLink.setAttribute("href","#");
    typeLink.setAttribute("onclick","Store.reorderStoresByDishCountDescending();return false;");
    typeLink.appendChild(document.createTextNode("Dishes"));
    thType.appendChild(typeLink);

    return table;
  }

  var createTableRowForStore=function(store) {
    var tr=document.createElement("tr");
    // Attributes
    var storeId=store.getAttribute("storeId");
    tr.setAttribute("storeId",storeId);
    tr.setAttribute("name",store.getAttribute("text").toLowerCase());
    tr.setAttribute("lat",store.getAttribute("lat"));
    tr.setAttribute("lon",store.getAttribute("lon"));
    //tr.setAttribute("yes",store.getAttribute("yes"));
    tr.setAttribute("dishCount",store.getAttribute("dishCount"));
    
    // Distance and bearing
    tr.appendChild(document.createElement("td"));

    // Desc
    var desc=document.createElement("td");
    var descLink=document.createElement("a");
    descLink.setAttribute("href","#");
    descLink.setAttribute("onclick","Store.linkTo("+storeId+");return false;");
    var text=store.getAttribute("text");
    descLink.appendChild(document.createTextNode(text));
    desc.appendChild(descLink);
    tr.appendChild(desc);

    // Count
    var type=document.createElement("td");
    type.setAttribute("class","center");
    var typeLink=document.createElement("a");
    typeLink.setAttribute("href","#");
    typeLink.setAttribute("onclick","Store.linkTo("+storeId+");return false;");
    typeLink.appendChild(document.createTextNode(store.getAttribute("dishCount")));
    type.appendChild(typeLink);
    tr.appendChild(type);

    return tr;
  }

  var createTableRowForNoCachedData=function() {
    var tr=document.createElement("tr");
    var td=document.createElement("td");
    td.setAttribute("colspan","3");
    td.appendChild(document.createTextNode("No connectivity or cached data.  Please try again later."));
    tr.appendChild(td);
    return tr;
  }

  var createTableRowForNoData=function() {
    var tr=document.createElement("tr");
    var td=document.createElement("td");
    td.setAttribute("colspan","3");
    td.appendChild(document.createTextNode("No nearby restaurants."));
    tr.appendChild(td);
    return tr;
  }

  var displayTableNoCachedData=function() {
    var tableDiv=document.getElementById("data");
    var table=createTable();
    table.appendChild(createTableRowForNoCachedData());
    removeChildrenFromElement(tableDiv);
    tableDiv.appendChild(table);
  }

  var displayLocationError=function(error) {
    updateGeoStatus(locationNotFoundMessage + ": (" + error.code + ") " + error.message);
  }

  var updateGeoStatus=function(text) {
    document.getElementById("geoStatus").innerHTML=text;
  }

  var updateLocationDispay=function() {
    // Current location
    var latitude=parseFloat(localStorage.getItem("latitude"));
    var longitude=parseFloat(localStorage.getItem("longitude"));
    // For each store
    var stores=document.getElementById("stores");
    var storeRows=stores.getElementsByTagName("tr");
    for (var i=1; i<storeRows.length; i++) {
      var store=storeRows[i];
      var storeLat=parseFloat(store.getAttribute("lat"));
      var storeLon=parseFloat(store.getAttribute("lon"));
      var display="";
      // Distance
      var distance=calculateDistance(latitude, longitude, storeLat, storeLon);
      // Save for ordering
      store.setAttribute("distance",distance);
      // Add distance to display
      if (distance<1){
        display=Math.round(distance*1000)+"m";
      } else {
        display=Math.round(distance*10)/10 +"km";
      }
      // Bearing
      var bearingDegrees=calculateBearing(latitude, longitude, storeLat, storeLon);
      display+=" " + getCardinalDirection(bearingDegrees);
      display="<a href='/storeUpdateLocation?storeId=" + store.getAttribute("storeId") + "'>"+display+"</a>";
      // Update direction display
      store.getElementsByTagName("td")[0].innerHTML=display;
    }
    // Sort
    var sortBy=localStorage.getItem(storesSortBy);
    if (sortBy==null || sortBy=="name") {
      reorderStoresByNameAscending();
    } else if (sortBy=="distance") {
      reorderStoresByDistanceAscending();
    } else if (sortBy=="dishCount") {
      reorderStoresByDishCountDescending();
    }
  }

  var getCardinalDirection=function(degrees) {
    var value;
    if (degrees>=22.5 && degrees<=67.5){
      value="NE";
    } else if (degrees>67.5 && degrees<112.5) {
      value="E";
    } else if (degrees>=112.5 && degrees<=157.5) {
      value="SE";
    } else if (degrees>157.5 && degrees<202.5) {
      value="S";
    } else if (degrees>=202.5 && degrees<=247.5) {
      value="SW";
    } else if (degrees>247.5 && degrees<292.5) {
      value="W";
    } else if (degrees>=292.5 && degrees<=337.5) {
      value="NW";
    } else {
      value="N";
    }
    return value;
  }

  var updateAddButtons=function(degrees) {
    var addButtonDisabled=document.getElementById("addButtonDisabled");
    if (addButtonDisabled) {
      addButtonDisabled.style.display='none';
    }
    var addButtonEnabled=document.getElementById("addButtonEnabled");
    if (addButtonEnabled) {
      addButtonEnabled.style.display='inline';
    }
  }

  var showProgressData=function(degrees) {
    var progressData=document.getElementById("progressData");
    if (progressData) {
      progressData.style.display="inline";
    }
  }

  ///////////////////
  // View - Sorting
  ///////////////////

  var reorderStores=function(sortFunction) {
    var stores=document.getElementById("stores");
    var notes=stores.getElementsByTagName("tr");
    var notesTemp=new Array();
    for (var i=1; i<notes.length; i++) {
      notesTemp.push(notes[i]);
    }
    notesTemp.sort(sortFunction);
    for (var i=0; i<notesTemp.length; i++) {
      stores.appendChild(notesTemp[i]);
    }
  }

  var sortByDishCountDescending=function(note1,note2) {
    var dishCount1=parseFloat(note1.getAttribute("dishCount"));
    var dishCount2=parseFloat(note2.getAttribute("dishCount"));
    if (dishCount1>dishCount2) {
        return -1;
    } else if (dishCount2>dishCount1) {
        return 1;
    } else {
        return 0;
    }
  }

  var sortByDistanceAscending=function(note1,note2) {
    var distance1=parseFloat(note1.getAttribute("distance"));
    var distance2=parseFloat(note2.getAttribute("distance"));
    if (distance2>distance1) {
        return -1;
    } else if (distance1>distance2) {
        return 1;
    } else {
        return 0;
    }
  }

  var sortByNameAscending=function(note1,note2) {
    var name1=note1.getAttribute("name");
    var name2=note2.getAttribute("name");
    return name1.localeCompare(name2);
  }

  return {
    display: function() {
      create();
    },
    linkTo: function(aStoreId) {
      DishRev.lock=false;
      var stateObj = { action: "stores" };
      history.pushState(stateObj, "Stores", "/stores" );
      create();
    }
  };
  
})();