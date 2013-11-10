function call(){
  request("api/playing.html", "", function(result){
    buildSongInfo($("#song-info"), result);
  });
  request("api/song-list.html", "", function(result){
    buildSongTable($("#queue"), result);
  });
}
$( document ).ready(function(){
  setInterval(call, 1000);
});
call();