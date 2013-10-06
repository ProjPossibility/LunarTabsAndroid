package com.tuxguitar.io.base;

import java.io.IOException;
import java.io.OutputStream;

import com.tuxguitar.song.factory.TGFactory;
import com.tuxguitar.song.models.TGSong;


public interface TGOutputStreamBase {
	
	public void init(TGFactory factory,OutputStream stream);
	
	public boolean isSupportedExtension(String extension);
	
	public TGFileFormat getFileFormat();
	
	public void writeSong(TGSong song) throws TGFileFormatException,IOException;
}
