package com.PP.StompDetector;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;

import android.app.Activity;

public class InstructionStomp implements StompListener  {

	protected MainActivity mainActivity;
	
	public InstructionStomp(Activity parent) {
		mainActivity = (MainActivity) parent;
	}

	@Override
	public void trigger_callback(double ts) {
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
			mainActivity.nextInstruction();
		}
	}

}
