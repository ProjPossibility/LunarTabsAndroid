package com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

import android.util.Log;

import com.PP.APIs.TuxGuitarUtil;
import com.PP.InstructionGenerator.DrumInstructionGenerator;
import com.PP.InstructionGenerator.GuitarInstructionGenerator;
import com.PP.InstrumentModels.ChordRecognizer;
import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Instruction;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base.CrochemoreSegment;
import com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base.CrochemoreSolver;
import com.PP.IntelliSeg.Util.SelStruct;
import com.PP.IntelliSeg.Util.SelectionFunction;
import com.PP.IntelliSeg.Util.StringRepr;

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
			List<Instruction> instructions = new ArrayList<Instruction>();
			for(int z=0; z < measures.size(); z++) {
				//generate playing instructions for beats
				List<TGBeat> beats = measures.get(z).getBeats();
				for(int x=0; x < beats.size(); x++) {
					String i1="";
					String i2="";
					String i3="";
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
			Segment seg = new CrochemoreSegment(start,end);
			seg.setInstructions(instructions);
			rtn.add(seg);
			
		}
		return rtn;
	}
	
}