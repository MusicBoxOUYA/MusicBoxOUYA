function call(){
  request("api/playing.html", "", function(result){
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
  });
  request("api/queue-list.html", "", function(result){
    buildQueueTable($("#queue"), JSON.parse(result));
  });
}
$( document ).ready(function(){
	$("#likeButton").click(function(){
    
  });
  $("#dislikeButton").click(function(){

  });

  setInterval(call, 1000);
});
call();