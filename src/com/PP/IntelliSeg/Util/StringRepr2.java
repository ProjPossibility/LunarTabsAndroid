package com.PP.IntelliSeg.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;

import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;

public class StringRepr2 {
	
	public static String getTrackHashString(TGTrack t) {
		
		//init map and starting character
		Map<String, Character> elemHashMap = new HashMap<String,Character>();
		char nextChar = 'A';
		
		//loop to generate track hash string
		StringBuffer rtn = new StringBuffer();
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(t);
		for(TGMeasure measure : measures) {
			String measureHash = getMeasureHash(measure);
			char toUse;
			if(elemHashMap.containsKey(measureHash)) {
				toUse = elemHashMap.get(measureHash);
			}
			else {
				toUse = nextChar;
				elemHashMap.put(measureHash, toUse);
				nextChar++;
			}
			rtn.append(toUse);
		}
		
		//return
		return rtn.toString();
	}
	
	public static String getMeasureHash(TGMeasure measure) {
		
		//init
		HashBuffer rtn = new HashBuffer(HashBuffer.DEFAULT_DELIM + HashBuffer.DEFAULT_DELIM);
		
		//write measure settings
		rtn.append(measure.getClef());
		rtn.append(measure.getKeySignature());
//		rtn.append(measure.getRepeatClose());
		rtn.append(measure.getTripletFeel());
//		rtn.append(measure.isRepeatOpen());
		
		//write beats
		for(int x=0; x < measure.countBeats(); x++) {
			TGBeat b = measure.getBeat(x);
			rtn.append(getBeatHash(b));
		}
		
		//return
		return rtn.toString();
	}
	
	public static String getBeatHash(TGBeat beat) {
		
		//init
		HashBuffer rtn = new HashBuffer(HashBuffer.DEFAULT_DELIM);
		
		//write beat type settings to hash buffer
		rtn.append(beat.isChordBeat());
		rtn.append(beat.isRestBeat());
//		rtn.append(beat.isTextBeat());
		
		//get notes
		List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
		
		//sort notes based on note ID
		Collections.sort(notes,TGNote.getNoteIDComparator());
		
		//write notes to hash
		for(int x=0; x < notes.size(); x++) {
			
			//get note
			TGNote note = notes.get(x);
			
			//write note attributes to hash buffer
			rtn.append(note.isTiedNote());
			rtn.append(note.getString());
			rtn.append(note.getValue());
//			rtn.append(note.getVelocity());
			
			//write effects profile to hash buffer
			TGNoteEffect effect = note.getEffect();
			rtn.append(effect.hasAnyEffect());
			rtn.append(effect.isAccentuatedNote());
			rtn.append(effect.isBend());
			rtn.append(effect.isDeadNote());
			rtn.append(effect.isFadeIn());
			rtn.append(effect.isGhostNote());
			rtn.append(effect.isGrace());
			rtn.append(effect.isHammer());
			rtn.append(effect.isHarmonic());
			rtn.append(effect.isHeavyAccentuatedNote());
			rtn.append(effect.isLetRing());
			rtn.append(effect.isPalmMute());
			rtn.append(effect.isPopping());
			rtn.append(effect.isSlapping());
			rtn.append(effect.isSlide());
			rtn.append(effect.isStaccato());
			rtn.append(effect.isTapping());
			rtn.append(effect.isTremoloBar());
			rtn.append(effect.isTremoloPicking());			
			rtn.append(effect.isTrill());
			rtn.append(effect.isVibrato());
			
			//write voice and duration attributes to hash buffer
			TGVoice voice = note.getVoice();
			TGDuration duration = voice.getDuration();
//			rtn.append(voice.getDirection());
			rtn.append(duration.getValue());
			rtn.append(duration.isDotted());
			rtn.append(duration.isDoubleDotted());
		}
		
		//return
		return rtn.toString();
	}
	
	/**
	 * Hash function for guitar notes
	 * @param n
	 * @return
	 */
	public static int getNoteID(TGNote n) {
		int string = n.getString() - 1;
		int fret = n.getValue();
		int[] fretStarts = {25,20,16,11,6,1}; 
		int val = fretStarts[string] + fret; //unique hash for each note on the guitar
		return val;
	}
}
