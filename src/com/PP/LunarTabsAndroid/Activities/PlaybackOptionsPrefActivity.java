package com.PP.LunarTabsAndroid.Activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.PP.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.Dialogs.PlayTracksDialog;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;


public class PlaybackOptionsPrefActivity extends PreferenceActivity {
	
	//on create
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
    	//theme
//    	this.setTheme(android.R.style.Theme_Black);        
    	
    	//create
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }
    
    //fragment for preferences
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {        	
        	//create
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.playback_settings);
            
            //set up sel playback tracks
            final Preference dialogPreference = getPreferenceScreen().findPreference("sel_pbtracks_pref");
            if(dialogPreference!=null) {
	            dialogPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                @Override
					public boolean onPreferenceClick(Preference preference) {
	                	
	                	//if on loaded tab, show play tracks dialog.
	                	DataModel dataModel = DataModel.getInstance();
	        	    	if(dataModel.getSong()!=null && 
	        	    			dataModel.getInstSegments()!=null && 
	        	    			dataModel.getTrackNum()!=-1 && dataModel.getCurrentSegment()!=-1 && 
	        	    			dataModel.getInstSegments().get(dataModel.getCurrentSegment())!=null) {
	                	
	        	    		//show play tracks dialog
		                	PlayTracksDialog dialog = new PlayTracksDialog();
		                	dialog.show(getFragmentManager(), "LOZ");
	        	    	}
	        	    	else {
	        	    		
	        	    		//say no file loaded.
	        	    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
	        	    	}
	                    return true;
	                }
	            });            
            }
            
            
        }
    }
}