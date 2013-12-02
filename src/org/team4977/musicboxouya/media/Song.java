	package org.team4977.musicboxouya.media;

import org.team4977.musicboxouya.database.library.LibraryProvider;

import android.graphics.Bitmap;

public class Song {
	
	public static int NEXT_ID = 0;
	public static int getNextID()
	{
		int i = NEXT_ID;
		NEXT_ID++;
		return i;
	}
	
	int id;
	String title;
	Artist artist;
	Album album;
	String path;
	String artURL;
	int likes;
	
	public Song(int id, String title, Album album, Artist artist, String path, String artURL)
	{
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.path = path;
		this.artURL = artURL;
		this.likes = 0;
	}
	
	public int getID()
	{
		return id;
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
	
	public String getPath()
	{
		return path;
	}
	
	public void like()
	{
		likes++;
	}
	
	public void dislike()
	{
		likes--;
	}
	
	public int getLikes()
	{
		return likes;
	}
	
	public String getArtworkURL()
	{
		return artURL;
	}
	
	public String toJSON()
	{
		return toJSON(true);
	}
	
	
	public String toJSON(boolean fullData)
	{
		String output = "{";
		output += "\"id\": "+getID()+",";
		output += "\"title\": \""+getTitle()+"\",";
		if ( fullData )
		{
			output += "\"album\": \""+getAlbum().getName()+"\",";
			output += "\"artist\": \""+getArtist().getName()+"\",";
		}
		output += "\"score\": "+getLikes()+",";
		output += "\"path\": \""+getPath()+"\",";
		output += "\"art\": \""+getArtworkURL()+"\"";
		output += "}";
		return output;
	}
}
