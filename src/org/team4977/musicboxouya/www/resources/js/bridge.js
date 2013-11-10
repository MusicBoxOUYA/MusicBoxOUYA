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

function likeSong(id){
  request("api/like", "song="+id, function(data){
    console.log("Liking song");
  });
}

function dislikeSong(id){
  request("api/dislike", "song="+id, function(data){
    console.log("Dis;liking song");
  });
}

function queueSong(id){
  request("api/queue", "song="+id, function(data){
    console.log(JSON.parse(data))
  });
}

function buildAlbumArt(imgEle, res){
  var data = JSON.parse(res);
  imgEle.attr("src", "/api/art?song="+data.song.id);
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
  insertElementAt(album, p);
  insertElementAt(br, p);
  insertElementAt(artist, p);
}

function buildSongTime(parentEle, res){
    var data = JSON.parse(res);
    var currentMinutes = Math.floor((data.position/1000)/60);
    var currentSeconds = Math.floor((data.position/1000)%60);
    if(currentSeconds < 10){
      currentSeconds = "0" + currentSeconds;
    }
    var durationMinutes = Math.floor((data.duration/1000)/60);
    var durationSeconds = Math.floor((data.duration/1000)%60);
    if(durationSeconds < 10){
      durationSeconds = "0" + durationSeconds;
    }
    var currentTimeParent = createElement("div", {"class":"small-1 columns"});
  var currentTime = createElement("span", {"class":""}, currentMinutes + ":" + currentSeconds);
  var durationTime = createElement("span", {"class":""}, durationMinutes + ":" + durationSeconds);
  var percent = ((data.position/data.duration)*100);
  var progressPercent = createElement("span", {"class":"meter","style":"width: " + percent + "%"});
  parentEle.html("");
  insertElementAt(currentTime,parentEle[0]);
  insertElementAt(progressPercent,parentEle[1]);
  insertElementAt(durationTime,parentEle[2]);
}

function buildUpNext(parentEle, res){
  var data = JSON.parse(res);
  var p = createElement("p");
  var title = createElement("b", null, "Up Next: ");
  var artist = createElement("span", null, data[0].title);
  var album = createElement("span", null, data[0].artist); 
  var dash = createText(" - ");
  parentEle.html("");
  insertElementAt(title, p);
  insertElementAt(artist, p);
  insertElementAt(dash, p);
  insertElementAt(album, p);
  if(data.length != 0)
    insertElementAt(p, parentEle[0]);
}

function setButtonSongId(parentEle, res){
  var data = JSON.parse(res);
  parentEle.data("song-id", data.song.id);
}

function buildQueueTable(parentEle, res){
  var table = new Table(["order", "title", "album", "artist", "score"], ["#", "Song", "Album", "Artist", "+1"]);
  parentEle.html("");
  var counter = 1;
  table.addAdvancedColumnProcessor("order", function(data){
    return counter++;
  });
  table.setProperties("table", {width:"100%"});
  var html = table.buildTable(res);
  insertElementAt(html, parentEle[0]);
}

function buildSongTable(parentEle, res){
  var table = new Table(["title", "album", "artist", "score", "add"], ["Song", "Album", "Artist", "+1", ""]);
  parentEle.html("");
  table.setProperties("table", {width:"100%"});
  table.addAdvancedColumnProcessor("add", function(data){
    button = createElement("button", {"class":"button tiny"}, "Add To Queue");
    $(button).click(function(){
      $(this).attr("disabled", "disabled").delay(10000).queue(function(next){
        $(this).removeAttr("disabled");
        next();
      });
      console.log("queuing song " + data["id"]);
      queueSong(data["id"]);
    })
    return button;
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
    var img = createElement("img", {"src":"/api/art?song="+data[album].songs[0].id});
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
      $("#album-list-title").html($this.data("title"));
      buildSongTable($("#album-song-table"), $(this).data("songs"));
    });
  }
  return albumSongs;
}
