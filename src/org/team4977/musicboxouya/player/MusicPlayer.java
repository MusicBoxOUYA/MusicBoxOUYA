package org.team4977.musicboxouya.player;

import org.team4977.musicboxouya.database.library.LibraryProvider;
import org.team4977.musicboxouya.media.Song;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MusicPlayer implements OnCompletionListener {
	protected static MusicPlayer instance = null;
	protected MusicQueue queue = new MusicQueue();
	protected Song nowPlaying = null;
	boolean paused = false;
	
	public static MusicPlayer getMusicPlayer()
	{
		if ( instance == null )
			instance = new MusicPlayer();
		return instance;
	}
	
	MediaPlayer player;
	
	protected MusicPlayer()
	{
		System.out.println("Media player init!");
		player = new MediaPlayer();
		onCompletion(player); // Start by playing the first song randomly
	}
	
	public void onCompletion(MediaPlayer player)
	{
		Song nextSong = queue.poll();
		if ( nextSong == null )
		{
			RandomMusicPool pool = new RandomMusicPool(LibraryProvider.getInitalizedProvider().getSongs());
			nextSong = pool.pick();
		}
		playSong(nextSong);
	}
	
	public void queueSong(Song song)
	{
		if ( nowPlaying == null )
			playSong(song);
		else
			queue.add(song);
	}
	
	public MusicQueue getQueue()
	{
		return queue;
	}
	
	public void playSong(Song song)
	{
		if ( nowPlaying != null )
		{
			player.stop();
			player.release();
			player = new MediaPlayer();
		}
		nowPlaying = song;
		player.setOnCompletionListener(this);
		
		try {
			player.setDataSource(song.getPath());
			player.prepare();
			player.start();
			paused = false;
		} catch (Exception e) {
			System.out.println("DataSource set failed");
		}
	}
	
	public void next()
	{
		if ( nowPlaying != null )
		{
			onCompletion(player);
		}
	}
	
	public void pause()
	{
		if ( nowPlaying != null )
		{
			if ( paused )
			{
				player.start();
				paused = false;
			}
			else
			{
				player.pause();
				paused = true;
			}
		}
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public String nowPlayingJSON()
	{
		String output = "{";
		if ( nowPlaying != null )
		{
			output += "\"paused\": "+(paused?"true":"false")+",";
			output += "\"duration\": "+player.getDuration()+",";
			output += "\"position\": "+player.getCurrentPosition()+",";
			output += "\"song\": "+nowPlaying.toJSON(true);
		}
		output += "}";
		return output;
	}
}
