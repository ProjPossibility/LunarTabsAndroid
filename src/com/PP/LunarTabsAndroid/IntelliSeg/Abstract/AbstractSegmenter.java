package com.PP.LunarTabsAndroid.IntelliSeg.Abstract;

import java.util.List;

import com.tuxguitar.song.models.TGTrack;

public abstract class AbstractSegmenter {
	
	//abstract segmentation function for a track
	public abstract List<Segment> segment(TGTrack t); 

}
