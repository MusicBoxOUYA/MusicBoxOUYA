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
	var name = createElement("h3", {"class":"song-title"}, data.title);
	var p = createElement("p");
	var artist = createElement("span", {"class":"song-artist"}, data.artist);
	var br = createElement("br");
	var album = createElement("span", {"class":"song-album"}, data.album);
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
	var html = table.buildTable(JSON.parse(res));
	insertElementAt(html, parentEle[0]);
}

function buildAlbumList(parentEle, res){
  var albumSongs = [];
	var data = JSON.parse(res);
	for(album in data){
		var li = createElement("li");
		var img = createElement("img", {"src":"resources/cover-art.jpg"});
		var info = createElement("ul", {"class":"no-bullet ul-margin-bottom"});
		var title = createElement("li", {"class":"album-artist"}, data[album].title);
		var artist = createElement("li", {"class":"album-artist"}, data[album].artist);
    insertElementAt(img, li);
    insertElementAt(info, li);
    insertElementAt(title, info);
    insertElementAt(artist, info);
    insertElementAt(li, parentEle[0]);
    albumSongs.push(data[album].songs); 
	}
  window.console&&console.log(albumSongs);
  return albumSongs;
}