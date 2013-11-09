package org.team4977.musicboxouya.media;

public class Song {
	String title;
	Artist artist;
	Album album;
	public Song(String title, Artist artist, Album album)
	{
		this.title = title;
		this.artist = artist;
		this.album = album;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Artist getArtist()
	{
		return artist;
	}
	
	public Album getAlbum()
	{
		return album;
	}
}
