function call(){
  request("api/library", "", function(result){
    buildAlbumList($("#album-list"), result);
  }, function(){
    $("#connection-error-alert").modal({
      keyboard: false,
      backdrop:"static"
    }).modal("show");
  });
}

function startCall(){
  window.console&&console.log("Starting Call");
  setTimeout(call, 1000);
}

$( document ).ready(function(){
  startCall();
  $("#connection-error-alert").on("hidden.bs.modal", function () {
    startCall();
  })
});
call();

