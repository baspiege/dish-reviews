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
  DishRev.lock=true;
  var qsString=getQueryStrings();
  if (qsString) {
    if (qsString.storeId) {
      Store.display(qsString.storeId);    
    } else if (qsString.dishId && qsString.reviewId) {
      Dish.display(qsString.dishId, qsString.reviewId);
    } else if (qsString.dishId) {
      Dish.display(qsString.dishId);
    }
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
    } else if (e.state.action=="dish") {
      Dish.display(e.state.dishId);
    } else if (e.state.action=="review") {
      Dish.display(e.state.dishId, e.state.reviewId);
    }
  } else {
    Stores.display();
  }
};