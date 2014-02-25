package com.tuxguitar.io.base;

import java.io.IOException;
import java.io.InputStream;

import com.tuxguitar.song.factory.TGFactory;
import com.tuxguitar.song.models.TGSong;


public interface TGInputStreamBase {
	
	public void init(TGFactory factory,InputStream stream);
	
	public boolean isSupportedVersion();
	
	public TGFileFormat getFileFormat();
	
	public TGSong readSong() throws TGFileFormatException,IOException;
}
