package com.tuxguitar.song.models.effects;

import java.io.Serializable;

import com.tuxguitar.song.factory.TGFactory;
import com.tuxguitar.song.models.TGDuration;

public abstract class TGEffectTremoloPicking implements Serializable {
	
	private TGDuration duration;
	
	public TGEffectTremoloPicking(TGFactory factory) {
		this.duration = factory.newDuration();
	}
	
	public TGDuration getDuration() {
		return this.duration;
	}
	
	public void setDuration(TGDuration duration) {
		this.duration = duration;
	}
	
	public TGEffectTremoloPicking clone(TGFactory factory){
		TGEffectTremoloPicking effect = factory.newEffectTremoloPicking();
		effect.getDuration().setValue(getDuration().getValue());
		effect.getDuration().setDotted(getDuration().isDotted());
		effect.getDuration().setDoubleDotted(getDuration().isDoubleDotted());
		effect.getDuration().getTupleto().setEnters(getDuration().getTupleto().getEnters());
		effect.getDuration().getTupleto().setTimes(getDuration().getTupleto().getTimes());
		return effect;
	}
	
}
