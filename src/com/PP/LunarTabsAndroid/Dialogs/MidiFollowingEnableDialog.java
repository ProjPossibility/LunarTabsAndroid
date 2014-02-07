package com.PP.LunarTabsAndroid.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.PP.MidiServer.MidiServer;
import com.example.lunartabsandroid.R;

public class MidiFollowingEnableDialog extends Dialog {

	public MidiFollowingEnableDialog(Context context, final MainActivity activity, final MenuItem menuItem) {
		super(context);
		setContentView(R.layout.midi_follower_dialog_layout);
		Button okButton = (Button) findViewById(R.id.OK_BUTTON_MIDI_FOLLOWING_DIALOG);
		Button cancelButton = (Button) findViewById(R.id.CANCEL_BUTTON_MIDI_FOLLOWING_DIALOG);
		setTitle(ResourceModel.getInstance().MIDI_FOLLOWING_DIALOG_TITLE);	 
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
	    		//enable midi follower
				MidiServer.getInstance().start();

				//change text on menu item
				menuItem.setTitle(ResourceModel.getInstance().DISABLE_MIDI_FOLLOWING);				
				
				//update gui for next available index
				activity.updateGUIForNextAvailableIndex();
	    		
	    		//close dialog
				dismiss();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}
}
