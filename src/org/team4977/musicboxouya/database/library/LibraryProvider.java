package org.team4977.musicboxouya.database.library;

import java.util.ArrayList;
import java.util.HashMap;
import org.team4977.musicboxouya.media.Album;
import org.team4977.musicboxouya.media.Artist;
import org.team4977.musicboxouya.media.Song;

import android.os.AsyncTask;

public abstract class LibraryProvider {

	protected static LibraryProvider initalizedProvider = null;
	
	public static LibraryProvider getInitalizedProvider()
	{
		return initalizedProvider;
	}
	
	HashMap<String, Artist> artists = new HashMap<String, Artist>();
	// Map for "globally unique" IDs
	HashMap<Integer, Song> songs = new HashMap<Integer, Song>();
	LibraryRefreshFinishedListener refreshDoneListner = null;
	
	public LibraryProvider()
	{
		initalizedProvider = this;
	}
	
	public void setRefreshFinishedListener(LibraryRefreshFinishedListener listener)
	{
		refreshDoneListner = listener;
	}
	
	protected abstract void doLibraryPopulate();
	
	public void refresh()
	{
		new AsyncTask<Void, Void, Boolean>()
		{

			@Override
			protected Boolean doInBackground(Void... params) {
				doLibraryPopulate();
				
				return true;
			}
			
			protected void onPostExecute(Boolean result)
			{
				System.out.println("really done");
				refreshDoneListner.libraryRefreshFinished(LibraryProvider.this);
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
		return songs.values().toArray(new Song[songs.size()]);
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
			Album[] albums = artist.getAlbums();
			for ( int i = 0; i < albums.length; i++ )
			{
				output += albums[i].toJSON();
				if ( a != artists.size()-1 )
					output += ",";
			}
		}
		output += "]";
		return output;
	}
	
	
}
