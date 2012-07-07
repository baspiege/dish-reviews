///////////////////
// Event handlers
///////////////////

function disableAddButton() {
  this.style.display='none';
  document.getElementById('addButtonDisabled').style.display='inline';
}

///////////////////
// Set-up Page
///////////////////

function setUpPage() {
  
  // Update submit button
  var addButtonEnabled=document.getElementById("addButtonEnabled");
  addButtonEnabled.onclick=disableAddButton;
}

setUpPage();