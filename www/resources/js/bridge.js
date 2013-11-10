function request(url, sendData, callBack){
	$.ajax({
		url:url,
		type:"GET",
		data:sendData,
		success:function(result){
			callBack(result)
		}
	})
}

function buildSongInfo(parentEle, res){
	var data = JSON.parse(res);
	var name = createElement("h3", {"class":"song-title"}, data.song.title);
	var p = createElement("p");
	var artist = createElement("span", {"class":"song-artist"}, data.song.artist);
	var br = createElement("br");
	var album = createElement("span", {"class":"song-album"}, data.song.album);
	parentEle.html("");
	insertElementAt(name, parentEle[0]);
	insertElementAt(p, parentEle[0]);
	insertElementAt(artist, p);
	insertElementAt(br, p);
	insertElementAt(album, p);
}

function buildSongTable(parentEle, res){
	var table = new Table(["title", "album", "artist", "likes", "add"], ["Song", "Album", "Artist", "+1", ""]);
  parentEle.html("");
	table.setProperties("table", {width:"100%"});
	table.addColumnProcessor("add", function(data){
		return createElement("button", {"class":"button tiny"}, "Add To Queue");
	});
	var html = table.buildTable(res);
	insertElementAt(html, parentEle[0]);
}

function buildAlbumList(parentEle, res){
  var albumSongs = [];
  var data = JSON.parse(res);
  parentEle.html("");
	for(album in data){
		var li = createElement("li");
    var a = createElement("a", {"href":"#", "data-reveal-id":"album-song-list", "data-album-id":album})
		var img = createElement("img", {"src":"resources/cover-art.jpg"});
		var info = createElement("ul", {"class":"no-bullet ul-margin-bottom"});
		var title = createElement("li", {"class":"album-artist"}, data[album].title);
		var artist = createElement("li", {"class":"album-artist"}, data[album].artist);
    insertElementAt(img, a);
    insertElementAt(a, li);
    insertElementAt(info, li);
    insertElementAt(title, info);
    insertElementAt(artist, info);
    insertElementAt(li, parentEle[0]);
    albumSongs.push(data[album].songs);
    $(a).data("songs", data[album].songs);
    $(a).data("title", data[album].title);
    $(a).click(function(){
      $this = $(this);
      console.log($this.data("songs"));
      $("#album-list-title").html($this.data("title"));
      buildSongTable($("#album-song-table"), $(this).data("songs"));
    });
	}
  return albumSongs;
}
