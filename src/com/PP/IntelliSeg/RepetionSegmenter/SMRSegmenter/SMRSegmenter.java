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
import android.util.SparseIntArray;

import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Instruction;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter.base.SMRSegment;
import com.PP.IntelliSeg.Util.SelStruct;
import com.PP.IntelliSeg.Util.SelectionFunction;
import com.PP.IntelliSeg.Util.StringRepr2;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstructionGenerator.DrumInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.GuitarInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.RepeatInstructionGenerator;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordRecognizer;

public class SMRSegmenter extends AbstractSegmenter {	
					
	@Override
	public List<Segment> segment(TGTrack t) {
				
		//convert to string representation
		String str_repr = StringRepr2.getTrackHashString(t);
				
		//count repetition of measures (based on disjoint occurrences in string)
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
		
		//get num repeats (based on repeat signs)
		SparseIntArray repeats = RepeatInstructionGenerator.getNumRepeats(TuxGuitarUtil.getMeasures(t));
		
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
			List<Instruction> instructions = new ArrayList<Instruction>();
			for(int z=0; z < measures.size(); z++) {
				//generate playing instructions for beats
				List<TGBeat> beats = measures.get(z).getBeats();
				for(int x=0; x < beats.size(); x++) {
					String i1;
					String i2;
					String i3;
					TGBeat b = beats.get(x);
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
					Instruction inst;
					if(i1.toLowerCase().indexOf("rest") > -1) {
						inst = new Instruction(Instruction.REST_INSTRUCTION);
					}
					else {
						inst = new Instruction(Instruction.PLAY_INSTRUCTION);
					}
					inst.setBeat(b);
					inst.setChordInst(i1);
					inst.setSfInst(i2);
					inst.setMatchTarget(i3);
					instructions.add(inst);
				}				
			}	
			
			//add segment
			SMRSegment seg = new SMRSegment(start,end,s.getStartSet());
			seg.setInstructions(instructions);
			seg.computeTotalRepeatCount(repeats);
			rtn.add(seg);
		}
		return rtn;
	}
}