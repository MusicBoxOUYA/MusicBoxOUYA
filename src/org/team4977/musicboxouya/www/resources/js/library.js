function call(){
  request("api/library", "", function(result){
    buildAlbumList($("#album-list"), result);
  });
}
$( document ).ready(function(){});
call();
