package com.tuxguitar.io.midi;

import java.io.IOException;
import java.io.OutputStream;

import com.tuxguitar.io.midi.base.*;
import com.tuxguitar.player.base.*;
import com.tuxguitar.song.models.*;


public class MidiSequenceHandlerImpl extends MidiSequenceHandler{
	
	private OutputStream stream;
	private MidiSequence sequence;
	
	public MidiSequenceHandlerImpl(int tracks,OutputStream stream){
		super(tracks);
		this.stream = stream;
		this.init();
	}
	
	private void init(){
		this.sequence = new MidiSequence(MidiSequence.PPQ,(int)TGDuration.QUARTER_TIME);
		for (int i = 0; i < getTracks(); i++) {
			this.sequence.addTrack(new MidiTrack());
		}
	}
	
	public MidiSequence getSequence(){
		return this.sequence;
	}
	
	public void addEvent(int track, MidiEvent event) {
		if(track >= 0 && track < getSequence().countTracks()){
			getSequence().getTrack(track).add(event);
		}
	}
	
	@Override
	public void addControlChange(long tick,int track,int channel, int controller, int value) {
		addEvent(track,new MidiEvent(MidiMessageUtils.controlChange(channel, controller, value), tick ));
	}
	
	@Override
	public void addNoteOff(long tick,int track,int channel, int note, int velocity) {
		addEvent(track,new MidiEvent(MidiMessageUtils.noteOff(channel, note, velocity), tick ));
	}
	
	@Override
	public void addNoteOn(long tick,int track,int channel, int note, int velocity) {
		addEvent(track,new MidiEvent(MidiMessageUtils.noteOn(channel, note, velocity), tick ));
	}
	
	@Override
	public void addPitchBend(long tick,int track,int channel, int value) {
		addEvent(track,new MidiEvent(MidiMessageUtils.pitchBend(channel, value), tick ));
	}
	
	@Override
	public void addProgramChange(long tick,int track,int channel, int instrument) {
		addEvent(track,new MidiEvent(MidiMessageUtils.programChange(channel, instrument), tick ));
	}
	
	@Override
	public void addTempoInUSQ(long tick,int track,int usq) {
		addEvent(track,new MidiEvent(MidiMessageUtils.tempoInUSQ(usq), tick ));
	}
	
	@Override
	public void addTimeSignature(long tick,int track,TGTimeSignature ts) {
		addEvent(track,new MidiEvent(MidiMessageUtils.timeSignature(ts), tick ));
	}
	
	@Override
	public void notifyFinish() {
		try {
			getSequence().finish();
			new MidiFileWriter().write(getSequence(),1,this.stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
