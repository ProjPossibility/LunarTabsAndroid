package com.PP.APIs;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

public class VolumeAPI {
	
	//default flags
	public static final float DEFAULT_VOLUME_FACTOR = 0.5f;
	
	//variables
	protected AudioManager audioManager;
	protected float volumeFactor;
	
	//singleton
	protected VolumeAPI() {}
	protected static VolumeAPI instance;
	public static VolumeAPI getInstance() {
		if(instance==null) {
			instance = new VolumeAPI();
		}
		return instance;
	}
	
	public void init(Activity a) {
        audioManager = (AudioManager) a.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void setVolume(float volumeFactor) {
		this.volumeFactor = volumeFactor;
        int maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxMediaVolume  * volumeFactor),0);
	}
}
