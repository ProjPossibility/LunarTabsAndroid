package com.PP.StompDetector;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.DataModel;

import android.app.Activity;

public class InstructionStomp implements StompListener  {

	protected MainActivity mainActivity;
	
	public InstructionStomp(Activity parent) {
		mainActivity = (MainActivity) parent;
	}

	@Override
	public void trigger_callback(double ts) {
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
			mainActivity.nextInstruction();
		}
	}

}
