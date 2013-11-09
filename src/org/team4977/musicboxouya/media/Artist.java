package org.team4977.musicboxouya.media;

import java.util.ArrayList;
import java.util.Iterator;

public class Artist {
	String name;
	ArrayList<Album> albums;
	public Artist(String name)
	{
		this.name = name;
	}
	
	public void addAlbum(Album a)
	{
		albums.add(a);
	}
	
	public Album[] getAlbums()
	{
		return (Album[])albums.toArray();
	}
	
	public Song[] getSongs()
	{
		ArrayList<Song> songs = new ArrayList<Song>();
		Iterator<Album> it = albums.iterator();
		while ( it.hasNext() )
		{
			Song[] thisAlbumSongs = it.next().getSongs();
			for ( int a = 0; a < thisAlbumSongs.length; a++ )
				songs.add(thisAlbumSongs[a]);
		}
		return (Song[])songs.toArray();
	}
}
