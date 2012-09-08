///////////////////
// Main controller
///////////////////

DishRev.initialize=function() {
  if (typeof(google)!="undefined") {
    geocoder = new google.maps.Geocoder();
  }

  // Check if logged in
  var user=getCookie("dishRevUser");
  DishRevUser.isLoggedIn=false;
  if (user!="") {
    DishRevUser.isLoggedIn=true;
  }
  
  // If logged in and online, can edit
  DishRevUser.canEdit=DishRevUser.isLoggedIn && navigator.onLine;
}

DishRev.checkPage=function() {
  DishRevUser.lock=true;
  var qsString=getQueryStrings();
  if (qsString && qsString.storeId) {
    Store.display(qsString.storeId);    
  } else {
    Stores.display();
  }
}

DishRev.checkPage();

window.onpopstate = function(e) {
  if (DishRev.lock) {
    DishRev.lock=false;
    return;
  }

  if (e && e.state && e.state.action) { 
    if (e.state.action=="stores") {
      Stores.display();
    } else if (e.state.action=="store") {
      Store.display(e.state.storeId);
    }
  } else {
    Stores.display();
  }
};