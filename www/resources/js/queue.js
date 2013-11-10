function call(){
  request("api/queue.html", "", function(result){
    buildQueueTable($("#queue-list"), JSON.parse(result));
  });
}
$( document ).ready(function(){
  setInterval(call, 1000);
});
call();