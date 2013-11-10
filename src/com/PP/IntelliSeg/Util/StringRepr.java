package com.PP.IntelliSeg.Util;

import java.util.*;

import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGMeasure;
import com.tuxguitar.song.models.TGNote;
import com.tuxguitar.song.models.TGSong;
import com.tuxguitar.song.models.TGTrack;
import com.tuxguitar.song.models.TGVoice;

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
		Map<String, List<TGMeasure>> trackMeasures = extractAllMeasures(song, 0, song.countTracks()-1);
		
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
				
				//add
				rtn.add(beatStr);
			}
		}
		return rtn;
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
	
	/**
	 * Extract measures for particular track range
	 * @param song
	 * @param minTrack
	 * @param maxTrack
	 * @return
	 */
	public static Map<String, List<TGMeasure>> extractAllMeasures(TGSong song, int minTrack, int maxTrack) 
	{
		Map<String, List<TGMeasure>> rtn = new LinkedHashMap<String,List<TGMeasure>>();
		for(int x=minTrack; x <= maxTrack && x < song.countTracks(); x++) {
			TGTrack t = song.getTrack(x);
			String name = "T" + x + "_" + t.getChannel().getInstrument() + "_" + t.getName();
			List<TGMeasure> measures = extractMeasures(song, x, 0, t.countMeasures()-1);
			rtn.put(name, measures);
		}
		return rtn;
	}
	
	/**
	 * Extract measures from song for particular track
	 * @param song
	 * @param track
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<TGMeasure> extractMeasures(TGSong song, int track, int min, int max) {
		TGTrack t = song.getTrack(track);
		List<TGMeasure> rtn = new ArrayList<TGMeasure>();
		for(int x=min; x <= max; x++) {
			rtn.add(t.getMeasure(x));
		}
		return rtn;
	}
}