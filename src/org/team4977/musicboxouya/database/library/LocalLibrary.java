package org.team4977.musicboxouya.database.library;

import java.io.File;
import java.io.FileInputStream;
import org.team4977.musicboxouya.media.Album;
import org.team4977.musicboxouya.media.Artist;
import org.team4977.musicboxouya.media.Song;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public class LocalLibrary extends LibraryProvider {
	
	String path;
	public LocalLibrary(String path)
	{
		this.path = path;
	}
	
	protected void processDirectory(String dir)
	{
		System.out.println("Processing "+dir);
		
		File[] fileList = new File(dir).listFiles();
		for ( int i = 0; i < fileList.length; i++ )
		{
			if ( fileList[i].isDirectory() )
				processDirectory(fileList[i].getAbsolutePath());
			else if ( fileList[i].isFile() && fileList[i].canRead() && fileList[i].getName().endsWith(".mp3") )
			{
				MediaMetadataRetriever metadata = new MediaMetadataRetriever();
				System.out.println("Reading "+fileList[i].getAbsolutePath());
				try {
					metadata.setDataSource(new FileInputStream(fileList[i]).getFD());
					String artistName = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
					if ( artistName == null )
						artistName = "Unknown Artist";
					
					String albumName = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
					if ( albumName == null )
						albumName = "Unknown Album";
					
					Artist artist = addArtist(artistName);
					
					Album album = artist.addAlbum(albumName);
					
					String title = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
					int songID = Song.getNextID();
					
					Song s = new Song(songID, title, album, artist, fileList[i].getAbsolutePath());
					album.addSong(s);
					addSong(s);
					
					metadata.release();
				} catch (Exception e) {
					System.out.println("Metadata get failed for "+fileList[i].getAbsolutePath());
				}
			}
		}
	}
	
	protected void doLibraryPopulate()
	{
		processDirectory(path);
		System.out.println("DONE LIBRARY");
	}

	@Override
	public Bitmap getArtForSong(Song s) {
		byte[] picture = getRawArtForSong(s);
		Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
		return bitmap;
	}

	@Override
	public byte[] getRawArtForSong(Song s) {
		MediaMetadataRetriever metadata = new MediaMetadataRetriever();
		metadata.setDataSource(s.getPath());
		byte[] picture = metadata.getEmbeddedPicture();
		metadata.release();
		return picture;
	}

}
