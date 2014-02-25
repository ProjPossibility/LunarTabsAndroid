package com.PP.InstrumentModels;

public class GuitarModel {

	//consts
	public static final String[] MUSIC_NOTES_SHARP = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public static final String[] MUSIC_NOTES_FLAT = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
	
	//state
	protected int[] tuning;
	
	//singleton
	public static GuitarModel instance;
	
	//ctr
	private GuitarModel() {
		tuning = getStandardTuning();
	}
	
	//singleton pattern
	public static GuitarModel getInstance() {
		if(instance==null) {
			instance = new GuitarModel();
		}
		return instance;
	}
	
	//input string 1-6, fret number 1-...
	//returns both sharp and flat name	
	public String[] getNoteName(int string, int fret) {
		
		//find string note index
		int stringStart = tuning[string-1];
		int index = stringStart + (fret-1);
		index = index % MUSIC_NOTES_SHARP.length;
		
		//get names
		String n1 = MUSIC_NOTES_SHARP[index];
		if(n1.indexOf("#") > -1) {
			String n2 = MUSIC_NOTES_FLAT[index];
			return new String[] {n1,n2};
		}
		else {
			return new String[] {n1};
		}		
	}
	
	public int getNoteIndex(String note) {
		if(note.indexOf("b") > -1) {
			
			//search flats
			for(int x=0; x < MUSIC_NOTES_FLAT.length; x++) {
				if(note.equalsIgnoreCase(MUSIC_NOTES_FLAT[x]))
					return x;
			}
			
		}
		else {

			//search sharps
			for(int x=0; x < MUSIC_NOTES_SHARP.length; x++) {
				if(note.equalsIgnoreCase(MUSIC_NOTES_SHARP[x]))
					return x;
			}
			
		}
		
		//not found
		return -1;
	}
	
	public int[] getStandardTuning() {

		//corresponds to indices of notes
		int[] rtn = {4, 11, 7, 2, 9, 4};
		return rtn;
	}
		
}
