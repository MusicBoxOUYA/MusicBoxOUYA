package org.team4977.musicboxouya;

import java.io.IOException;
import org.team4977.musicboxouya.database.library.LibraryProvider;
import org.team4977.musicboxouya.database.library.LibraryRefreshFinishedListener;
import org.team4977.musicboxouya.database.library.LocalLibrary;
import org.team4977.musicboxouya.player.MusicPlayer;

import tv.ouya.console.api.OuyaController;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
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
		OuyaController.init(this);
		
		webView = new WebView(this)
		{
			public boolean onKeyDown(final int keyCode, KeyEvent event){ 
			    boolean handled = false;

			    //Handle the input
			    switch(keyCode){
			        case OuyaController.BUTTON_O:
			            if ( player != null )
			            	player.pause();
			            handled = true;
			            break;
			        case OuyaController.BUTTON_A:
			            if ( player != null )
			            	player.next();
			            handled = true;
			            break;
			        case OuyaController.BUTTON_Y:
			        	library.resetCache();
			        	library.refresh();
			        	handled = true;
			        	break;
			    }
			    return handled || super.onKeyDown(keyCode, event);
			}
		};
		webView.setInitialScale(1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		getActionBar().hide();
		setContentView(webView);
		webView.setSelected(true);
		
		//library = new LocalLibrary(this, "/mnt/usbdrive/Music");
		library = new LocalLibrary(this, "/sdcard/MusicTest");
		library.setRefreshFinishedListener(this);
			
		library.refresh();
		
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
		
	}
	
	@Override
	public boolean onKeyDown(final int keyCode, KeyEvent event){
	    //Get the player #     
	    boolean handled = false;

	    //Handle the input
	    switch(keyCode){
	        case OuyaController.BUTTON_O:
	            if ( player != null )
	            	player.pause();
	            handled = true;
	            break;
	        case OuyaController.BUTTON_A:
	            if ( player != null )
	            	player.next();
	            handled = true;
	            break;
	    }
	    return handled || super.onKeyDown(keyCode, event);
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
