package org.team4977.musicboxouya.media;

import java.util.ArrayList;

public class Album {
	Artist artist;
	ArrayList<Song> songs = new ArrayList<Song>();
	
	public Album(Artist artist)
	{
		this.artist = artist;
	}
	
	public void addSong(Song s)
	{
		songs.add(s);
	}
	
	public Song[] getSongs()
	{
		return (Song[])songs.toArray();
	}
}
