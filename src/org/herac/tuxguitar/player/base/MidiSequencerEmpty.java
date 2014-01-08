package org.herac.tuxguitar.player.base;

import org.herac.tuxguitar.song.models.TGTimeSignature;

public class MidiSequencerEmpty implements MidiSequencer{
	
	@Override
	public void open() {
		// Not implemented
	}	
	
	@Override
	public void close() {
		// Not implemented
	}
	
	@Override
	public void check() {
		// Not implemented
	}
	
	@Override
	public MidiSequenceHandler createSequence(int tracks) {
		return new MidiSequenceHandler(tracks) {
			
			@Override
			public void notifyFinish() {
				// Not implemented
			}
			
			@Override
			public void addTimeSignature(long tick, int track, TGTimeSignature ts) {
				// Not implemented
			}
			
			@Override
			public void addTempoInUSQ(long tick, int track, int usq) {
				// Not implemented
			}
			
			@Override
			public void addProgramChange(long tick, int track, int channel,int instrument) {
				// Not implemented
			}
			
			@Override
			public void addPitchBend(long tick, int track, int channel, int value, int voice, boolean bendMode) {
				// Not implemented
			}
			
			@Override
			public void addNoteOn(long tick, int track, int channel, int note,int velocity, int voice, boolean bendMode) {
				// Not implemented
			}
			
			@Override
			public void addNoteOff(long tick, int track, int channel, int note,int velocity, int voice, boolean bendMode) {
				// Not implemented
			}
			
			@Override
			public void addControlChange(long tick, int track, int channel,int controller, int value) {
				// Not implemented
			}
		};
	}
	
	@Override
	public void setTransmitter(MidiTransmitter transmitter) throws MidiPlayerException {
		// Not implemented
	}
	
	@Override
	public long getTickLength() {
		// Not implemented
		return 0;
	}
	
	@Override
	public long getTickPosition() {
		// Not implemented
		return 0;
	}
	
	@Override
	public boolean isRunning() {
		// Not implemented
		return false;
	}
	
	@Override
	public void setMute(int index, boolean mute) {
		//not implemented
	}
	
	@Override
	public void setSolo(int index, boolean solo) {
		// Not implemented
	}
	
	@Override
	public void setTickPosition(long tickPosition) {
		// Not implemented
	}
	
	@Override
	public void start() {
		// Not implemented
	}
	
	@Override
	public void stop() {
		// Not implemented
	}
	
	@Override
	public String getKey() {
		return null;
	}
	
	@Override
	public String getName() {
		return null;
	}	
}
