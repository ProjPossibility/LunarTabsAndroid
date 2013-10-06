package com.PP.LunarTabsAndroid.InstrumentModels;
import java.util.*;
import com.tuxguitar.song.models.TGNote;

public class ChordRecognizer {
			
	public static String getChordName(List<TGNote> notes) {
		List<String> chordNotes = getChordNoteNames(notes);
		return getClosestMatch(chordNotes);
	}
	
	protected static List<String> getChordNoteNames(List<TGNote> notes) {
		List<String> rtn = new ArrayList<String>();
		for(int x=0; x < notes.size(); x++) {
			TGNote note = notes.get(x);
			String[] noteNames = GuitarModel.getInstance().getNoteName(note.getString(),note.getValue());
			rtn.add(noteNames[0]);
		}
		return rtn;
	}
	
	protected static String getClosestMatch(List<String> chordNotes) {
		ChordDBGenerator.getInstance();
		List<String> chordDB = ChordDBGenerator.getChordNotes();
		ChordDBGenerator.getInstance();
		List<String> chordNames = ChordDBGenerator.getChordNames();
		
		//compute scores
		int[] scores = new int[chordDB.size()];
		for(int x=0; x < chordDB.size();x++) {
			String chord = chordDB.get(x);
			int score = 0;
			for(int y=0; y < chordNotes.size(); y++) {
				if(chord.indexOf(chordNotes.get(y)) > -1) {
					score++;
				}
			}
			scores[x] = score;
		}
		
		//find max
		int maxScore=scores[0];
		String bestMatch = chordDB.get(0);
		for(int x=1; x < scores.length; x++) {
			if(scores[x] > maxScore) {
				maxScore = scores[x];
				bestMatch = chordNames.get(x);
			}
		}
		
		//return best match
		return bestMatch;
	}
		
}


class ChordDBGenerator {
	
	//guitar model and chord db
	private static ChordDBGenerator instance;
	private static List<String> chordNotes;
	private static List<String> chordNames;
	
	//chord formulas
	public List<int[]> getFormulas() {
		
		//create
		int[] MAJOR_CHORD = {1,3,5};
		int[] MINOR_CHORD = {1,2,5};
		int[] ADD4_CHORD = {1,3,4,5};
		int[] D7_CHORD = {1,3,5,6};
		int [] DIM_CHORD = {1,2,4};
		
		//add to structure
		List<int[]> rtn = new ArrayList<int[]>();
		rtn.add(MAJOR_CHORD);
		rtn.add(MINOR_CHORD);
		rtn.add(ADD4_CHORD);
		rtn.add(D7_CHORD);
		rtn.add(DIM_CHORD);
		return rtn;
	}
	
	public String[] getDescriptors() {
		String[] descriptors = {"major", "minor", "add4", "Dom7", "diminished"};
		return descriptors;
	}
			

	public static ChordDBGenerator getInstance() {
		if(instance==null) {
			instance = new ChordDBGenerator();
			instance.generateChordDB();
		}
		return instance;
	}
	
	public void generateChordDB() {
		chordNotes = new ArrayList<String>();
		chordNames = new ArrayList<String>();
		List<int[]> formulas = getFormulas();
		for(int x=0; x < GuitarModel.MUSIC_NOTES_SHARP.length; x++) {
			for(int y=0; y < formulas.size(); y++) {
				chordNotes.add(generateChord(GuitarModel.MUSIC_NOTES_SHARP[x],formulas.get(y),getDescriptors()[y]));
				chordNames.add(GuitarModel.MUSIC_NOTES_SHARP[x].replaceAll("#", "-Sharp") + " " + getDescriptors()[y]);
			}
		}
	}
	
	public String generateChord(String rootNote, int[] formula, String descriptor) {
		
		//find root note index
		int rootIndex = GuitarModel.getInstance().getNoteIndex(rootNote);
		
		//create string of notes in chord
		StringBuffer rtn = new StringBuffer();
		for(int x=0; x < formula.length; x++) {
			int index = (formula[x]-1+rootIndex) % GuitarModel.MUSIC_NOTES_SHARP.length;
			rtn.append(GuitarModel.MUSIC_NOTES_SHARP[index]);
		}
		
		//return
		return rtn.toString();
	}

	/**
	 * @return the chordNotes
	 */
	public static List<String> getChordNotes() {
		return chordNotes;
	}

	/**
	 * @param chordNotes the chordNotes to set
	 */
	public static void setChordNotes(List<String> chordNotes) {
		ChordDBGenerator.chordNotes = chordNotes;
	}

	/**
	 * @return the chordNames
	 */
	public static List<String> getChordNames() {
		return chordNames;
	}

	/**
	 * @param chordNames the chordNames to set
	 */
	public static void setChordNames(List<String> chordNames) {
		ChordDBGenerator.chordNames = chordNames;
	}	

}