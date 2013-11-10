function call(){
  request("api/queue", "", function(result){
    buildQueueTable($("#queue-list"), JSON.parse(result));
  });
}
$( document ).ready(function(){
  call();
  setInterval(call, 1000);
});
