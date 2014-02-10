package com.PP.APIs;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

public class VibratorAPI {
	
	//const
	public static final int VIBRATE_LENGTH = 300;
	
	//fields
	protected static Vibrator vibrator;
	protected static boolean vibratorOn = true;
	
	//no instantiation
	protected VibratorAPI(){}
	
	/**
	 * Init function
	 * @param a The activity to init with
	 */
	public static void init(Activity a) {
    	vibrator = (Vibrator) a.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
	 * Call vibrate function.
	 */
	public static void vibrate() {
		if(vibrator!=null && vibratorOn) {
			vibrator.vibrate(VIBRATE_LENGTH);
		}
	}

	/**
	 * @return the vibratorOn
	 */
	public static boolean isVibratorOn() {
		return vibratorOn;
	}

	/**
	 * @param vibratorOn the vibratorOn to set
	 */
	public static void setVibratorOn(boolean vibratorOn) {
		VibratorAPI.vibratorOn = vibratorOn;
	}	

}
