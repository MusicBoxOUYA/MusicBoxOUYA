package org.team4977.musicboxouya.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Artist {
	String name;
	HashMap<String, Album> albums = new HashMap<String,Album>();
	public Artist(String name)
	{
		this.name = name;
	}
	
	public Album addAlbum(String title)
	{
		Album a = albums.get(title);
		if ( a != null )
			return a;
		a = new Album(title, this);
		albums.put(title, a);
		return a;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Album[] getAlbums()
	{
		return albums.values().toArray(new Album[albums.size()]);
	}
	
	public Song[] getSongs()
	{
		ArrayList<Song> songs = new ArrayList<Song>();
		Iterator<Album> it = albums.values().iterator();
		while ( it.hasNext() )
		{
			Song[] thisAlbumSongs = it.next().getSongs();
			for ( int a = 0; a < thisAlbumSongs.length; a++ )
				songs.add(thisAlbumSongs[a]);
		}
		return (Song[])songs.toArray();
	}
}
