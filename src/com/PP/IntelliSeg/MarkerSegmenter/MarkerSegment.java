package com.PP.IntelliSeg.MarkerSegmenter;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.UI.DataModel;

public class MarkerSegment extends Segment {

	//marker Text
	protected String markerText;
	
	public MarkerSegment(int start, int end, String markerText) {
		super(start, end);
		this.markerText = markerText;
	}

	@Override
	public void play() {
		DataModel dataModel = DataModel.getInstance();
		TuxGuitarUtil.playClip(dataModel.getSong(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum(), dataModel.getTempoScale());		
	}

	@Override
	public String getTitlePresentation() {
		if(markerText!=null && !markerText.equals("")) {
			return "(" + markerText + ")";	
		}
		else {
			return "(M" + (getStart()+1) + "-" + "M" + (getEnd()+1) + ")";
		}
	}
}