package com.tuxguitar.player.impl.sequencer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tuxguitar.player.base.*;


public class MidiSequencerProviderImpl implements MidiSequencerProvider{
	
	private List sequencers;
	
	public MidiSequencerProviderImpl(){
		super();
	}
	
	@Override
	public List listSequencers() throws MidiPlayerException {
		if(this.sequencers == null){
			this.sequencers = new ArrayList();
			this.sequencers.add(new MidiSequencerImpl());
		}
		return this.sequencers;
	}
	
	@Override
	public void closeAll() throws MidiPlayerException {
		Iterator it = listSequencers().iterator();
		while(it.hasNext()){
			MidiSequencer sequencer = (MidiSequencer)it.next();
			sequencer.close();
		}
	}
	
}
