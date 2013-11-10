package com.PP.StompDetector;


import com.PP.LunarTabsAndroid.APIs.MetronomeAPI;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import android.app.Activity;

public class MetronomeStomp extends StompDetector {

	protected MainActivity mainActivity;
	
	//state
	protected boolean tempoSet = false;
	protected double lastTS = -1;
	
	public MetronomeStomp(Activity parent) {
		super(parent);
		mainActivity = (MainActivity) parent;
	}

	@Override
	protected void trigger_callback(double timestamp) {
		if(!tempoSet && lastTS==-1) {
			
			//state
			lastTS = timestamp;
			
		}
		else if(!tempoSet && lastTS!=-1) {
			
			//find tempo and start metronome
			double diff_ms = timestamp - lastTS;
			double bpm = 1.0 / (diff_ms / (1000*60));			
			MetronomeAPI.getInstance().setBpm((short)bpm);
			MetronomeAPI.getInstance().start();
			
			//state
			tempoSet = true;
		}
		else if(tempoSet && lastTS!=-1) {
			
			//stop metronome
			MetronomeAPI.getInstance().stop();
			
			//state
			tempoSet = false;
			lastTS = -1;
		}
	}

}
