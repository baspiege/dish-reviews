
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
  sendRequest('reviewVote.jsp?vote=yes&reviewId='+id,handleYesVote);
}

///////////////////
// Sorting
///////////////////

function reorderReviews(sortFunction) {
  var reviews=document.getElementById("reviews");
  var notes=reviews.getElementsByTagName("tr");
  var notesTemp=new Array();
  for (var i=1; i<notes.length; i++) {
    notesTemp.push(notes[i]);
  }
  notesTemp.sort(sortFunction);
  for (var i=0; i<notesTemp.length; i++) {
    reviews.appendChild(notesTemp[i]);
  }
}

function sortByNameAscending(note1,note2) {
  var name1=note1.getAttribute("name");
  var name2=note2.getAttribute("name");
  return name1.localeCompare(name2);
}

function sortByTimeDescending(note1,note2) {
  var time1=parseFloat(note1.getAttribute("time"));
  var time2=parseFloat(note2.getAttribute("time"));
  if (time1>time2) {
      return -1;
  } else if (time2>time1) {
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

function reorderReviewsByNameAscending() {
  // setCookie("sortBy","name");
  reorderStores(sortByNameAscending);
}

function reorderReviewsByTimeDescending() {
  // setCookie("sortBy","time");
  reorderReviews(sortByTimeDescending);
}

function reorderReviewsByVoteYesDescending() {
  // setCookie("sortBy","voteYes");
  reorderReviews(sortByVoteYesDescending);
}

function getElapsedTime(oldSeconds,newSeconds){
  var display="";
  var seconds=newSeconds-oldSeconds;
  if (seconds<60){
    display=Math.round(seconds)+" sec";
  } else {
    var minutes=seconds/60;
    if (minutes<60) {
      display=Math.round(minutes)+" min";
    } else {
      var hours=minutes/60;
      if (hours<24) {
        display=Math.round(hours)+" hr";
      } else {
        var days=hours/24;
        display=Math.round(days)+" days";
      }
    }
  }
  return display;
}