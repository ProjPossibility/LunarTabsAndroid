package com.PP.LunarTabsAndroid.InstrumentModels;
import java.util.*;

import android.util.Log;

import com.tuxguitar.song.models.TGNote;

public class ChordRecognizer {
			
	public static String getChordName(List<TGNote> notes) {
		
		//convert notes to name of notes list
		List<String> chordNotes = getChordNoteNames(notes);
		
		//get unique elements and sort input into lex order
		chordNotes = ListUtil.unique(chordNotes);
		Collections.sort(chordNotes);
		
		//look up in hash table first
		String chordHash = ChordDB.chordHash(chordNotes);
		if(ChordDB.getInstance().getChordHashToName().containsKey(chordHash)) {
			return ChordDB.getInstance().getChordHashToName().get(chordHash);
		}
		else {
			
			//else do approximate matching
			String bestMatch  = getClosestMatch(chordNotes);			
			
			//store solution in hash table for efficient use later
			ChordDB.getInstance().getChordHashToName().put(chordHash, bestMatch);
			
			//return
			return bestMatch;
			
		}		
	}
	
	protected static List<String> getChordNoteNames(List<TGNote> notes) {
		List<String> rtn = new ArrayList<String>();
		for(int x=(notes.size()-1); x >= 0; x--) {
			TGNote note = notes.get(x);
			String[] noteNames = GuitarModel.getInstance().getNoteName(note.getString(),note.getValue()+1);
			rtn.add(noteNames[0]);
		}
		return rtn;
	}
	
	protected static String getClosestMatch(List<String> chordNotes) {
				
		//debug
		/*
		String rtn="";
		for(String str : chordNotes) {
			rtn = rtn+ " " + str;
		}
		*/
		
		//get db (init if not inited)
		List<List<String>> chordDB = ChordDB.getInstance().getChordNotes();
		List<String> chordNames = ChordDB.getInstance().getChordNames();
		
		//compute scores
		double[] scores = new double[chordDB.size()];
		for(int x=0; x < chordDB.size(); x++) {
			List<String> chord = chordDB.get(x);
			
			//compute match and mismatch scores and ratio
			double match_score = ListUtil.computeMatchScore(chordNotes,chord);
			double mismatch_score = (chordNotes.size()-match_score) + (chord.size()-match_score);
			scores[x] = match_score / mismatch_score;
			
		}
		
		//find max
		double maxScore=scores[0];
		String bestMatch = chordNames.get(0);
		for(int x=1; x < scores.length; x++) {
			if(scores[x] > maxScore) {
				maxScore = scores[x];
				bestMatch = chordNames.get(x);
			}
		}
		
		//return best match
		return bestMatch;
//		return rtn + " : " + bestMatch + " : " + maxScore;
	}	
}