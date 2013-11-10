function call(){
  request("api/album.html", "", function(result){
    buildAlbumList($("#album-list"), result);
  });
}
$( document ).ready(function(){
  $(document).foundation();
  call();
  $("#view-album").click(function(){
    $("dd").removeClass("active");
    $(this).parent().addClass("active");
    request("api/album.html", "", function(result){
      buildAlbumList($("#album-list"), result);
      
    });
  });
  $("#view-song").click(function(){
    $("dd").removeClass("active")
    $(this).parent().addClass("active");
    request("api/song-list.html", "", function(result){
      buildSongTable($("#album-list"), JSON.parse(result));
      $(document).foundation();
    });
  });
});
