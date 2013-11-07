package com.PP.LunarTabsAndroid.StompDetector;

import com.PP.LunarTabsAndroid.APIs.VibratorAPI;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;

import android.app.Activity;

public class StompDetectorImpl extends StompDetector {

	protected MainActivity mainActivity;
	
	public StompDetectorImpl(Activity parent) {
		super(parent);
		mainActivity = (MainActivity) parent;
	}

	@Override
	protected void trigger_callback() {
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
			mainActivity.nextInstruction();
		}
	}

}
