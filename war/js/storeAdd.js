///////////////////
// Event handlers
///////////////////

function disableButton() {
  this.style.display='none';
  document.getElementById('addButtonDisabled').style.display='inline';
}

///////////////////
// Set-up Page
///////////////////

function setUpPage() {
  
  // Update submit button
  var addButtonEnabled=document.getElementById("addButtonEnabled");
  addButtonEnabled.onclick=disableButton;
}

setUpPage();