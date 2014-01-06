package com.PP.IntelliSeg.Abstract;

import java.io.Serializable;
import java.util.List;

import org.herac.tuxguitar.song.models.TGTrack;


public abstract class AbstractSegmenter implements Serializable {
	
	//abstract segmentation function for a track
	public abstract List<Segment> segment(TGTrack t); 

}
