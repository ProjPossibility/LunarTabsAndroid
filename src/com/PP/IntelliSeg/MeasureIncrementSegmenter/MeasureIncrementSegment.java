package com.PP.IntelliSeg.MeasureIncrementSegmenter;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.UI.DataModel;

public class MeasureIncrementSegment extends Segment {

	/**
	 * Ctr
	 * @param start
	 * @param end
	 */
	public MeasureIncrementSegment(int start, int end) {
		super(start, end);
	}

	@Override
	public void play() {
		DataModel dataModel = DataModel.getInstance();
		TuxGuitarUtil.playClip(dataModel.getSong(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum(), dataModel.getTempoScale());		
	}

	@Override
	public String getTitlePresentation() {
		return "(M" + (getStart()+1) + "-" + "M" + (getEnd()+1) + ")";
	}
}
