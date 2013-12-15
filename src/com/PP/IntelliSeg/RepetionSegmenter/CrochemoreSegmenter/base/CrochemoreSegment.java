package com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.UI.DataModel;

public class CrochemoreSegment extends Segment {

	/**
	 * Instantiate
	 * @param start
	 * @param end
	 */
	public CrochemoreSegment(int start, int end) {
		super(start, end);
	}
	
	@Override
	public void play() {
		DataModel dataModel = DataModel.getInstance();
//		TuxGuitarUtil.playClip_beats(dataModel.getFilePath(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum());
		TuxGuitarUtil.playClip(dataModel.getFilePath(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum());				
	}
	
	@Override
	public String getTitlePresentation() {
		return "(M " + (getStart()+1) + "-" + "M" + (getEnd()+1) + ")";
	}

}
