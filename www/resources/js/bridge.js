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

function buildTable(parentEle, res){
	
	var data = JSON.parse(res);
	var table = createElement("table");
	
	insertElementAt(table, parentEle);
}
