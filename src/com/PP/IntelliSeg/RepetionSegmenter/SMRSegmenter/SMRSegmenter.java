package com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

import android.util.Log;

import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base.CrochemoreSolver;
import com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter.base.SMRSegment;
import com.PP.IntelliSeg.Util.SelStruct;
import com.PP.IntelliSeg.Util.SelectionFunction;
import com.PP.IntelliSeg.Util.StringRepr;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstructionGenerator.DrumInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.GuitarInstructionGenerator;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordRecognizer;

public class SMRSegmenter extends AbstractSegmenter {	
					
	@Override
	public List<Segment> segment(TGTrack t) {
		
		//convert to string representation
		String str_repr = StringRepr.getMeasureStringSequence(t,StringRepr.NOTE_STRING);
		
		//count repetition of measures
		Map<String,Set<Integer>> rep = new HashMap<String,Set<Integer>>();
		for(int x=0; x < str_repr.length(); x++) {
			String c = ""+str_repr.charAt(x);
			if(!rep.containsKey(c)) {
				Set<Integer> newSet = new HashSet<Integer>();
				newSet.add(x);
				rep.put(c, newSet);
			}
			else {
				Set<Integer> existingSet = rep.get(c);
				existingSet.add(x);
			}
		}
		List<String> grams = new ArrayList<String>(rep.keySet());
		List<Set<Integer>> startSets = new ArrayList<Set<Integer>>();
		for(String gram : grams) {
			startSets.add(rep.get(gram));
		}
		
		//apply selection function to solution
		List<SelStruct> sortedStructs = sortBySelFunction(grams,startSets,str_repr);
		
		//sort by first start
		Collections.sort(sortedStructs,SelStruct.getFirstOccurrenceComparator());
		
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
		
		//get offset
		int offset = t.getOffset();
		
		//segments loop
		List<Segment> rtn = new ArrayList<Segment>();
		for(int y=0; y < structs.size(); y++) {
			
			//get measures
			SelStruct s = structs.get(y);
			Log.d("TEST", "Score for y: "+s.getScore());
			List<Integer> startSet = new ArrayList<Integer>(s.getStartSet());
			Collections.sort(startSet);
			int start = startSet.get(0);
			int end = start+ s.getGram().length()-1;
			List<TGMeasure> measures = TuxGuitarUtil.extractMeasures_rtnMeas(t, start,end);

			List<String> chordInst = new ArrayList<String>();
			List<String> sfI = new ArrayList<String>();
			List<TGBeat> beatsI = new ArrayList<TGBeat>(); 
			List<String> targets = new ArrayList<String>();
			for(int z=0; z < measures.size(); z++) {
				//generate playing instructions for beats
				List<TGBeat> beats = measures.get(z).getBeats();
				for(int x=0; x < beats.size(); x++) {
					String i1;
					String i2;
					String i3;
					TGBeat b = (TGBeat)beats.get(x);
					if(t.isPercussionTrack()) {
						i1 = DrumInstructionGenerator.getInstance().getPlayInstruction(b,offset);
						i2 = i1;
						i3 = "";
					}
					else {
						i1 = GuitarInstructionGenerator.getInstance().getPlayInstruction(b,offset);
	//					i2 = GuitarInstructionGenerator.getInstance().getStringFretInstruction(b);
						i2 = GuitarInstructionGenerator.getInstance().getCondensedInstruction(b);					
						i3 = ChordRecognizer.getMatchTarget(b);
					}
					chordInst.add(i1);
					sfI.add(i2);
					targets.add(i3);					
					beatsI.add(b);
				}				
			}	
			
			//add segment
			Segment seg = new SMRSegment(start,end,s.getStartSet());
			seg.setSfInst(sfI);
			seg.setChordInst(chordInst);
			seg.setBeats(beatsI);
			seg.setMatchTargets(targets);
			rtn.add(seg);
		}
		return rtn;
	}
}