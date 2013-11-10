function call(){
  request("api/playing.html", "", function(result){
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
    setButtonSongId($("#like-button"), result);
    setButtonSongId($("#dislike-button"), result);
  });
  request("api/queue-list.html", "", function(result){
    buildQueueTable($("#queue"), JSON.parse(result));
  });
}
$( document ).ready(function(){
  setInterval(call, 1000);
  $("#like-button").click(function(){
    likeSong($(this).data("song-id"));
  });
  $("#dislike-button").click(function(){
    dislikeSong($(this).data("song-id"));
  });
});
call();
