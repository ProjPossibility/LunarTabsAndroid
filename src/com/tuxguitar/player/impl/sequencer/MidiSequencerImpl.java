package com.tuxguitar.player.impl.sequencer;

import com.tuxguitar.player.base.*;

public class MidiSequencerImpl implements MidiSequencer{
	
	private boolean reset;
	private boolean running;
	private MidiTransmitter transmitter;
	private MidiTickPlayer midiTickPlayer;
	private MidiEventPlayer midiEventPlayer;
	private MidiEventDispacher midiEventDispacher;
	private MidiTrackController midiTrackController;
	
	public MidiSequencerImpl(){
		this.running = false;
		this.midiTickPlayer = new MidiTickPlayer();
		this.midiEventPlayer = new MidiEventPlayer(this);
		this.midiEventDispacher = new MidiEventDispacher(this);
		this.midiTrackController = new MidiTrackController(this);
	}
	
	public synchronized MidiTrackController getMidiTrackController(){
		return this.midiTrackController;
	}
	
	public synchronized void setTempo(int tempo){
		this.midiTickPlayer.setTempo(tempo);
	}
	
	@Override
	public synchronized long getTickPosition(){
		return this.midiTickPlayer.getTick();
	}
	
	@Override
	public synchronized void setTickPosition(long tickPosition){
		this.reset = true;
		this.midiTickPlayer.setTick(tickPosition);
	}
	
	@Override
	public synchronized long getTickLength(){
		return this.midiTickPlayer.getTickLength();
	}
	
	public synchronized void sendEvent(MidiEvent event) throws MidiPlayerException{
		if(!this.reset){
			this.midiEventDispacher.dispatch(event);
		}
	}
	
	public synchronized void addEvent(MidiEvent event){
		this.midiEventPlayer.addEvent(event);
		this.midiTickPlayer.notifyTick(event.getTick());
	}
	
	@Override
	public synchronized boolean isRunning() {
		return this.running;
	}
	
	public synchronized void setRunning(boolean running) throws MidiPlayerException {
		this.running = running;
		if(this.running){
			this.setTempo(120);
			this.setTickPosition( this.getTickPosition() );
			new MidiTimer(this).start();
		}else{
			this.process();
		}
	}
	
	@Override
	public synchronized void stop() throws MidiPlayerException{
		this.setRunning(false);
	}
	
	@Override
	public synchronized void start() throws MidiPlayerException{
		this.setRunning(true);
	}
	
	public synchronized void reset(boolean systemReset)  throws MidiPlayerException{
		this.getTransmitter().sendAllNotesOff();
		for(int channel = 0; channel < 16;channel ++){
			this.getTransmitter().sendPitchBend(channel, 64);
		}
		if( systemReset ){
			this.getTransmitter().sendSystemReset();
		}
	}
	
	protected synchronized boolean process() throws MidiPlayerException{
		boolean running = this.isRunning();
		if(running){
			if(this.reset){
				this.reset( false );
				this.reset = false;
				this.midiEventPlayer.reset();
			}
			this.midiTickPlayer.process();
			this.midiEventPlayer.process();
			if(this.getTickPosition() > this.getTickLength()){
				this.stop();
			}
		}
		else{
			this.midiEventPlayer.clearEvents();
			this.midiTickPlayer.clearTick();
			this.reset( true );
		}
		return running;
	}
	
	public synchronized MidiTransmitter getTransmitter() {
		return this.transmitter;
	}
	
	@Override
	public synchronized void setTransmitter(MidiTransmitter transmitter) {
		this.transmitter = transmitter;
	}
	
	@Override
	public synchronized void open() {
		//not implemented
	}
	
	@Override
	public synchronized void close() throws MidiPlayerException {
		if(isRunning()){
			this.stop();
		}
	}
	
	@Override
	public synchronized MidiSequenceHandler createSequence(int tracks) throws MidiPlayerException{
		return new MidiSequenceHandlerImpl(this,tracks);
	}
	
	@Override
	public synchronized void setSolo(int index,boolean solo) throws MidiPlayerException{
		this.getMidiTrackController().setSolo(index, solo);
	}
	
	@Override
	public synchronized void setMute(int index,boolean mute) throws MidiPlayerException{
		this.getMidiTrackController().setMute(index, mute);
	}
	
	@Override
	public String getKey() {
		return "tuxguitar.sequencer";
	}
	
	@Override
	public String getName() {
		return "TuxGuitar Sequencer";
	}
	
	private class MidiTimer extends Thread{
		
		private static final int TIMER_DELAY = 15;
		
		private MidiSequencerImpl sequencer;
		
		public MidiTimer(MidiSequencerImpl sequencer){
			this.sequencer = sequencer;
		}
		
		@Override
		public void run() {
			try {
				synchronized(this.sequencer) {
					while( this.sequencer.process() ){
						this.sequencer.wait( TIMER_DELAY );
					}
				}
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}
}
