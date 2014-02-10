package com.PP.LunarTabsAndroid.Activities;

import com.PP.LunarTabsAndroid.Dialogs.MidiFollowingEnableDialog;
import com.PP.LunarTabsAndroid.Dialogs.SetHomeDirectoryDialog;
import com.PP.LunarTabsAndroid.Dialogs.StomperEnableDialog;
import com.PP.LunarTabsAndroid.Dialogs.VoiceActionsDialog;
import com.example.lunartabsandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;


public class HandsFreeSettingsPrefActivity extends PreferenceActivity {
	
	//on create
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
    	//theme
//    	this.setTheme(android.R.style.Theme_Black);        
    	
    	//create
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment(this)).commit();
    }
    
    //fragment for preferences
    public static class MyPreferenceFragment extends PreferenceFragment
    {
    	
    	protected Activity parent;
    	
    	public MyPreferenceFragment(Activity parent) {
    		this.parent = parent;
    	}
    	
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
        	
        	//create
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.hands_free_settings);
            
            //set up voice actions preference
            final CheckBoxPreference dialogPreference = (CheckBoxPreference) getPreferenceScreen().findPreference("enable_voice_actions_pref");
            if(dialogPreference!=null) {
	            dialogPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                public boolean onPreferenceClick(Preference preference) {
	                	 
	                	 //get whether just enabled or disabled
		            	 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(preference.getContext().getApplicationContext());
		            	 boolean currentlyEnabled = sharedPrefs.getBoolean("enable_voice_actions_pref", false);
		            	 if(currentlyEnabled) {
		            		 
		            		 //keep disabled for now
		            		 dialogPreference.setChecked(false);
		            		 		            		 
		            		 //just tried to turn voice on
		            		 VoiceActionsDialog dialog = new VoiceActionsDialog(dialogPreference);
		            		 dialog.show(parent.getFragmentManager(),"LOZ");		            		 
		            	 }
		            	 else {
		            		 Log.d("V", "TURNED VOICE OFF");		            		 
		            	 }
		            	 
		            	 //finish
		            	 return true;
	                }
	            });            
            }
            
            //set up stomp mode preference
            final CheckBoxPreference stompModePreference = (CheckBoxPreference) getPreferenceScreen().findPreference("enable_stomp_mode_pref");
            if(stompModePreference!=null) {
	            stompModePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                public boolean onPreferenceClick(Preference preference) {
	                	 
	                	 //get whether just enabled or disabled
		            	 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(preference.getContext().getApplicationContext());
		            	 boolean currentlyEnabled = sharedPrefs.getBoolean("enable_stomp_mode_pref", false);
		            	 if(currentlyEnabled) {
		            		 
		            		 //keep disabled for now
		            		 stompModePreference.setChecked(false);
		            		 		            		 
		            		 //just tried to turn on stomper
		            		 StomperEnableDialog dialog = new StomperEnableDialog(stompModePreference.getContext(),stompModePreference);
		            		 dialog.show();		            		 
		            	 }
		            	 else {
		            		 Log.d("V", "TURNED STOMPER OFF");		            		 
		            	 }
		            	 
		            	 //finish
		            	 return true;
	                }
	            });            
            }
            
            //set up calibrate stomp mode preference
            final Preference calibrateStompModePreference = (Preference) getPreferenceScreen().findPreference("calibrate_stomp_mode_pref");
            if(calibrateStompModePreference!=null) {
	            calibrateStompModePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                public boolean onPreferenceClick(Preference preference) {
	                	
	                	//launch calibrate stomp mode activity
	            		Intent i = new Intent(calibrateStompModePreference.getContext(), StomperCalibActivity.class);
	            		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            		startActivity(i);												
		            	 
		            	//finish
		            	return true;
	                }
	            });  
            }
            
            //set up enable midi following preference
            final CheckBoxPreference midiFollowingPreference = (CheckBoxPreference) getPreferenceScreen().findPreference("enable_midi_following_pref");
            if(midiFollowingPreference!=null) {
	            midiFollowingPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                public boolean onPreferenceClick(Preference preference) {
	                	 
	                	 //get whether just enabled or disabled
		            	 SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(preference.getContext().getApplicationContext());
		            	 boolean currentlyEnabled = sharedPrefs.getBoolean("enable_midi_following_pref", false);
		            	 if(currentlyEnabled) {
		            		 
		            		 //keep disabled for now
		            		 midiFollowingPreference.setChecked(false);
		            		 		            		 
		            		 //just tried to turn on midi following
		            		 MidiFollowingEnableDialog dialog = new MidiFollowingEnableDialog(midiFollowingPreference.getContext(),midiFollowingPreference);
		            		 dialog.show();		            		 
		            	 }
		            	 else {
		            		 Log.d("V", "TURNED MIDI FOLLOWING OFF");		            		 
		            	 }
		            	 
		            	 //finish
		            	 return true;
	                }
	            });            
            }
            
        }
    }
}
