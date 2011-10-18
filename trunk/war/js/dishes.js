
///////////////////
// Asynch
///////////////////

var xmlHttpRequest=new XMLHttpRequest();

function sendRequest(url,callback,postData) {
  var req = xmlHttpRequest;
  if (!req) return;
  var method = (postData) ? "POST" : "GET";
  req.open(method,url,true);
  req.setRequestHeader('User-Agent','XMLHTTP/1.0');
  if (postData)
    req.setRequestHeader('Content-type','application/x-www-form-urlencoded');
  req.onreadystatechange = function () {
    if (req.readyState != 4) return;
    if (req.status != 200 && req.status != 304) {
      // alert('HTTP error ' + req.status);
      return;
    }
    if (callback){      
      callback(req);
    }
  }
  if (req.readyState == 4) return;
  req.send(postData);
}

///////////////////
// Votes
///////////////////

function handleYesVote(req) {
  var xmlDoc=req.responseXML;
  var error=xmlDoc.getElementsByTagName("error");
  if (error.length>0){
    var message=error[0].getAttribute("message");
    alert(message);
  }
}

function sendYesVote(elem,id) {
  var yes=parseInt(elem.innerHTML);
  elem.innerHTML=yes+1;
  sendRequest('dishVote.jsp?vote=yes&dishId='+id,handleYesVote);
}

///////////////////
// Sorting
///////////////////

function reorderDishes(sortFunction) {
  var dishes=document.getElementById("dishes");
  var notes=dishes.getElementsByTagName("tr");
  var notesTemp=new Array();
  for (var i=1; i<notes.length; i++) {
    notesTemp.push(notes[i]);
  }
  notesTemp.sort(sortFunction);
  for (var i=0; i<notesTemp.length; i++) {
    dishes.appendChild(notesTemp[i]);
  }
}

function sortByNameAscending(note1,note2) {
  var name1=note1.getAttribute("name");
  var name2=note2.getAttribute("name");
  return name1.localeCompare(name2);
}

function sortByReviewCountDescending(note1,note2) {
  var reviewCount1=parseFloat(note1.getAttribute("reviewCount"));
  var reviewCount2=parseFloat(note2.getAttribute("reviewCount"));
  if (reviewCount1>reviewCount2) {
      return -1;
  } else if (reviewCount2>reviewCount1) {
      return 1;
  } else {
      return 0;
  }
}

function sortByVoteYesDescending(note1,note2) {
  var vote1=parseInt(note1.getAttribute("yes"));
  var vote2=parseInt(note2.getAttribute("yes"));
  if (vote1>vote2) {
      return -1;
  } else if (vote2>vote1) {
      return 1;
  } else {
      return 0;
  }
}

function reorderDishesByNameAscending() {
  //setCookie("sortBy","name");
  reorderDishes(sortByNameAscending);
}

function reorderDishesByReviewCountDescending() {
  //setCookie("sortBy","reviewCount");
  reorderDishes(sortByReviewCountDescending);
}

function reorderDishesByVoteYesDescending() {
  //setCookie("sortBy","voteYes");
  reorderDishes(sortByVoteYesDescending);
}
