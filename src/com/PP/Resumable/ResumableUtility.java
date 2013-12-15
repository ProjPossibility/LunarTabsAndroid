package com.PP.Resumable;

import android.util.Log;

public abstract class ResumableUtility {
	
	//state on stop
	protected volatile boolean runningState;
	protected volatile boolean onStop_state;
	
	//abstract start and stop
	public abstract void start();
	public abstract void stop();
		
	public void onStop() {
		onStop_state = runningState;
		Log.d("SETTING", "ONSTOPSTATE: " + onStop_state);
		if(onStop_state) {
			doStop();
		}
	}
	
	public void onResume() {
		Log.d("CHECKING", "ONSTOPSTATE: " + onStop_state);
		if(onStop_state) {
			doStart();
		}
	}
	
	public void doStart() {
		runningState = true;		
		start();
	}
	
	public void doStop() {
		runningState = false;
		stop();
	}
	/**
	 * @return the onStop_state
	 */
	public boolean isOnStop_state() {
		return onStop_state;
	}
	/**
	 * @param onStop_state the onStop_state to set
	 */
	public void setOnStop_state(boolean onStop_state) {
		this.onStop_state = onStop_state;
	}
}