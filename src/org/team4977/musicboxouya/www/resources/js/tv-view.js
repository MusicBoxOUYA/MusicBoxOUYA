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
  request("api/getIP", "", function(result)
  {
	  $("#connection-info").text("Visit http://"+result+":8080/ to queue songs");
  });
  
  setInterval(call, 1000);
});

