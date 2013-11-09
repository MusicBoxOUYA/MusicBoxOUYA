package org.team4977.musicboxouya.media;

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
	
	
	public Song(int id, String title, Album album, Artist artist)
	{
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
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
	
	
	public String toJSON()
	{
		return toJSON(false);
	}
	
	public String toJSON(boolean fullData)
	{
		String output = "{";
		output += "\"id\": "+getID()+",";
		output += "\"title\": \""+getTitle()+"\",";
		if ( fullData )
		{
			output += "\"album\": \""+getAlbum()+"\",";
			output += "\"artist\": \""+getArtist()+"\",";
		}
		output += "\"score\": 0";
		output += "}";
		return output;
	}
}
