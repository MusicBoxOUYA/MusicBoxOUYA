package org.team4977.musicboxouya;

import java.io.IOException;


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
	WebServer webserver = null;
	LocalLibrary library = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		library = new LocalLibrary("/sdcard/MusicTest");
		library.setRefreshFinishedListener(this);
			
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
		webserver = new WebServer(library, player);
		try {
			webserver.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
