package org.team4977.musicboxouya.database.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.team4977.musicboxouya.media.Album;
import org.team4977.musicboxouya.media.Artist;
import org.team4977.musicboxouya.media.Song;

import android.os.AsyncTask;

public abstract class LibraryProvider {
	/* Remove this if we end up not needing a singleton
	protected static LibraryProvider initalizedProvider = null;
	
	public static LibraryProvider getInitalizedProvider()
	{
		return initalizedProvider;
	}
	*/
	
	HashMap<String, Artist> artists = new HashMap<String, Artist>();
	// Map for "globally unique" IDs
	HashMap<Integer, Song> songs = new HashMap<Integer, Song>();
	
	public LibraryProvider()
	{
		//initalizedProvider = this;
	}
	
	protected abstract void doLibraryPopulate();
	
	public void refresh()
	{
		new AsyncTask<Void, Void, Void>()
		{

			@Override
			protected Void doInBackground(Void... params) {
				doLibraryPopulate();
				return null;
			}

		}.execute();
	}
	
	public Artist addArtist(String name)
	{
		Artist a = getArtist(name);
		if ( a != null )
			return a;
		a = new Artist(name);
		artists.put(name, a);
		return a;
	}
	
	public Artist getArtist(String name)
	{
		return artists.get(name);
	}
	
	public void addSong(Song s)
	{
		songs.put(s.getID(), s);
	}
	
	public Song getSong(int id)
	{
		return songs.get(id);
	}
	

	public Song[] getSongs()
	{
		Album[] albums = getAlbums();
		ArrayList<Song> songs = new ArrayList<Song>();
		for ( int i = 0; i < albums.length; i++ )
		{
			Song[] thisAlbumsSongs = albums[i].getSongs();
			for ( int a = 0; a < thisAlbumsSongs.length; a++ ) 
			{
				songs.add(thisAlbumsSongs[a]);
			}
		}
		return (Song[])songs.toArray();
	}
	
	public Album[] getAlbums()
	{
		Artist[] artists = getArtists();
		ArrayList<Album> albums = new ArrayList<Album>();
		for ( int i = 0; i < artists.length; i++ )
		{
			Album[] thisArtistsAlbums = artists[i].getAlbums();
			for ( int a = 0; a < thisArtistsAlbums.length; a++ ) 
			{
				albums.add(thisArtistsAlbums[a]);
			}
		}
		return (Album[])albums.toArray();
	}
	
	public Artist[] getArtists()
	{
		return artists.values().toArray(new Artist[artists.size()]);
	}
	
	public String toJSON()
	{
		String output = "[";
		Artist[] artistValues = artists.values().toArray(new Artist[artists.size()]);
		for ( int a = 0; a < artists.size(); a++ )
		{
			Artist artist = artistValues[a];
			output += "{";
			output += "\"artist\": \""+artist.getName()+"\",";
			output += "\"albums\": [";
			Album[] albums = artist.getAlbums();
			for ( int i = 0; i < albums.length; i++ )
			{
				output += albums[i].toJSON();
				if ( i != albums.length-1 )
					output += ",";
			}
			output += "]}";
			if ( a != artists.size()-1 )
				output += ",";
		}
		output += "]";
		return output;
	}
	
	
}
