package com.PP.LunarTabsAndroid.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.PP.StompDetector.StompDetector;
import com.example.lunartabsandroid.R;

public class StomperEnableDialog extends Dialog {

	public StomperEnableDialog(Context context, final StompDetector stomper, final MainActivity mainActivity, final MenuItem menuItem) {
		super(context);
		setContentView(R.layout.stomper_dialog_layout);
		Button okButton = (Button) findViewById(R.id.OK_BUTTON_STOMPER_DIALOG);
		Button cancelButton = (Button) findViewById(R.id.CANCEL_BUTTON_STOMPER_DIALOG);
		setTitle(ResourceModel.getInstance().STOMPER_ENABLE_DIALOG_TITLE);	 
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
	    		//enable stomper
	    		stomper.start();					
	    		
				//change text on menu item
				menuItem.setTitle(ResourceModel.getInstance().DISABLE_STOMP_MODE);				
	    		
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
