package org.team4977.musicboxouya;

import java.io.IOException;

import lobos.andrew.aztec.Aztec;
import lobos.andrew.aztec.EasyRequestHandler;
import lobos.andrew.aztec.HTTPRequest;
import lobos.andrew.aztec.HTTPResponse;
import lobos.andrew.aztec.RequestHandler;

import org.team4977.musicboxouya.database.library.LocalLibrary;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final LocalLibrary library = new LocalLibrary("/sdcard/MusicTest");
		
		EasyRequestHandler reqHandler = new EasyRequestHandler();
		reqHandler.registerPage("/api/library", new RequestHandler()
		{

			@Override
			public HTTPResponse handleHTTPRequest(HTTPRequest req) {
				return new HTTPResponse(200, library.toJSON());
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

}
