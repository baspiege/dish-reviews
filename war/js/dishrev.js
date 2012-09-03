
dishrev=new Object();
dishrev.model=new Object();
dishrev.view=new Object();
dishrev.controller=new Object();

///////////////////
// Main model
///////////////////

dishrev.model.lock=false;

///////////////////
// Main view
///////////////////

dishrev.view.fbLogoutLink=function(event) {
  event.preventDefault(); 
  fbLogout();
}

dishrev.view.fbLoginLink=function(event) {
  event.preventDefault(); 
  fbLogin();
}