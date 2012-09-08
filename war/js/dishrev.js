
DishRev=new Object();

///////////////////
// Main model
///////////////////

DishRev.lock=false;

///////////////////
// Main view
///////////////////

DishRev.fbLogoutLink=function(event) {
  event.preventDefault(); 
  fbLogout();
}

DishRev.fbLoginLink=function(event) {
  event.preventDefault(); 
  fbLogin();
}