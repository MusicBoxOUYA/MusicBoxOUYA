function call(){
  request("api/playing.html", "", function(result){
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
  });
  request("api/queue.html", "", function(result){
    buildQueueTable($("#queue"), JSON.parse(result));
  });
}
$( document ).ready(function(){
  setInterval(call, 1000);
});
call();