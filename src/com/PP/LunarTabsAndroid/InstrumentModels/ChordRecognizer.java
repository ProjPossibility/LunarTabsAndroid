package com.PP.LunarTabsAndroid.InstrumentModels;
import java.util.*;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGNote;

import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;

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
	
	/**
	 * Function for getting match target out of beat.
	 * @param beat
	 * @return
	 */
	public static String getMatchTarget(TGBeat beat) {
		if(beat.isRestBeat()) {
			return "";
		}
		else {
			
			//get notes in beat
			List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);

			//convert notes to name of notes list			
			List<String> chordNotes = getChordNoteNames(notes);
			
			//get unique elements and sort input into lex order
			chordNotes = ListUtil.unique(chordNotes);
			Collections.sort(chordNotes);
			
			//convert to string
			String target = "";
			for(int x=0; x < chordNotes.size(); x++) {
				target = target + chordNotes.get(x) + " ";
			}
			
			//return
			return target.trim();
			
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
	
	/**
	 * Converts Midi hash to chord hash
	 * @param str
	 * @return
	 */
	public static String getChordHash(String str) {
		
		//remove numbers
		StringBuffer rtn = new StringBuffer();
		for(int x=0; x < str.length(); x++) {
			char c = str.charAt(x);
			if(!(c >= '0' && c<= '9')) {
				rtn.append(str.charAt(x));
			}
		}
		
		//tokenize and sort
		String[] toks = rtn.toString().split(" ");
		List<String> chordNotes = Arrays.asList(toks);
		chordNotes = ListUtil.unique(chordNotes);
		Collections.sort(chordNotes);
		
		//recreate hash
		String hash = "";
		for(int x=0; x < chordNotes.size(); x++) {
			hash = hash + chordNotes.get(x) + " ";
		}
		hash = hash.trim();
		
		//return
		return hash;
	}
	
	public static boolean robustMidiMatch(String played, String target) {
		
		//get parts
		String[] playedNotes = played.split(" ");
		String[] targetNotes = target.split(" ");
		
		//checks
		outer:for(String playedNote : playedNotes) {
			for(String targetNote : targetNotes) {
				
				//if target note, matched.
				if(targetNote.equalsIgnoreCase(playedNote)) {
					continue outer;
				}
				
				//if half-pertubation of target note, matched.
				int playedIndex = GuitarModel.getInstance().getNoteIndex(playedNote);
				int targetIndex = GuitarModel.getInstance().getNoteIndex(targetNote);
				int distance = Math.abs(playedIndex - targetIndex);
				if(distance==1 || distance==11) {
					continue outer;
				}
				
			}	
			
			//failed because not matched.
			return false;			
		}
		
		//success because all matched.
		return true;
		
	}

}