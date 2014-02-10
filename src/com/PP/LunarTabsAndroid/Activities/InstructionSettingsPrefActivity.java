package com.PP.LunarTabsAndroid.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.PP.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.Dialogs.MeasureIncrementDialog;
import com.PP.LunarTabsAndroid.Dialogs.SelectSectionDialog;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;


public class InstructionSettingsPrefActivity extends PreferenceActivity {
		
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
            addPreferencesFromResource(R.xml.instruction_settings);
      
            //set up set instruction increment 
            final Preference dialogPreference = getPreferenceScreen().findPreference("set_section_increment_pref");
            if(dialogPreference!=null) {
	            dialogPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                @Override
					public boolean onPreferenceClick(Preference preference) {
	                	MeasureIncrementDialog d = new MeasureIncrementDialog(parent);
	                	d.show(parent.getFragmentManager(), "LOZ");
	                	return true;
	                }
	            });            
            }
            
            //set up go to section
            final Preference goToPreference = getPreferenceScreen().findPreference("go_to_pref");
            if(goToPreference!=null) {
	            goToPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	                @Override
					public boolean onPreferenceClick(Preference preference) {
	                	
	                	//if on loaded tab
	                	DataModel dataModel = DataModel.getInstance();
	        	    	if(dataModel.getSong()!=null && 
	        	    			dataModel.getInstSegments()!=null && 
	        	    			dataModel.getTrackNum()!=-1 && dataModel.getCurrentSegment()!=-1 && 
	        	    			dataModel.getInstSegments().get(dataModel.getCurrentSegment())!=null) {        	    			
        	    		
	        	    		//show selection dialog 
		                	SelectSectionDialog d = new SelectSectionDialog();
		                	d.show(parent.getFragmentManager(), "LOZ");
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