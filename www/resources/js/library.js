function call(){
  request("api/album.html", "", function(result){
    buildAlbumList($("#album-list"), result);
    $(document).foundation();
  });
}
$( document ).ready(function(){
});
call();
