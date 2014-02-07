package com.PP.LunarTabsAndroid.InstrumentModels;

import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;

import android.content.Context;

public class DrumModel {
		
	/**
	 * Singleton instance
	 */
	private static String[] instance = null; 
	
	public static String[] getDrumModel() {
		if(instance==null) {
			instance = new String[88];
			for(int x=27; x < (27+ResourceModel.getInstance().DRUMS.length); x++) {
				instance[x] = ResourceModel.getInstance().DRUMS[x-27];
			}					
		}
		return instance;		
	}
	

	public static String getDrumName(int index) {
		if(index >= 27 && index <= 87)
			return DrumModel.getDrumModel()[index];
		else {
			return ResourceModel.getInstance().UNKNOWN_DRUM;
		}
			
	}
}
