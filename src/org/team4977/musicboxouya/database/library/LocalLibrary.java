package org.team4977.musicboxouya.database.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.team4977.musicboxouya.media.Album;
import org.team4977.musicboxouya.media.Artist;
import org.team4977.musicboxouya.media.Song;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;

public class LocalLibrary extends LibraryProvider {
	
	String path;
	public LocalLibrary(Context context, String path)
	{
		super(context);
		this.path = path;
	}
	
	public boolean canRead()
	{
		File p = new File(path);
		return p.exists() && p.canRead();
	}
	
	public void informationPrompt()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(context).setTitle("Setup Information").setMessage("Insert a USB drive that contains a folder named \"Music\".").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				refresh();
			}
		});
		alert.show();
	}
	
	protected boolean isSupportedFile(File f)
	{
		String filename = f.getName();
		return filename.endsWith(".mp3") || filename.endsWith(".m4a") || filename.endsWith(".3gp") || filename.endsWith(".aac") || filename.endsWith(".flac") || filename.endsWith(".ogg") || filename.endsWith(".wav");
	}
	
	protected void processDirectory(String dir)
	{
		System.out.println("Processing "+dir);
		
		File[] fileList = new File(dir).listFiles();
		for ( int i = 0; i < fileList.length; i++ )
		{
			if ( fileList[i].isDirectory() )
				processDirectory(fileList[i].getAbsolutePath());
			else if ( fileList[i].isFile() && fileList[i].canRead() && isSupportedFile(fileList[i]) )
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
					if ( title == null || title == "null" )
						title = fileList[i].getName();
					
					int songID = Song.getNextID();
					Song s = new Song(songID, title, album, artist, fileList[i].getAbsolutePath(), album.getArtworkURL());
					album.addSong(s);
					addSong(s);
					
					metadata.release();
				} catch (Exception e) {
					System.out.println("Metadata get failed for "+fileList[i].getAbsolutePath());
				}
			}
		}
	}
	
	public void generateCache()
	{
		Iterator<Song> it = songs.values().iterator();
		JSONArray output = new JSONArray();
		while (it.hasNext())
		{
			try {
				JSONObject thisSong = new JSONObject(it.next().toJSON());
				output.put(thisSong);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter cacheWriter = new FileWriter(path+"/MusicBoxLibrary.json");
			cacheWriter.write(output.toString());
			cacheWriter.flush();
			cacheWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void doLibraryPopulate()
	{
		File f = new File(path+"/MusicBoxLibrary.json");
		if ( f.exists() )
		{
			System.out.println("Using cached library");
			try {
				FileReader reader = new FileReader(f);
				String jsonData = IOUtils.toString(reader);
				
				JSONArray json = new JSONArray(jsonData);
				for ( int i = 0; i < json.length(); i++ )
				{
					JSONObject song = json.getJSONObject(i);
					Artist artist = addArtist(song.getString("artist"));
					Album album = artist.addAlbum(song.getString("album"), song.getString("art"));
					int songID = Song.getNextID();
					Song s = new Song(songID, song.getString("title"), album, artist, song.getString("path"), album.getArtworkURL());
					album.addSong(s);
					addSong(s);
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
				f.delete();
				System.out.println("Cached library read failed; Using indexer");
				processDirectory(path);
			}
			
		}
		else
		{
			System.out.println("Using indexer to populate library");
			processDirectory(path);
			generateCache();
		}
	}

	@Override
	public void resetCache() {
		File f = new File(path+"/MusicBoxLibrary.json");
		if ( f.exists() )
			f.delete();
	}

}
