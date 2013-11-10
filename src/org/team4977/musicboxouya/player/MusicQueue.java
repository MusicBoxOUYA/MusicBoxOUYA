package org.team4977.musicboxouya.player;

import java.util.Iterator;
import java.util.LinkedList;

import org.team4977.musicboxouya.media.Song;



public class MusicQueue extends LinkedList<Song> {
	public String toJSON()
	{
		String output = "[";
		for ( int i = 0; i < size(); i++ )
		{
			output += get(i).toJSON(true);
			if ( i != size()-1 )
				output += ",";
		}
		output += "]";
		return output;
	}
	
	public String toJSON(int limit)
	{
		String output = "[";
		for ( int i = 0; i < size(); i++ )
		{
			output += get(i).toJSON(true);
			if ( i != size()-1 && i != limit-1 )
				output += ",";
			if ( i == limit-1 )
				break;
		}
		output += "]";
		return output;
	}
}
