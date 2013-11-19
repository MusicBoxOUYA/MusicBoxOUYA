var callID;

function call(){
  request("api/nowplaying", "", function(result){
    $("#album-art").removeClass("loading");
    buildAlbumArt($("#album-art"), result);
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
    setButtonSongId($("#like-button"), result);
    setButtonSongId($("#dislike-button"), result);
  }, function(data){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
  request("api/queue?limit=3", "", function(result){
    buildQueueTable($("#queue"), JSON.parse(result));
  }, function(){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
}

function startCall(){
  window.console&&console.log("Starting Call");
  callID = setInterval(call, 1000);
  
}

function stopCall(){
  window.console&&console.log("Stopping Call");
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

