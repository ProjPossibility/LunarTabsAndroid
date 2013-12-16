package com.PP.LunarTabsAndroid.Dialogs;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.MidiServer.MidiServer;
import com.PP.StompDetector.StompDetector;
import com.example.lunartabsandroid.R;
import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MidiFollowingEnableDialog extends Dialog {

	public MidiFollowingEnableDialog(Context context, final MainActivity activity, final MenuItem menuItem) {
		super(context);
		setContentView(R.layout.midi_follower_dialog_layout);
		Button okButton = (Button) findViewById(R.id.OK_BUTTON_MIDI_FOLLOWING_DIALOG);
		Button cancelButton = (Button) findViewById(R.id.CANCEL_BUTTON_MIDI_FOLLOWING_DIALOG);
		setTitle("Enable Midi Following");	 
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
	    		//enable midi follower
				MidiServer.getInstance().start();

				//change text on menu item
				String new_title = activity.getResources().getString(R.string.DisableMidiFollowing);
				menuItem.setTitle(new_title);				
				
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
