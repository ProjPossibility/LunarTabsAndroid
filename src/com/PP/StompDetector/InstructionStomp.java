package com.PP.StompDetector;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;

import android.app.Activity;

public class InstructionStomp extends StompDetector {

	protected MainActivity mainActivity;
	
	public InstructionStomp(Activity parent) {
		super(parent);
		mainActivity = (MainActivity) parent;
	}

	@Override
	protected void trigger_callback(double ts) {
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
			mainActivity.nextInstruction();
		}
	}

}
