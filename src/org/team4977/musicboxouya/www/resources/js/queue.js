function call(){
  request("api/queue", "", function(result){
    buildQueueTable($("#queue-list"), JSON.parse(result));
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
});
