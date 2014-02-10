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
import android.util.Log;
import android.view.MenuItem;

import com.PP.LunarTabsAndroid.APIs.WordActivatorAPI;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;

public class VoiceActionsDialog extends DialogFragment {
	
	//menu item to relabel
//	protected MenuItem menuItem;
	protected CheckBoxPreference checkBox;
	
	public VoiceActionsDialog(CheckBoxPreference checkBox) {
//		this.menuItem = menuItem;
		this.checkBox = checkBox;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.VoiceActionsText)
        	   .setTitle(R.string.VoiceActionsTitle)
               .setPositiveButton(R.string.VoiceActionsYes, new DialogInterface.OnClickListener() {
                   @Override
				public void onClick(DialogInterface dialog, int id) {
                	                   	   
                	   //start voice actions
                	   Log.d("V", "VOICE ACTIONS STARTED");
//                	   DataModel.getInstance().setVoiceActionsEnabled(true);
//                	   WordActivatorAPI.getInstance().start();
                	   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                	   Editor e =sharedPrefs.edit();
                	   e.putBoolean("enable_voice_actions_pref", true);
                	   e.commit();
                	                   	   
                	   //relabel menu item
                	   checkBox.setChecked(true);
//                	   menuItem.setTitle(ResourceModel.getInstance().DISABLE_VOICE_ACTIONS);
                	   
                   }
               })
               .setNegativeButton(R.string.VoiceActionsNo, new DialogInterface.OnClickListener() {
                   @Override
				public void onClick(DialogInterface dialog, int id) {
                	   
                	   //set false
                	   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                	   Editor e =sharedPrefs.edit();
                	   e.putBoolean("enable_voice_actions_pref", false);
                	   e.commit();
                	   checkBox.setChecked(false);
                	   
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}