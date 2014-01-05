package com.PP.LunarTabsAndroid.APIs;

import java.util.HashMap;
import java.util.Locale;

import com.PP.LunarTabsAndroid.UI.DataModel;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
		if(instance==null) {
			instance = new TextToSpeechAPI();
		}
		if(instance.tts==null) {
			instance.tts = new TextToSpeech(mainActivity.getApplicationContext(),instance);
		}
	}
	
	/**
	 * Speaks text
	 * @param text Text to speak
	 */
	public static void speak(String text) {
		if(instance!=null && instance.tts!=null && text!=null && !text.trim().equals((""))) {
			
			//turn of stt (if currently running)
			if(DataModel.getInstance().isVoiceActionsEnabled()) {
				WordActivatorAPI.getInstance().stopListening();
			}
			
			//turn on tts
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,text);			
    		instance.tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {    	  
			int result = tts.setLanguage(Locale.US);
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
			instance.tts.setOnUtteranceProgressListener(new UtteranceProgressListener()
	        {
	            @Override
	            public void onDone(String utteranceId)
	            {
	            	
	            	//restart stt (if currently user enabled)
	            	Log.d("END", "FINISHED: " + utteranceId);
	    			if(DataModel.getInstance().isVoiceActionsEnabled()) {
	    				WordActivatorAPI.getInstance().start();                	
	    			}
	            }
	
	            @Override
	            public void onError(String utteranceId){}
	
	            @Override
	            public void onStart(String utteranceId)
	            {
	            	Log.d("START", "STARTED MUMBLING: " + utteranceId);
	            }
	        });			
		}
		else {
			Log.e("TTS", "Initilization Failed!");
			instance=null;
		}
	}
}
