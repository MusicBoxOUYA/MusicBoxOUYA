package org.team4977.musicboxouya.media;

import java.util.ArrayList;

import org.team4977.musicboxouya.database.library.ArtworkGetter;

public class Album {
	String name;
	Artist artist;
	String artwork = "";
	ArrayList<Song> songs = new ArrayList<Song>();
	
	public Album(String name, Artist artist)
	{
		this.name = name;
		this.artist = artist;
		this.artwork = ArtworkGetter.getArtwork(artist.getName(), name);
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
	
	public String getArtworkURL()
	{
		return artwork;
	}
	
	public String toJSON()
	{
		String output = "{";
		output += "\"title\": \""+getName()+"\",";
		output += "\"artist\": \""+artist.getName()+"\",";
		output += "\"art\": \""+getArtworkURL()+"\",";
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
