package org.team4977.musicboxouya;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;


import org.team4977.musicboxouya.database.library.LibraryProvider;
import org.team4977.musicboxouya.database.library.LibraryRefreshFinishedListener;
import org.team4977.musicboxouya.database.library.LocalLibrary;
import org.team4977.musicboxouya.media.Song;
import org.team4977.musicboxouya.player.MusicPlayer;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.webkit.WebView;

public class MainActivity extends Activity implements LibraryRefreshFinishedListener {

	MusicPlayer player = null; 
	WebServer webserver = null;
	LocalLibrary library = null;
	WebView webView;
	String ipAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		getActionBar().hide();
		setContentView(webView);
		
		library = new LocalLibrary("/sdcard/MusicTest");
		library.setRefreshFinishedListener(this);
			
		library.refresh();
		
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
		
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
		webserver = new WebServer(library, player, ipAddress);
		try {
			webserver.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webView.loadUrl("http://localhost:8080/tvView.html");
	}

}
