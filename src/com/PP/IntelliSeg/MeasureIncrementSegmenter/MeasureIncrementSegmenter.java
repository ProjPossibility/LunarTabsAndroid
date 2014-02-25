package com.PP.IntelliSeg.MeasureIncrementSegmenter;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

import android.util.SparseArray;

import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Instruction;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstructionGenerator.DrumInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.GuitarInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.RepeatInstructionGenerator;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordRecognizer;

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
		
		//get capo offset
		int offset = track.getOffset();
		
		//init return
		List<Segment> rtn = new ArrayList<Segment>();
		
		//init current segment structures
		List<Instruction> instructions = new ArrayList<Instruction>();
		int start=0;
		int end=0;
		int incCnt=0;

		//get repeat measure instructions
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(track);		
		SparseArray<String> repeatInstructions = RepeatInstructionGenerator.getRepeatInstructions(measures);
		
		//iterate through measures and generate segments
		for(int y=0; y < measures.size(); y++) {
			
			//get measure and generate instructions for it
			TGMeasure measure = measures.get(y);
			List<TGBeat> beats = measure.getBeats();
			for(int x=0; x < beats.size(); x++) {
				String i1="";
				String i2="";
				String i3="";
				TGBeat b = beats.get(x);
				if(track.isPercussionTrack()) {
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
			incCnt++;
			
			//add repeat instruction (if exists)
			if(repeatInstructions.get((y+1))!=null) {
				Instruction inst = new Instruction(Instruction.REPEAT_INSTRUCTION);
				String repeatInst = repeatInstructions.get((y+1));
				inst.setChordInst(repeatInst);
				inst.setSfInst(repeatInst);
				instructions.add(inst);
			}
			
			//store segment and restart if gone through enough measures
			if(incCnt==increment) {
				
				//create and store segment
				end = y;
				Segment s = new MeasureIncrementSegment(start,end);
				s.setInstructions(instructions);
				rtn.add(s);
				
				//reset state
				instructions = new ArrayList<Instruction>();
				start = (y+1);
				incCnt=0;
			}
		}
		
		//do last one if needed
		if(start!=measures.size()) {
			end = measures.size()-1;
			Segment s = new MeasureIncrementSegment(start,end);
			s.setInstructions(instructions);
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
