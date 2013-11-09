package org.team4977.musicboxouya.database.library;

import org.team4977.musicboxouya.media.Album;
import org.team4977.musicboxouya.media.Artist;
import org.team4977.musicboxouya.media.Song;

public abstract class LibraryProvider {
	public LibraryProvider()
	{
		
	}
	
	
	public abstract Song[] getSongs();
	public abstract Artist[] getArtists();
	public abstract Album[] getAlbums();
}
