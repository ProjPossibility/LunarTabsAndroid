package com.PP.LunarTabsAndroid.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;

public class StomperEnableDialog extends Dialog {

	public StomperEnableDialog(final Context context, final CheckBoxPreference stomperPreference) {
		super(context);
		setContentView(R.layout.stomper_dialog_layout);
		Button okButton = (Button) findViewById(R.id.OK_BUTTON_STOMPER_DIALOG);
		Button cancelButton = (Button) findViewById(R.id.CANCEL_BUTTON_STOMPER_DIALOG);
		setTitle(ResourceModel.getInstance().STOMPER_ENABLE_DIALOG_TITLE);	 
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					    		
			   //update data model
         	   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
         	   Editor e = sharedPrefs.edit();
         	   e.putBoolean("enable_stomp_mode_pref", true);
         	   e.commit();
         	                   	   
         	   //set checkbox
         	   stomperPreference.setChecked(true);
				
	    		//close dialog
				dismiss();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
         	   //update data so stomp mode preference is set false
         	   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
         	   Editor e =sharedPrefs.edit();
         	   e.putBoolean("enable_stomp_mode_pref", false);
         	   e.commit();
         	   stomperPreference.setChecked(false);
				
				//close dialog
				dismiss();
			}
		});
	}
}
