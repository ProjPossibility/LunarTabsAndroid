package com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base.CrochemoreSegment;
import com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base.CrochemoreSolver;
import com.PP.IntelliSeg.Util.SelStruct;
import com.PP.IntelliSeg.Util.SelectionFunction;
import com.PP.IntelliSeg.Util.StringRepr;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstructionGenerator.DrumInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.GuitarInstructionGenerator;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGMeasure;
import com.tuxguitar.song.models.TGTrack;

public class CrochemoreMeasSegmenter extends AbstractSegmenter {	
		
	//default params
	public static final int numShow_DEFAULT = 10;
	
	//params
	protected int numShow;
	
	public CrochemoreMeasSegmenter() {
		this.numShow = numShow_DEFAULT;
	}
	
	@Override
	public List<Segment> segment(TGTrack t) {
		
		//convert to string representation
		String str_repr = StringRepr.getMeasureStringSequence(t,StringRepr.NOTE_STRING);
		
		//plug into Crochemore solver
		Map<String,Set<Integer>> solution = CrochemoreSolver.seg(str_repr);
		List<String> grams = new ArrayList<String>(solution.keySet());
		List<Set<Integer>> startSets = new ArrayList<Set<Integer>>();
		for(String gram : grams) {
			startSets.add(solution.get(gram));
		}
		
		//apply selection function to solution
		List<SelStruct> sortedStructs = sortBySelFunction(grams,startSets,str_repr);
		
		//create segments
		List<Segment> rtn = structsToSegs(sortedStructs,t);
		return rtn;
	}
	
	public List<SelStruct> sortBySelFunction(List<String> grams, List<Set<Integer>> startSets,String str_rep) {
		List<SelStruct> rtn = new ArrayList<SelStruct>();
		for(int x=0; x < grams.size(); x++) {
			String gram = grams.get(x);
			Set<Integer> startSet = startSets.get(x);
			double score = SelectionFunction.score(gram, startSet,str_rep);
			SelStruct st = new SelStruct(gram,startSet,score);
			rtn.add(st);
		}
		Collections.sort(rtn,SelStruct.getScoreComparator());
		return rtn;
	}
	
	public List<Segment> structsToSegs(List<SelStruct> structs, TGTrack t) {
		
		//get ofset
		int offset = t.getOffset();
		
		//segments loop
		List<Segment> rtn = new ArrayList<Segment>();
		for(int y=0; y < numShow && y < structs.size(); y++) {
			
			//get measures
			SelStruct s = structs.get(y);
			Log.d("TEST", ""+s.getScore());
			List<Integer> startSet = new ArrayList<Integer>(s.getStartSet());
			int start = startSet.get(0);
			int end = start+ s.getGram().length()-1;
			List<TGMeasure> measures = TuxGuitarUtil.extractMeasures_rtnMeas(t, start,end);

			List<String> chordInst = new ArrayList<String>();
			List<String> sfI = new ArrayList<String>();
			for(int z=0; z < measures.size(); z++) {
				//generate playing instructions for beats
				List<TGBeat> beats = measures.get(z).getBeats();
				for(int x=0; x < beats.size(); x++) {
					String i1="";
					String i2="";
					TGBeat b = (TGBeat)beats.get(x);
					if(t.isPercussionTrack()) {
						i1 = DrumInstructionGenerator.getInstance().getPlayInstruction(b,offset);
						i2 = i1;
					}
					else {
						i1 = GuitarInstructionGenerator.getInstance().getPlayInstruction(b,offset);
	//					i2 = GuitarInstructionGenerator.getInstance().getStringFretInstruction(b);
						i2 = GuitarInstructionGenerator.getInstance().getCondensedInstruction(b);					
					}
					chordInst.add(i1);
					sfI.add(i2);
				}				
			}	
			
			//add segment
			Segment seg = new CrochemoreSegment(start,end);
			seg.setSfInst(sfI);
			seg.setChordInst(chordInst);
			rtn.add(seg);
			
		}
		return rtn;
	}
	
}