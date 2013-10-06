package com.tuxguitar.io.base;

import java.io.InputStream;

import com.tuxguitar.song.factory.TGFactory;
import com.tuxguitar.song.models.TGSong;


public interface TGSongImporter {
	
	public String getImportName();
	
	public TGFileFormat getFileFormat();
	
	public boolean configure(boolean setDefaults);
	
	public TGSong importSong(TGFactory factory,InputStream stream) throws TGFileFormatException;
	
}
