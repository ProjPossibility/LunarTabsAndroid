package com.PP.IntelliSeg.MeasureIncrementSegmenter;

import java.util.ArrayList;
import java.util.List;

import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstructionGenerator.DrumInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.GuitarInstructionGenerator;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGMeasure;
import com.tuxguitar.song.models.TGTrack;

/**
 * Segmenter for measure increments.
 * @author prateek
 *
 */
public class MeasureIncrementSegmenter extends AbstractSegmenter {

	//default params
	public static final int increment_DEFAULT = 1;
	
	//parameters
	protected int increment;
		
	//ctrs
	public MeasureIncrementSegmenter() {
		increment = increment_DEFAULT;
	}	
	public MeasureIncrementSegmenter(int increment) {
		this.increment = increment;
	}
	
	/**
	 * Implementation of segment.
	 * @param track The instrument to generate segments for
	 * @return
	 */
	@Override
	public List<Segment> segment(TGTrack track) {
		
		//init return
		List<Segment> rtn = new ArrayList<Segment>();
		
		//init current segment structures
		List<String> chordInst = new ArrayList<String>();
		List<String> sfI = new ArrayList<String>();
		List<TGBeat> beatsI = new ArrayList<TGBeat>();
		int start=0;
		int end=0;
		int incCnt=0;
		
		//iterate through measures and generate segments
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(track);		
		for(int y=0; y < measures.size(); y++) {
			
			//get measure and generate instructions for it
			TGMeasure measure = measures.get(y);
			List<TGBeat> beats = measure.getBeats();
			for(int x=0; x < beats.size(); x++) {
				String i1="";
				String i2="";
				TGBeat b = (TGBeat)beats.get(x);
				if(track.isPercussionTrack()) {
					i1 = DrumInstructionGenerator.getInstance().getPlayInstruction(b);
					i2 = i1;
				}
				else {
					i1 = GuitarInstructionGenerator.getInstance().getPlayInstruction(b);
//					i2 = GuitarInstructionGenerator.getInstance().getStringFretInstruction(b);
					i2 = GuitarInstructionGenerator.getInstance().getCondensedInstruction(b);					
				}
				chordInst.add(i1);
				sfI.add(i2);
				beatsI.add(b);
			}
			incCnt++;
			
			//store segment and restart if gone through enough measures
			if(incCnt==increment) {
				
				//create and store segment
				end = y;
				Segment s = new MeasureIncrementSegment(start,end);
				s.setSfInst(sfI);
				s.setChordInst(chordInst);
				s.setBeats(beatsI);
				rtn.add(s);
				
				//reset state
				chordInst = new ArrayList<String>();
				sfI = new ArrayList<String>();
				beatsI = new ArrayList<TGBeat>();
				start = (y+1);
				incCnt=0;
			}
		}
		
		//do last one if needed
		if(start!=measures.size()) {
			end = measures.size()-1;
			Segment s = new MeasureIncrementSegment(start,end);
			s.setSfInst(sfI);
			s.setChordInst(chordInst);
			s.setBeats(beatsI);
			rtn.add(s);			
		}
		
		//return
		return rtn;
		
	}

	/**
	 * @return the increment
	 */
	public int getIncrement() {
		return increment;
	}

	/**
	 * @param increment the increment to set
	 */
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	
}