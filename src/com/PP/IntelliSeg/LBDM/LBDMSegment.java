package com.PP.IntelliSeg.LBDM;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;

public class LBDMSegment extends Segment {

	/**
	 * Ctr
	 * @param start
	 * @param end
	 */
	public LBDMSegment(int start, int end) {
		super(start, end);
	}

	@Override
	public void play() {
		GUIDataModel dataModel = GUIDataModel.getInstance();
		TuxGuitarUtil.playClip_beats(dataModel.getFilePath(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum());		
	}

	@Override
	public String getTitlePresentation() {
		return "(M " + (getStart()+1) + "-" + "M" + (getEnd()+1) + ")";
	}
	
}
