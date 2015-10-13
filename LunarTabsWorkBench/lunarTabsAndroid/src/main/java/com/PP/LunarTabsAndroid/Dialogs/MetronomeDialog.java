package com.PP.LunarTabsAndroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;

import com.example.lunartabsandroid.R;

public class MetronomeDialog extends DialogFragment {
	
	//menu item to relabel
	protected CheckBoxPreference checkBox;
	
	public MetronomeDialog(CheckBoxPreference checkBox) {
		this.checkBox = checkBox;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.metronome_explanation)
        	   .setTitle(R.string.metronome_title)
               .setNegativeButton(R.string.VoiceActionsNo, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	   //update data model
                	   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                	   Editor e =sharedPrefs.edit();
                	   e.putBoolean("enable_voice_activated_metronome_pref", false);
                	   e.commit();
                	   
                	   //update checkbox
                	   checkBox.setChecked(false);
                	   
                   }
                   
                })
               .setPositiveButton(R.string.OK_button_text, new DialogInterface.OnClickListener() {
        		@Override
				public void onClick(DialogInterface dialog, int id) {
	         	                   	                   	   
	         	   //update data model
	         	   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	         	   Editor e =sharedPrefs.edit();
	         	   e.putBoolean("enable_voice_activated_metronome_pref", true);
	         	   e.commit();
	         	                   	   
	         	   //update checkbox
	         	   checkBox.setChecked(true);
	         	   
	            }
        	});

        // Create the AlertDialog object and return it
        return builder.create();
    }
}