package org.team4977.musicboxouya;

import java.io.IOException;

import lobos.andrew.aztec.Aztec;
import lobos.andrew.aztec.BinarySafeHTTPResponse;
import lobos.andrew.aztec.EasyRequestHandler;
import lobos.andrew.aztec.HTTPRequest;
import lobos.andrew.aztec.HTTPResponse;
import lobos.andrew.aztec.RequestHandler;
import lobos.andrew.aztec.SendableHTTPResponse;

import org.team4977.musicboxouya.database.library.LibraryProvider;
import org.team4977.musicboxouya.database.library.LibraryRefreshFinishedListener;
import org.team4977.musicboxouya.database.library.LocalLibrary;
import org.team4977.musicboxouya.media.Song;
import org.team4977.musicboxouya.player.MusicPlayer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity implements LibraryRefreshFinishedListener {

	MusicPlayer player = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final LocalLibrary library = new LocalLibrary("/sdcard/MusicTest");
		library.setRefreshFinishedListener(this);
		
		EasyRequestHandler reqHandler = new EasyRequestHandler();
		
		reqHandler.registerPage("/api/library", new RequestHandler()
		{

			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
					return new HTTPResponse(200, library.toJSON());
			}
			
		});
		
		reqHandler.registerPage("/api/queue", new RequestHandler()
		{
			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
				{
					if ( !req.getParam("song").equals("") )
					{
						try
						{
							Song song = library.getSong(Integer.parseInt(req.getParam("song")));
							if ( song != null )
							{
								player.queueSong(song);
							}
						} catch (Exception e) {
							System.out.println("Request for song that does not exist!");
						}
					}
					
					if ( !req.getParam("limit").equals("") )
					{
						try {
							int limit = Integer.parseInt(req.getParam("limit"));
							return new HTTPResponse(200, player.getQueue().toJSON(limit));
						}catch (Exception e) {}
					}
					return new HTTPResponse(200, player.getQueue().toJSON());
				}
					
			} 
		});
		
		reqHandler.registerPage("/api/nowplaying", new RequestHandler()
		{
			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
					return new HTTPResponse(200, player.nowPlayingJSON());
			}
		});
		
		reqHandler.registerPage("/api/like", new RequestHandler()
		{

			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
				{
					try
					{
						Song song = library.getSong(Integer.parseInt(req.getParam("song")));
						if ( song != null )
						{
							song.like();
						}
						else
						{
						return new HTTPResponse(200, "{\"success\": false}");
						}
					} catch (Exception e) {
						return new HTTPResponse(200, "{\"success\": false}");
					}
					return new HTTPResponse(200, "{\"success\": true}");
				}
			}
		});
		
		reqHandler.registerPage("/api/dislike", new RequestHandler()
		{
			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
				{
					try
					{
						Song song = library.getSong(Integer.parseInt(req.getParam("song")));
						if ( song != null )
						{
							song.dislike();
						}
						else
						{
						return new HTTPResponse(200, "{\"success\": false}");
						}
					} catch (Exception e) {
						return new HTTPResponse(200, "{\"success\": false}");
					}
					return new HTTPResponse(200, "{\"success\": true}");
				}
			}
		});
		
		reqHandler.registerPage("/api/next", new RequestHandler()
		{
			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
				{
					player.next();
					return new HTTPResponse(200, "{\"success\": true}");
				}
			}
		});
		
		
		reqHandler.registerPage("/api/pause", new RequestHandler()
		{
			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
				{
					player.pause();
					return new HTTPResponse(200, "{\"success\": true, \"paused\": "+(player.isPaused()?"true":"false")+"}");
				}
			}
		});
		
		reqHandler.registerPage("/api/art", new RequestHandler()
		{
			public SendableHTTPResponse handleHTTPRequest(HTTPRequest req) {
				if ( player == null )
					return new HTTPResponse(200, "Waiting for library refresh...");
				else
				{
					Song song = library.getSong(Integer.parseInt(req.getParam("song")));
					if ( song != null )
					{
						byte[] art = song.getRawArt();
						if ( art != null )
						{
							return new BinarySafeHTTPResponse(200, HTTPResponse.stringForResponseCode(200), art, "image/bmp");
						}
						else
						{
							return new HTTPResponse(200, "No art");
						}
							
					}
					return new HTTPResponse(500, "Error");
				}
			}
		});
		
		
		try {
			new Aztec(8080, reqHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		library.refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void libraryRefreshFinished(LibraryProvider library) {
		System.out.println("Library load finished!");
		player = MusicPlayer.getMusicPlayer();
	}

}
