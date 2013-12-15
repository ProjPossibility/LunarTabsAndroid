package com.PP.LunarTabsAndroid.APIs;

import android.content.Context;
import android.media.AudioManager;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;
import com.root.gast.speech.activation.WordActivator;

public class WordActivatorAPI {
		
	//Word Activator object and parent
	protected MainActivity parent;
	protected volatile WordActivator wa = null;
	
	//on stop/resume state
	protected volatile boolean onStop_state = false;
		
	//singleton
	protected WordActivatorAPI(){}
	protected static WordActivatorAPI instance=null;
	public static WordActivatorAPI getInstance() {
		if(instance==null) {
			instance = new WordActivatorAPI();
		}
		return instance;
	}
	
	/**
	 * Init
	 * @param targetWords Words to track
	 * @param parent Main Activity parent (that implements speech listener)
	 */
	public void init(String[] targetWords, MainActivity parent) {
				
		//store parent
		this.parent = parent;
		
		//preempt beeping noise of speech recognizer by turning it off
		AudioManager amanager=(AudioManager)parent.getSystemService(Context.AUDIO_SERVICE);
		amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);        
		
		//initialize word activator
		boolean needGC = (wa!=null);
		wa = new WordActivator(parent,parent,targetWords);
		if(needGC) {
			System.gc();
		}
	}
	
	/**
	 * Wrapper for start
	 */
	public void start() {
		if(parent!=null) {
			parent.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(wa!=null) {
						wa.detectActivation();
					}				
				}
			});
		}
	}
	
	/**
	 * Wrapper for stoping listening
	 */
	public void stopListening() {
		
		if(parent!=null) {
			parent.runOnUiThread(new Runnable() {
			
				@Override
				public void run() {
					if(wa!=null) {
						wa.stopListening();
					}
				}
				
			});
		}
	}
	
	/**
	 * Disable for a period of time (used for speech)
	 */
	public void disableFor(final int ms_wait) {
		
		//make mechanism inactive and 
		//make reactive after wait
		Thread t = new Thread() {
			
			@Override
			public void run() {
				try {
					WordActivatorAPI.getInstance().stopListening();
					Thread.sleep(ms_wait);
					if(GUIDataModel.getInstance().isVoiceActionsEnabled()) {
						WordActivatorAPI.getInstance().start();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		};
		t.start();
	}
	
	public void onStop() {
		onStop_state = GUIDataModel.getInstance().isVoiceActionsEnabled();
		if(onStop_state) {
     	   GUIDataModel.getInstance().setVoiceActionsEnabled(false);
     	   WordActivatorAPI.getInstance().stopListening();
		}		
	}
	
	public void onResume() {
		if(onStop_state) {
     	   //start voice actions
     	   GUIDataModel.getInstance().setVoiceActionsEnabled(true);
     	   WordActivatorAPI.getInstance().start();
		}
	}
}