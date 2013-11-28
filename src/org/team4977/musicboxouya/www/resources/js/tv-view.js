var callID;

function call(){  
  request("api/nowplaying", "", function(result){
    buildAlbumArt($("#album-art"), result);
    buildSongInfo($("#song-info"), result);
    buildSongTime($(".song-progress"), result);
    setColor($(".color-1"), $("#album-art"));
  }, function(data){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
  request("api/queue?limit=1", "", function(result){
    buildUpNext($("#up-next"), result);
  }, function(data){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
  request("api/getIP", "", function(result){
    $("#connection-info").text("Visit http://"+result+":8080/ to queue songs");
  }, function(data){
    stopCall();
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
}

function startCall(){
  window.console&&console.log("Starting Call");
  setTimeout(function(){
    $("#album-art").removeClass("loading");
  },1000);
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
  });
});

