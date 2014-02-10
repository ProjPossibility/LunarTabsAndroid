package com.PP.IntelliSeg.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;

import com.PP.APIs.TuxGuitarUtil;

public class StringRepr {
	
	//consts
	public static final int NOTE_STRING = 1;
	public static final int DUR_STRING = 2;
	public static final int TEMPO_STRING = 3;
	public static final int TIME_SIG_STRING = 4;
		
	/**
	 * Get strings for particular tracks.
	 * @param song The song
	 * @param type The type of beat strings to generate
	 * @return Track name --> Beat String
	 */
	public static Map<String,List<String>> getStringsforTracks(TGSong song, int type) {
				
		//extract all measures for tracks		
		Map<String, List<TGMeasure>> trackMeasures = TuxGuitarUtil.extractAllMeasures(song, 0, song.countTracks()-1);
		
		//rtn
		Map<String,List<String>> rtn = new HashMap<String,List<String>>();
		for(String trackStr : trackMeasures.keySet()) {
			List<TGMeasure> measureList = trackMeasures.get(trackStr);
			List<String> melStr = getBeatString(measureList, type);
			rtn.put(trackStr, melStr);
		}
		
		return rtn;
	}
		
	/**
	 * 
	 * Generate beat strings for list of measures
	 * 
	 * @param mList List of measures
	 * @param type What type of beat string to generate
	 * @return Beat String
	 */
	public static List<String> getBeatString(List<TGMeasure> mList, int type) {
		List<String> rtn = new ArrayList<String>();
		for(int x=0; x < mList.size(); x++) {
			TGMeasure m = mList.get(x);
			for(Object b : m.getBeats()) {
				TGBeat beat = (TGBeat)b;
				String beatStr = beatToString(beat,m,type);				
				rtn.add(beatStr);
			}
		}
		return rtn;
	}
	
	public static List<List<String>> getBeatStringPerMeasure(List<TGMeasure> mList, int type) {
		List<List<String>> rtn = new ArrayList<List<String>>();
		for(int x=0; x < mList.size(); x++) {
			TGMeasure m = mList.get(x);
			List<String> toAdd = new ArrayList<String>();
			for(Object b : m.getBeats()) {
				TGBeat beat = (TGBeat) b;
				toAdd.add(beatToString(beat,m,type));
			}
			rtn.add(toAdd);
		}
		return rtn;
	}
	
	/**
	 * Converts beat object to string
	 * @param beat
	 * @param m
	 * @param type
	 * @return
	 */
	public static String beatToString(TGBeat beat, TGMeasure m, int type) { 
		String beatStr="";
		
		//get pitch (i.e. note) string
		if(type==StringRepr.NOTE_STRING) {
			for(int y=0; y < beat.countVoices(); y++) {
				TGVoice v = beat.getVoice(y);
				for(Object n : v.getNotes()) {
					TGNote note = (TGNote) n;
					beatStr = beatStr + getNoteID(note) + ",";
				}					
			}
		}
		
		//get duration string
		if(type==StringRepr.DUR_STRING) {
			for(int y=0; y < beat.countVoices(); y++) {
				TGVoice v = beat.getVoice(y);
				beatStr = beatStr + v.getDuration().getTime() + ",";
			}					
		}
		
		//get tempo string
		if(type==StringRepr.TEMPO_STRING) {
			for(int y=0; y < beat.countVoices(); y++) {
				beatStr = beatStr + m.getTempo().getValue();
			}
		}
		
		//get time signature  string
		if(type==StringRepr.TIME_SIG_STRING) {
			for(int y=0; y < beat.countVoices(); y++) {
				beatStr = beatStr + m.getKeySignature();
			}					
		}
						
		//remove last comma
		if(beatStr.length() > 0) {
			beatStr = beatStr.substring(0,beatStr.length()-1); 
		}
		return beatStr;
		
	}
	
	/**
	 * Hash function for guitar notes
	 * @param n
	 * @return
	 */
	public static String getNoteID(TGNote n) {
		int string = n.getString() - 1;
		int fret = n.getValue();
		int[] fretStarts = {25,20,16,11,6,1}; 
		int val = fretStarts[string] + fret; //unique hash for each note on the guitar
		return "" + val;
	}
	
	public static String getBeatStringSequence(TGTrack track, int type) {
		
		//get beat strings
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(track);
		List<String> beatStrs= getBeatString(measures,type);
		
		//create melody string
		StringBuffer rtn = new StringBuffer();
		int elemCnt=0;
		Map<String,Character> elemHashTbl = new HashMap<String,Character>(); 
		for(String elem : beatStrs) {
			String hash = elemHash(elem);
			char c;
			if(elemHashTbl.containsKey(hash)) {
				c = elemHashTbl.get(hash);
			}
			else {
				c = (char) (elemCnt+64);
				elemHashTbl.put(hash,c);
				elemCnt++;
			}
			rtn.append(c);
		}
		
		return rtn.toString();
	}

	public static String getMeasureStringSequence(TGTrack track, int type) {
		
		//get beat strings
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(track);
		List<List<String>> measStrs= getBeatStringPerMeasure(measures,type);
		
		//create melody measure string
		StringBuffer rtn = new StringBuffer();
		int elemCnt=0;
		Map<String,Character> measHashTbl = new HashMap<String,Character>(); 
		for(List<String> measStr : measStrs) {
			String measHash = "";
			for(String elem : measStr) {
				String hash = elemHash(elem);
				measHash = measHash + hash;
			}
			char c;
			if(measHashTbl.containsKey(measHash)) {
				c = measHashTbl.get(measHash);
			}
			else {
				c = (char) (elemCnt+64);
				measHashTbl.put(measHash,c);
				elemCnt++;
			}
			rtn.append(c);
		}
		
		return rtn.toString();
	}

	public static String elemHash(String elem) {
		elem = elem.trim();
		String[] toks = elem.split(",");
		List<Integer> frets = new ArrayList<Integer>();
		for(String tok : toks) {
			tok = tok.trim();
			if(!tok.equals("")) {
				int parsed = Integer.parseInt(tok);
				frets.add(parsed);
			}
		}
		Collections.sort(frets);
		StringBuffer rtn = new StringBuffer();
		for(int f : frets) {
			rtn.append(f + ",");
		}
		String rtnStr = rtn.toString();
		return rtnStr;
	}
}