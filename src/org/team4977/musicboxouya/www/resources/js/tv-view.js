function call(){
  request("api/nowplaying", "", function(result){
	buildAlbumArt($("#album-art"), result);
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
  });
  request("api/queue?limit=3", "", function(result){
    buildQueueTable($("#queue"), JSON.parse(result));
  });
}
$( document ).ready(function(){
  call();
  setInterval(call, 1000);
});

