package com.tuxguitar.player.base;
import com.tuxguitar.song.models.*;

public class MidiSequencerEmpty implements MidiSequencer{
	
	public void open() {
		// Not implemented
	}	
	
	public void close() {
		// Not implemented
	}
	
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
			public void addPitchBend(long tick, int track, int channel, int value) {
				// Not implemented
			}
			
			@Override
			public void addNoteOn(long tick, int track, int channel, int note,int velocity) {
				// Not implemented
			}
			
			@Override
			public void addNoteOff(long tick, int track, int channel, int note,int velocity) {
				// Not implemented
			}
			
			@Override
			public void addControlChange(long tick, int track, int channel,int controller, int value) {
				// Not implemented
			}
		};
	}
	
	public void setTransmitter(MidiTransmitter transmitter) throws MidiPlayerException {
		// Not implemented
	}
	
	public long getTickLength() {
		// Not implemented
		return 0;
	}
	
	public long getTickPosition() {
		// Not implemented
		return 0;
	}
	
	public boolean isRunning() {
		// Not implemented
		return false;
	}
	
	public void setMute(int index, boolean mute) {
		//not implemented
	}
	
	public void setSolo(int index, boolean solo) {
		// Not implemented
	}
	
	public void setTickPosition(long tickPosition) {
		// Not implemented
	}
	
	public void start() {
		// Not implemented
	}
	
	public void stop() {
		// Not implemented
	}
	
	public String getKey() {
		return null;
	}
	
	public String getName() {
		return null;
	}	
}
