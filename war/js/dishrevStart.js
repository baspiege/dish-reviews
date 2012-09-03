///////////////////
// Main controller
///////////////////

dishrev.controller.initialize=function() {
  if (typeof(google)!="undefined") {
    geocoder = new google.maps.Geocoder();
  }

  // Check if logged in
  var dishRevUser=getCookie("dishRevUser");
  dishrev.user.isLoggedIn=false;
  if (dishRevUser!="") {
    dishrev.user.isLoggedIn=true;
  }
  
  // If logged in and online, can edit
  dishrev.user.canEdit=dishrev.user.isLoggedIn && navigator.onLine;
}

dishrev.controller.checkPage=function() {
  dishrev.model.lock=true;
  var qsString=getQueryStrings();
  if (qsString && qsString.storeId) {
    Store.display(qsString.storeId);    
  } else {
    dishrev.stores.controller.create();
  }
}

dishrev.controller.checkPage();

window.onpopstate = function(e) {
  if (dishrev.model.lock) {
    dishrev.model.lock=false;
    return;
  }

  if (e && e.state && e.state.action) { 
    if (e.state.action=="stores") {
      dishrev.stores.controller.create();
    } else if (e.state.action=="store") {   
      dishrev.store.controller.create(e.state.storeId);
    }
  } else {
    dishrev.stores.controller.create();
  }
};