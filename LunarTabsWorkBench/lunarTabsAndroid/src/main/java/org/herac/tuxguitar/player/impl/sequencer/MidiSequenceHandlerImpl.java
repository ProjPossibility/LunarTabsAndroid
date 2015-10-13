package org.herac.tuxguitar.player.impl.sequencer;

import org.herac.tuxguitar.player.base.MidiSequenceHandler;
import org.herac.tuxguitar.song.models.TGTimeSignature;

public class MidiSequenceHandlerImpl extends MidiSequenceHandler{
	
	private MidiSequencerImpl seq;
	
	public MidiSequenceHandlerImpl(MidiSequencerImpl seq,int tracks) {
		super(tracks);
		this.seq = seq;
		this.seq.getMidiTrackController().init(getTracks());
	}
	
	@Override
	public void addControlChange(long tick,int track,int channel, int controller, int value) {
		this.seq.addEvent(MidiEvent.controlChange(tick, track, channel, controller, value));
	}
	
	@Override
	public void addNoteOff(long tick,int track,int channel, int note, int velocity, int voice, boolean bendMode) {
		this.seq.addEvent(MidiEvent.noteOff(tick, track, channel, note, velocity, voice, bendMode));
	}
	
	@Override
	public void addNoteOn(long tick,int track,int channel, int note, int velocity, int voice, boolean bendMode) {
		this.seq.addEvent(MidiEvent.noteOn(tick, track, channel, note, velocity, voice, bendMode));
	}
	
	@Override
	public void addPitchBend(long tick,int track,int channel, int value, int voice, boolean bendMode) {
		this.seq.addEvent(MidiEvent.pitchBend(tick, track, channel, value, voice, bendMode));
	}
	
	@Override
	public void addProgramChange(long tick,int track,int channel, int instrument) {
		this.seq.addEvent(MidiEvent.programChange(tick, track, channel, instrument));
	}
	
	@Override
	public void addTempoInUSQ(long tick,int track,int usq) {
		this.seq.addEvent(MidiEvent.tempoInUSQ(tick, usq));
	}
	
	@Override
	public void addTimeSignature(long tick,int track,TGTimeSignature ts) {
		//not implemented
	}
	
	@Override
	public void notifyFinish(){
		//not implemented
	}
}
