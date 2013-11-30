var callID;
var currentSong;

//called every second to determine 
function call(){
  request("api/nowplaying", "", function(result){
    var data = JSON.parse(result);
    var test = currentSong =! null ? currentSong : data.song.id;
    if(test == data.song.id) { // same song
      onSongRefresh(data);
    }
    else { // new song
      onSongChange(data);
      onSongRefresh(data);
    }
    
  }, function(data){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
  request("api/queue?limit=3", "", function(result){
    var data = JSON.parse(result);
    onQueueRefresh(data);
  }, function(){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
}

function onStart() {
  $("#album-art").removeClass("loading");
}

function onStop() {
  $("#album-art").addClass("loading");
}

function onSongRefresh(data) {
  buildSongTime($(".song-progress"), data);
}

function onSongChange(data) {
  buildAlbumArt($("#album-art"), result);
  buildSongInfo($("#song-info"), result);
  setColor($(".color-1"), $("#album-art"));
  setButtonSongId($("#like-button"), result);
  setButtonSongId($("#dislike-button"), result);
  
}

function onQueueRefresh(data) {
  buildQueueTable($("#queue"), JSON.parse(result));
}

function startCall(){
  window.console&&console.log("Starting Call");
  onStart();
  callID = setInterval(call, 1000);
}

function stopCall(){
  window.console&&console.log("Stopping Call");
  onStop();
  clearInterval(callID);
}

$( document ).ready(function(){
  call();
  startCall();
  $("#connection-error-alert").on("hidden.bs.modal", function () {
    startCall();
  })
  $("#like-button").click(function(){
    likeSong($(this).data("song-id"));
    $(this).attr("disabled", "disabled").delay(10000).queue(function(next){
      $(this).removeAttr("disabled");
      next();
    });
  });
  $("#dislike-button").click(function(){
    dislikeSong($(this).data("song-id"));
    $(this).attr("disabled", "disabled").delay(10000).queue(function(next){
      $(this).removeAttr("disabled");
      next();
    });
  });
});

