package com.tuxguitar.io.midi;

import java.io.OutputStream;

import com.tuxguitar.io.base.TGFileFormat;
import com.tuxguitar.io.base.TGSongExporter;
import com.tuxguitar.player.base.MidiSequenceParser;
import com.tuxguitar.song.managers.TGSongManager;
import com.tuxguitar.song.models.TGSong;


public class MidiSongExporter implements TGSongExporter{
	
	private MidiSettings settings;
	
	public MidiSongExporter() {
		settings = new MidiSettings();
	}
	
	@Override
	public String getExportName() {
		return "Midi";
	}
	
	@Override
	public TGFileFormat getFileFormat() {
		return new TGFileFormat("Midi","*.mid;*.midi");
	}
	
	@Override
	public boolean configure(boolean setDefaults) {
		this.settings = (MidiSettings.getDefaults());
		return (this.settings != null);
	}
	
	@Override
	public void exportSong(OutputStream stream, TGSong song) {
		TGSongManager manager = new TGSongManager();
		manager.setSong(song);
		MidiSequenceParser parser = new MidiSequenceParser(manager,MidiSequenceParser.DEFAULT_EXPORT_FLAGS,100,this.settings.getTranspose());
		MidiSequenceHandlerImpl sequence = new MidiSequenceHandlerImpl( (song.countTracks() + 1) , stream);
		parser.parse(sequence);
	}
}
