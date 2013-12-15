package com.PP.LunarTabsAndroid.Dialogs;
import com.PP.MidiServer.MidiServer;
import com.PP.StompDetector.StompDetector;
import com.example.lunartabsandroid.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class MidiFollowingDialog extends Dialog {

	public MidiFollowingDialog(Context context) {
		super(context);
		setContentView(R.layout.midi_following_dialog_layout);
		setTitle("Midi Following");	 
		Button okButton = (Button) findViewById(R.id.OK_BUTTON_MIDI_FOLLOWING_DIALOG);
		Button cancelButton = (Button) findViewById(R.id.CANCEL_BUTTON_MIDI_FOLLOWING_DIALOG);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
	    		//start midi server
				MidiServer.getInstance().doStart();
	    		
	    		//close dialog
				dismiss();
			}
		});	 
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//close dialog
				dismiss();
				
			}
		});
	}
}
