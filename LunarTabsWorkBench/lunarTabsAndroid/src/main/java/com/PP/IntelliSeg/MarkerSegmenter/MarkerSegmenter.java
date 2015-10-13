package com.PP.IntelliSeg.MarkerSegmenter;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

import com.PP.APIs.TuxGuitarUtil;
import com.PP.InstructionGenerator.DrumInstructionGenerator;
import com.PP.InstructionGenerator.GuitarInstructionGenerator;
import com.PP.InstrumentModels.ChordRecognizer;
import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Instruction;
import com.PP.IntelliSeg.Abstract.Segment;

public class MarkerSegmenter extends AbstractSegmenter {

	@Override
	public List<Segment> segment(TGTrack track) 
	{
		//get capo offset
		int offset = track.getOffset();
		
		//init return
		List<Segment> rtn = new ArrayList<Segment>();
		
		//get repeat measure instructions
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(track);
		
		//iterate through measures and generate segments
		String currentMarker="";		
		int cStart=0;
		List<Instruction> instructions = new ArrayList<Instruction>();		
		for(int y=0; y < measures.size(); y++) {
			
			//get measure and see if marker. If so,
			//add past and create new segment.
			TGMeasure measure = measures.get(y);
			if(measure.hasMarker()) {
				
				//add old segment
				if(y!=0) {
					MarkerSegment toAdd = new MarkerSegment(cStart,y-1,currentMarker);
					toAdd.setInstructions(instructions);
					rtn.add(toAdd);
				}
				
				//start new segment
				TGMarker marker = measure.getMarker();
				cStart = y;
				currentMarker = marker.getTitle();
				instructions = new ArrayList<Instruction>();		
			}
						
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
		}
		
		//do last one if needed
		if(cStart!=measures.size()) {
			MarkerSegment s = new MarkerSegment(cStart,measures.size()-1, currentMarker);
			s.setInstructions(instructions);
			rtn.add(s);			
		}		
		
		//return
		return rtn;
		
	}
}
