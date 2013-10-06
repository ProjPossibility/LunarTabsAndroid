package com.PP.LunarTabsAndroid.APIs;

import java.util.Locale;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * Wrapper for speech API (for use of use)
 * @author prateek
 *
 */
public class TextToSpeechAPI implements TextToSpeech.OnInitListener {
	
	//fields
	protected TextToSpeech tts;
	
	//singleton instance
	protected static TextToSpeechAPI instance = null;
		
	/**
	 * Init API -- hook for main activity
	 * @param mainActivity
	 */
	public static void init(Activity mainActivity) {
		instance = new TextToSpeechAPI();
        instance.tts = new TextToSpeech(mainActivity,instance);
	}
	
	/**
	 * Speaks text
	 * @param text Text to speak
	 */
	public static void speak(String text) {
		if(instance!=null && instance.tts!=null && text!=null && !text.trim().equals((""))) {
    		instance.tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {    	  
			int result = tts.setLanguage(Locale.US);
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}  
		}
		else {
			Log.e("TTS", "Initilization Failed!");
			instance=null;
		}
	}
}
