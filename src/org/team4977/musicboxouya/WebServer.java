package org.team4977.musicboxouya;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.team4977.musicboxouya.database.library.LibraryProvider;
import org.team4977.musicboxouya.media.Song;
import org.team4977.musicboxouya.player.MusicPlayer;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
	LibraryProvider library;
	MusicPlayer player;
	public WebServer(LibraryProvider library, MusicPlayer player)
	{
		super(8080);
		this.library = library;
		this.player = player;
	}
	
    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
    	// Static content
    	if ( uri.equals("/") || uri.equals("/index.html") )
    	{
    		return new Response(Response.Status.OK, "text/html", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/index.html"));
    	}
    	else if ( uri.equals("/library.html") )
    	{
    		return new Response(Response.Status.OK, "text/html", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/library.html"));
    	}
    	else if ( uri.equals("/queue.html") )
    	{
    		return new Response(Response.Status.OK, "text/html", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/queue.html"));
    	}
    	else if ( uri.equals("/tvView.html") )
    	{
    		return new Response(Response.Status.OK, "text/html", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/tvView.html"));
    	}
    	
    	// API
    	else if ( uri.equals("/api/library") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
				return new Response(library.toJSON());
    	}
    	else if ( uri.equals("/api/queue") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
			{
				if ( !(parms.get("song") == null) )
				{
					try
					{
						Song song = library.getSong(Integer.parseInt(parms.get("song")));
						if ( song != null )
						{
							player.queueSong(song);
						}
					} catch (Exception e) {
						System.out.println("Request for song that does not exist!");
					}
				}
				
				if ( parms.get("limit") != null )
				{
					try {
						int limit = Integer.parseInt(parms.get("limit"));
						return new Response(player.getQueue().toJSON(limit));
					}catch (Exception e) {}
				}
				return new Response(player.getQueue().toJSON());
			}
    	}
    	else if ( uri.equals("/api/nowplaying") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
				return new Response(player.nowPlayingJSON());
    	}
    	else if ( uri.equals("/api/like") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
			{
				try
				{
					Song song = library.getSong(Integer.parseInt(parms.get("song")));
					if ( song != null )
					{
						song.like();
					}
					else
					{
					return new Response("{\"success\": false}");
					}
				} catch (Exception e) {
					return new Response("{\"success\": false}");
				}
				return new Response("{\"success\": true}");
			}
    	}
    	else if ( uri.equals("/api/dislike") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
			{
				try
				{
					Song song = library.getSong(Integer.parseInt(parms.get("song")));
					if ( song != null )
					{
						song.dislike();
					}
					else
					{
					return new Response("{\"success\": false}");
					}
				} catch (Exception e) {
					return new Response("{\"success\": false}");
				}
				return new Response("{\"success\": true}");
			}
		}
    	else if ( uri.equals("/api/next") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
			{
				player.next();
				return new Response("{\"success\": true}");
			}
    	}
    	else if ( uri.equals("/api/pause") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
			{
				player.pause();
				return new Response("{\"success\": true, \"paused\": "+(player.isPaused()?"true":"false")+"}");
			}
    	}
    	else if ( uri.equals("/api/art") )
    	{
    		if ( player == null )
				return new Response("Waiting for library refresh...");
			else
			{
				Song song = library.getSong(Integer.parseInt(parms.get("song")));
				if ( song != null )
				{
					byte[] art = song.getRawArt();
					if ( art != null )
					{
						return new Response(Response.Status.OK, "image/bmp", new ByteArrayInputStream(art));
					}
					else
					{
						return new Response("No art");
					}
						
				}
				return new Response("Error");
			}
    	}
    	
		return new Response(Response.Status.NOT_FOUND, "text/html", "404");
    }
}