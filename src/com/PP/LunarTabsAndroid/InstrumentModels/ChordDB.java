package com.PP.LunarTabsAndroid.InstrumentModels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class ChordDB {
	
	//db structures
	private List<List<String>> chordNotes;
	private List<String> chordNames;
	private Map<String,String> chordHashToName;
	
	/**
	 * Singleton pattern
	 */
	private ChordDB() {}
	private static ChordDB instance;
	public static ChordDB getInstance() {
		if(instance==null) {
			instance = new ChordDB();
			instance.generateChordDB();
		}
		return instance;
	}

	
	/**
	 * Debug function for contents of database.
	 */
	public void debugDump() {
		for(int x=0; x < chordNotes.size(); x++) {
			Log.d("DUMP", chordNames.get(x) + " : " + chordNotes.get(x));
		}
	}
		
	/**
	 * Chord formulas
	 * @return
	 */
	protected List<int[]> getFormulas() {
		
		//create formulas
		
		//evil -- incorrect solution
		//LET THIS BE A LESSON TO COMPUTATIONAL MUSIC STUDENTS!!!!
		//A MAJOR 2 is DIFFERENT FROM A MINOR 2.
		//HOW TO REALLY SCREW UP A SOFTWARE RELEASE! RIGHT HERE!
		/*
		int[] MAJOR_CHORD = {1,3,5};
		int[] MINOR_CHORD = {1,2,5};
		int[] ADD4_CHORD = {1,3,4,5};
		int[] D7_CHORD = {1,3,5,6};
		int[] DIM_CHORD = {1,2,4};
		*/
		
		//correct solution
		int[] MAJOR = {0,4,7};
		int[] SUS = {0,5,7};				
		int[] MINOR = {0,3,7};
		int[] AUG = {0,4,8};
		int[] DIM = {0,3,6};
		int[] MAJOR_6TH = {0,4,7,9};
		int[] MAJOR_7TH = {0,4,7,11};
		int[] DOM_7TH = {0,4,7,10};
		int[] DOM_7TH_SUS4 = {0,5,7,10};
		int[] MINOR_6TH = {0,3,7,9}; 
		int[] MINOR_MAJ7 = {0,4,7,11};
		int[] MINOR_7TH = {0,3,7,10};
		int[] MAJ_7TH_N5 = {0,4,8,11};
		int[] AUG_7TH = {0,4,8,10};
		int[] MIN_7TH_b5 = {0,3,6,10};
		int[] DIM_7TH = {0,3,6,9};
		int[] ADD4 = {0,4,5,7};
		
		//add to structure
		List<int[]> rtn = new ArrayList<int[]>();
		rtn.add(MAJOR);
		rtn.add(SUS);
		rtn.add(MINOR);
		rtn.add(AUG);
		rtn.add(DIM);
		rtn.add(MAJOR_6TH);
		rtn.add(MAJOR_7TH);
		rtn.add(DOM_7TH);
		rtn.add(DOM_7TH_SUS4);
		rtn.add(MINOR_6TH);
		rtn.add(MINOR_MAJ7);
		rtn.add(MINOR_7TH);
		rtn.add(MAJ_7TH_N5);
		rtn.add(AUG_7TH);
		rtn.add(MIN_7TH_b5);
		rtn.add(DIM_7TH);
		rtn.add(ADD4);
		return rtn;
	}
	
	/**
	 * Descriptors for chord formulas
	 * @return
	 */
	protected String[] getDescriptors() {
		String[] descriptors = {"major", "sus", "minor", "aug", "dim", "major 6th", 
				"major 7th", "dom 7th", "dom 7th (sus 4)", "minor 6th", "minor (maj 7)",
				"minor 7th", "Major 7th (#5)", "aug 7th",
				"minor 7th (b5)", "dim 7th", "add 4"};		
		return descriptors;
	}
	
	/**
	 * Hash function for chords
	 * @param chordNotes
	 * @return Hash
	 */
	public static String chordHash(List<String> chordNotes) {
//		chordNotes = ListUtil.unique(chordNotes);
//		Collections.sort(chordNotes);
		StringBuffer rtn = new StringBuffer();
		for(int x=0; x < chordNotes.size(); x++) {
			rtn.append(chordNotes.get(x));
		}
		return rtn.toString();
	}
	
	/**
	 * Init chord database.
	 */
	public void generateChordDB() {
		chordNotes = new ArrayList<List<String>>();
		chordNames = new ArrayList<String>();
		chordHashToName = new HashMap<String,String>();
		List<int[]> formulas = getFormulas();
		for(int x=0; x < GuitarModel.MUSIC_NOTES_SHARP.length; x++) {
			for(int y=0; y < formulas.size(); y++) {
				
				//generate data
				String chordName = GuitarModel.MUSIC_NOTES_SHARP[x].replaceAll("#", "-Sharp") + " " + getDescriptors()[y];
				List<String> notesInChord = generateChord(GuitarModel.MUSIC_NOTES_SHARP[x],formulas.get(y),getDescriptors()[y]);
				String chordHash = chordHash(notesInChord);
				
				//store in database
				chordNames.add(chordName);
				chordNotes.add(notesInChord);
				chordHashToName.put(chordHash, chordName);
				
			}
		}
	}
	
	public List<String> generateChord(String rootNote, int[] formula, String descriptor) {
		
		//find root note index
		int rootIndex = GuitarModel.getInstance().getNoteIndex(rootNote);
		
		//create list of notes in chord
		List<String> rtn = new ArrayList<String>();
		for(int x=0; x < formula.length; x++) {
			int index = (formula[x]+rootIndex) % GuitarModel.MUSIC_NOTES_SHARP.length;
			rtn.add(GuitarModel.MUSIC_NOTES_SHARP[index]);
		}
		
		//maintain data in lex sorted order
		rtn = ListUtil.unique(rtn);
		Collections.sort(rtn);
		
		//return
		return rtn;
	}

	/**
	 * @return the chordNotes
	 */
	public List<List<String>> getChordNotes() {
		return chordNotes;
	}

	/**
	 * @return the chordNames
	 */
	public List<String> getChordNames() {
		return chordNames;
	}

	/**
	 * @return the chordHashToName
	 */
	public Map<String, String> getChordHashToName() {
		return chordHashToName;
	}
	
}