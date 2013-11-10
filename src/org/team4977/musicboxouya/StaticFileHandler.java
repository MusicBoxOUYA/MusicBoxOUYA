package org.team4977.musicboxouya;

import java.io.InputStream;
import lobos.andrew.aztec.BinarySafeHTTPResponse;
import lobos.andrew.aztec.HTTPRequest;
import lobos.andrew.aztec.HTTPResponse;
import lobos.andrew.aztec.RequestHandler;
import lobos.andrew.aztec.SendableHTTPResponse;

public class StaticFileHandler implements RequestHandler {

	String path;
	String data;
	public StaticFileHandler(String path)
	{
		this.path = path;
	}
	
	@Override
	public SendableHTTPResponse handleHTTPRequest(HTTPRequest req) {
		InputStream stream = StaticFileHandler.class.getResourceAsStream(path);
		byte[] data = new byte[1024*10];
		try
		{
			stream.read(data);
		} catch (Exception e) {
			return new HTTPResponse(500, "Error reading file");
		}
		
		return new BinarySafeHTTPResponse(200, data);
	}

}
