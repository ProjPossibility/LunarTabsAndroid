package com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.UI.DataModel;

public class SMRSegment extends Segment {
	
	/**
	 * Start set for segment 
	 */
	protected Set<Integer> startSet;
	
	/**
	 * Instantiate
	 * @param start
	 * @param end
	 */
	public SMRSegment(int start, int end, Set<Integer> startSet) {
		super(start, end);
		this.startSet = startSet;
	}
	
	@Override
	public void play() {
		DataModel dataModel = DataModel.getInstance();
		TuxGuitarUtil.playClip(dataModel.getFilePath(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum());				
	}
	
	@Override
	public String getTitlePresentation() {
		/*
		StringBuffer rtn = new StringBuffer();
		rtn.append("M (");
		List<Integer> list = new ArrayList<Integer>(startSet);
		Collections.sort(list);
		for(int occ : list) {
			rtn.append(occ);
			rtn.append(",");
		}
		rtn.deleteCharAt(rtn.length()-1);
		rtn.append(")");
		return rtn.toString();
		*/
		return "(M" + this.getStart() + " x" + startSet.size() + ")";
	}
	
}
