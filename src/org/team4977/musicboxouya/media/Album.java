package org.team4977.musicboxouya.media;

import java.util.ArrayList;

public class Album {
	String name;
	Artist artist;
	ArrayList<Song> songs = new ArrayList<Song>();
	
	public Album(String name, Artist artist)
	{
		this.name = name;
		this.artist = artist;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addSong(Song s)
	{
		songs.add(s);
	}
	
	public Song[] getSongs()
	{
		return songs.toArray(new Song[songs.size()]);
	}
	
	public String toJSON()
	{
		String output = "{";
		output += "\"title\": \""+getName()+"\",";
		output += "\"artist\": \""+artist.getName()+"\",";
		output += "\"songs\":[";
		Song[] songs = getSongs();
		for ( int i = 0; i < songs.length; i++ )
		{
			output += songs[i].toJSON(true);
			if ( i != songs.length-1 )
				output += ",";
		}
		
		output += "]}";
		return output;
	}
}
