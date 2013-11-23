package org.team4977.musicboxouya.database.library;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class ArtworkGetter {
	private static final String API_KEY = "0c8d0b702a23553883685199703abe8c";
	private static final String DEFAULT_ART = "http://www.google.com/";
	public static String getArtwork(String artist, String album)
	{
		try
		{
		    HttpClient httpclient = new DefaultHttpClient();
		    album = URLEncoder.encode(album, "UTF-8");
		    artist = URLEncoder.encode(artist, "UTF-8");
		    HttpResponse response = httpclient.execute(new HttpGet("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key="+API_KEY+"&artist="+artist+"&album="+album+"&format=json"));
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        JSONObject data = new JSONObject(responseString);
		        System.out.println(responseString);
		        JSONObject albumInfo = data.getJSONObject("album");
		        if ( albumInfo != null )
		        {
		        	JSONArray images = albumInfo.getJSONArray("image");
		        	if ( images != null && images.length() != 0 )
		        	{
		        		JSONObject image = images.getJSONObject(images.length()-1);
		        		return image.getString("#text");
		        	}
		        }
		    } 
		    else
		    {
		        //Closes the connection.
		        response.getEntity().getContent().close();
		        return DEFAULT_ART;
		    }
		} catch ( Exception e )
		{
			e.printStackTrace();
			return DEFAULT_ART;
		}
		return DEFAULT_ART;
	}
}
