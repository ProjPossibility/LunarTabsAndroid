package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.List;

import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordRecognizer;
import com.PP.LunarTabsAndroid.InstrumentModels.GuitarModel;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGNote;

public class GuitarInstructionGenerator extends InstructionGenerator {
	
	//constants
	public static String[] langMod = {"Open","First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth","Eleventh", "Twelfth", "Thirteenth", "Fourteenth", "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth", "Twentieth", "Twenty-first", "Twenty-second", "Twenty-third", "Twenty-fourth", "Twenty-fifth", "Twenty-sixth", "Twenty-seventh", "Twenty-eight", "Twenty-ninth", "Thirtieth", "Thirty-first","Thirty-second","Thirty-third","Thirty-fourth","Thirty-fifth","Thirty-sixth","Thirty-seventh","Thirty-eigth","Thirty-nineth","Fortieth"};
				
	//singleton pattern
	private static GuitarInstructionGenerator instance;
	
	/**
	 * ctr
	 */
	protected GuitarInstructionGenerator(){}
	
	/**
	 * Singleton
	 * @return
	 */
	public static GuitarInstructionGenerator getInstance() {
		if(instance==null) {
			instance = new GuitarInstructionGenerator();
		}
		return instance;
	}
	
	
	/*
	 * Get play instruction
	 */
	@Override
	public String getPlayInstruction(TGBeat beat) {
		if(beat.isRestBeat()) {
			return getDurationInstruction(beat);
		}
		else {
			List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);		
			if(notes.size() > 1) {
				return ChordRecognizer.getChordName(notes) + " chord, " + getDurationInstruction(beat) + ".";
			}
			else {
				TGNote singleNote = notes.get(0);
				int string = singleNote.getString();
				int fret = singleNote.getValue()+1;
				return GuitarModel.getInstance().getNoteName(string, fret)[0].replaceAll("#", "-sharp") + ". " + getDurationInstruction(beat) + ".";
			}
		}
	}
	
	/*
	 * Generates instructions of the form (string, fret).
	 */
	public String getStringFretInstruction(TGBeat beat) {
		if(beat.isRestBeat()) {
			return getDurationInstruction(beat);
		}
		else {
		
			//loop for notes
			List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
			String instruction = "";
			for(TGNote note : notes) {
				
				//get string, fret
				int string = note.getString();
				int fret = note.getValue();
				
				//keep from crashing if some crazy high note
				if(string >= langMod.length)
					string = langMod.length-1;
				if(fret >= langMod.length)
					fret = langMod.length - 1;
				
				//create instruction
				instruction = instruction + " " + langMod[string] + " string, " + langMod[fret] + " fret. ";
			}
			
			//add duration
			instruction = instruction + getDurationInstruction(beat);
			
			//rtn
			return instruction;		
		}
	}
	
	/*
	 * Generates instructions using condensed form.
	 */
	public String getCondensedInstruction(TGBeat beat) {
		if(beat.isRestBeat()) {
			return getDurationInstruction(beat);
		}
		else {
			
			//init frets array
			int[] frets = new int[6];
			for(int x=0; x < 6; x++) {
				frets[x] = -1;
			}
			
			//loop for notes
			List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
			for(TGNote note : notes) {
				
				//get string, fret
				int string = note.getString();
				int fret = note.getValue();
				
				//keep from crashing if some crazy high note
				if(string >= langMod.length)
					string = langMod.length-1;
				if(fret >= langMod.length)
					fret = langMod.length - 1;
				
				//store
				frets[string-1] = fret;				
			}
			
			String instruction="";
			for(int x=5; x >= 0; x--) {
				
				//display modifier
				if(frets[x]==-1) {
					instruction = instruction + "-";
				}
				else {
					instruction = instruction + frets[x];
				}
				
				if(x==0) {
					instruction = instruction + ". ";
				}
				else {
					instruction = instruction  + ",";
				}
			}
			
			//add duration
			instruction = instruction + getDurationInstruction(beat);
			
			//rtn
			return instruction;		
		}
	}
}
