package com.PP.LunarTabsAndroid.Dialogs;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.StompDetector.StompDetector;
import com.example.lunartabsandroid.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class StomperEnableDialog extends Dialog {

	public StomperEnableDialog(Context context, final StompDetector stomper) {
		super(context);
		setContentView(R.layout.stomper_dialog_layout);
		setTitle("Stomp Mode");	 
		Button dialogButton = (Button) findViewById(R.id.OK_BUTTON_DIALOG);
		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
	    		//enable stomper
	    		stomper.start();						
	    		
	    		//close dialog
				dismiss();
			}
		});	 
	}
}
