package org.team4977.musicboxouya.player;

import java.util.ArrayList;

import org.team4977.musicboxouya.media.Song;

public class RandomMusicPool {
	ArrayList<Song> pool = new ArrayList<Song>();
	int baseCount = 5;
	public RandomMusicPool(Song[] songs)
	{
		for( int i = 0; i < songs.length; i++ )
		{
			int likes = songs[i].getLikes();
			
			if ( likes > baseCount ) likes = baseCount;
			else if ( likes < -baseCount ) likes = -baseCount;
			
			int insertCount = baseCount+songs[i].getLikes();
			for ( int a = 0; a < insertCount; a++ )
			{
				pool.add(songs[i]);
			}
		}
	}
	
	public Song pick()
	{
		int index = (int) Math.floor(Math.random()*pool.size());
		return pool.get(index);
	}
	
}
