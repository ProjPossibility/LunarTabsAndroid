
package com.PP.APIs;

import android.os.AsyncTask;
import android.os.Build;

import com.pntanasis.android.metronome.MetronomeAsyncTask;

public class MetronomeAPI {
	
	//params
	protected short bpm = 120;
	protected short noteValue = 4;
	protected short beats = 4;
	protected double beatSound = 1440;
	protected double sound = 2440;	
	
	//async task
    protected MetronomeAsyncTask metroTask;    
	
	//singleton
	protected MetronomeAPI(){}
	protected static MetronomeAPI instance;
	public static MetronomeAPI getInstance() {
		if(instance==null) {
			instance = new MetronomeAPI();
		}
		return instance;
	}
	
	public void start() {
        metroTask = new MetronomeAsyncTask();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			metroTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		else
			metroTask.execute();    		
	}
	
	public void stop() {
		if(metroTask!=null) {
			metroTask.stop();
		}
		metroTask = null;
		Runtime.getRuntime().gc();		
	}
	

	/**
	 * @return the bpm
	 */
	public short getBpm() {
		return bpm;
	}

	/**
	 * @param bpm the bpm to set
	 */
	public void setBpm(short bpm) {
		this.bpm = bpm;
	}

	/**
	 * @return the noteValue
	 */
	public short getNoteValue() {
		return noteValue;
	}

	/**
	 * @param noteValue the noteValue to set
	 */
	public void setNoteValue(short noteValue) {
		this.noteValue = noteValue;
	}

	/**
	 * @return the beats
	 */
	public short getBeats() {
		return beats;
	}

	/**
	 * @param beats the beats to set
	 */
	public void setBeats(short beats) {
		this.beats = beats;
	}

	/**
	 * @return the beatSound
	 */
	public double getBeatSound() {
		return beatSound;
	}

	/**
	 * @param beatSound the beatSound to set
	 */
	public void setBeatSound(double beatSound) {
		this.beatSound = beatSound;
	}

	/**
	 * @return the sound
	 */
	public double getSound() {
		return sound;
	}

	/**
	 * @param sound the sound to set
	 */
	public void setSound(double sound) {
		this.sound = sound;
	}
}
