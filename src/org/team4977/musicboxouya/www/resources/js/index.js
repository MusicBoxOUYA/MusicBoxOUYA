function call(){
  request("api/nowplaying", "", function(result){
    buildAlbumArt$("#album-art"), result);
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
    setButtonSongId($("#like-button"), result);
    setButtonSongId($("#dislike-button"), result);
  });
  request("api/queue?limit=3", "", function(result){
    buildQueueTable($("#queue"), JSON.parse(result));
  });
}
$( document ).ready(function(){
  call();
  setInterval(call, 1000);
  $("#like-button").click(function(){
    likeSong($(this).data("song-id"));
  });
  $("#dislike-button").click(function(){
    dislikeSong($(this).data("song-id"));
  });
});

