///////////////////
// Event handlers
///////////////////

function disableAddButton() {
  this.style.display='none';
  document.getElementById('addButtonDisabled').style.display='inline';
}

function cancelAction() {
  window.location='stores';
}

///////////////////
// Set-up Page
///////////////////

function setUpPage() {
  
  // Disable button after submitting
  var addButtonEnabled=document.getElementById("addButtonEnabled");
  addButtonEnabled.onclick=disableAddButton;
  
  // Cancel button action
  var cancelButton=document.getElementById("cancelButton");
  cancelButton.onclick=cancelAction;
}

setUpPage();