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
	String ipAddress;
	
	public WebServer(LibraryProvider library, MusicPlayer player, String ipAddress)
	{
		super(8080);
		this.library = library;
		this.player = player;
		this.ipAddress = ipAddress;
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
    	// JS
    	else if ( uri.equals("/resources/js/bridge.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/bridge.js"));
    	}
    	else if ( uri.equals("/resources/js/index.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/index.js"));
    	}
    	else if ( uri.equals("/resources/js/library.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/library.js"));
    	}
    	else if ( uri.equals("/resources/js/queue.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/queue.js"));
    	}
    	else if ( uri.equals("/resources/js/tv-view.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/tv-view.js"));
    	}
    	else if ( uri.equals("/resources/js/object.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/object.js"));
    	}
    	else if ( uri.equals("/resources/js/table.js") )
    	{
    		return new Response(Response.Status.OK, "text/javascript", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/js/table.js"));
    	}
    	// CSS
    	else if ( uri.equals("/resources/css/main.css") )
    	{
    		return new Response(Response.Status.OK, "text/css", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/css/main.css"));
    	}
    	else if ( uri.equals("/resources/css/index.css") )
    	{
    		return new Response(Response.Status.OK, "text/css", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/css/index.css"));
    	}
    	else if ( uri.equals("/resources/css/library.css") )
    	{
    		return new Response(Response.Status.OK, "text/css", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/css/library.css"));
    	}
    	else if ( uri.equals("/resources/css/tv-view.css") )
    	{
    		return new Response(Response.Status.OK, "text/css", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/css/tv-view.css"));
    	}
    	
    	// Images
    	else if ( uri.equals("/resources/a.png") )
    	{
    		return new Response(Response.Status.OK, "image/png", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/a.png"));
    	}
    	else if ( uri.equals("/resources/o.png") )
    	{
    		return new Response(Response.Status.OK, "image/png", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/www/resources/o.png"));
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
						return new Response(Response.Status.OK, "image/png", WebServer.class.getResourceAsStream("/org/team4977/musicboxouya/GenericMissing.png"));
					}
						
				}
				return new Response("Error");
			}
    	}
    	else if ( uri.equals("/api/getIP") )
    	{
    		return new Response(ipAddress);
    	}
    	
		return new Response(Response.Status.NOT_FOUND, "text/html", "404");
    }
}
