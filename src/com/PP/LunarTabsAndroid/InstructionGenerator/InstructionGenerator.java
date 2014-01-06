package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.*;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGVoice;

import android.util.Log;

import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;

public abstract class InstructionGenerator {
			
	/*
	 * Returns an instruction about the duration of the beat.
	 */
	public static String getDurationInstruction(TGBeat beat) {

		//get duration of beat
		TGVoice voice = beat.getVoice(0);
		TGDuration duration = voice.getDuration();
		
		//determine whether tied note
		String tiedMod = "";
		List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
		for(TGNote note : notes) {
			if(note.isTiedNote()) {
				tiedMod = "tied";
			}
		}
		
		//determine whether dotted
		String dottedMod = "";
		if(duration.isDotted()) {
			dottedMod = "dotted";
		}
		
		//determine whether double dotted
		String doubleDottedMod = "";
		if(duration.isDoubleDotted()) {
			doubleDottedMod = "double dotted";
		}
		
		//create mod string
		String mod = "";
		mod = mod + tiedMod + " ";
		mod = mod + dottedMod + " ";
		mod = mod + doubleDottedMod + " ";
		mod = mod.trim();
		if(!mod.equals("")) {
			mod = mod + " ";
		}
				
		if(beat.isRestBeat())
		{
			if(duration.getValue() == TGDuration.EIGHTH)
			{
				return "Rest, " + mod + "eighth note.";
			}
			if(duration.getValue() == TGDuration.HALF)
			{
				return "Rest, " + mod+ "half note.";
			}
			if(duration.getValue() == TGDuration.QUARTER)
			{
				return "Rest, " + mod + "quarter note.";
			}
			if(duration.getValue() == TGDuration.SIXTEENTH)
			{
				return "Rest, " + mod + "sixteenth note.";
			}
			if(duration.getValue() == TGDuration.SIXTY_FOURTH)
			{
				return "Rest, " + mod + "sixty-fourth note.";
			}
			if(duration.getValue() == TGDuration.THIRTY_SECOND)
			{
				return "Rest, " + mod + "thirty-second note.";
			}
			if(duration.getValue() == TGDuration.WHOLE)
			{
				return "Rest, " + mod + "whole note.";
			}

		}
		else
		{
			if(voice.getNotes().size() > 1)
			{
				if(duration.getValue() == TGDuration.EIGHTH)
				{
					return mod + "eighth note";
				}
				if(duration.getValue() == TGDuration.HALF)
				{
					return mod + "half note";
				}
				if(duration.getValue() == TGDuration.QUARTER)
				{
					return mod + "quarter note";
				}
				if(duration.getValue() == TGDuration.SIXTEENTH)
				{
					return mod + "sixteenth note";
				}
				if(duration.getValue() == TGDuration.SIXTY_FOURTH)
				{
					return mod + "sixty-fourth note";
				}
				if(duration.getValue() == TGDuration.THIRTY_SECOND)
				{
					return mod + "thirty-second note";
				}
				if(duration.getValue() == TGDuration.WHOLE)
				{
					return mod + "whole note";
				}
			}
			else
			{
				if(duration.getValue() == TGDuration.EIGHTH)
				{
					return mod + "eighth note";
				}
				if(duration.getValue() == TGDuration.HALF)
				{
					return mod + "half note";
				}
				if(duration.getValue() == TGDuration.QUARTER)
				{
					return mod + "quarter note";
				}
				if(duration.getValue() == TGDuration.SIXTEENTH)
				{
					return mod + "sixteenth note";
				}
				if(duration.getValue() == TGDuration.SIXTY_FOURTH)
				{
					return mod + "sixty-fourth note";
				}
				if(duration.getValue() == TGDuration.THIRTY_SECOND)
				{
					return mod + "thirty-second note";
				}
				if(duration.getValue() == TGDuration.WHOLE)
				{
					return mod + "whole note";
				}
			}
		}
		
		//no instruction - error
		return null;
	}
	
	public String getNoteEffectInstruction(TGBeat beat) {
		
		//get unique note effects
		List<String> noteEffects = new ArrayList<String>();
		List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
		for(TGNote note : notes) {
			List<String> effects = getNoteEffectsInstructions(note);
			for(String effect : effects) {
				if(!noteEffects.contains(effect)) {
					noteEffects.add(effect);
				}
			}
		}
		
		//create instruction
		if(noteEffects.size()==0) {
			return "";
		}
		else {
			StringBuffer rtn = new StringBuffer();
			rtn.append("Play with ");
			if(noteEffects.size()==1) {
				rtn.append(noteEffects.get(0) + ".");
			}
			else if(noteEffects.size()==2) {
				rtn.append(noteEffects.get(0) + " and " + noteEffects.get(1) + ".");
			}
			else {
				for(int x=0; x < noteEffects.size(); x++) {
					String effect = noteEffects.get(x);
					if(x!=(noteEffects.size()-1)) {
						rtn.append(effect + ", ");
					}
					else {
						rtn.append("and " + effect + ".");
					}
				}
			}
			Log.d("NOTE EFFECT STR", rtn.toString());
			return rtn.toString();
		}
	}
	
	/*
	 * Returns instructions about the effect modifiers of the note.
	 */
	public static List<String> getNoteEffectsInstructions(TGNote n) {
		
		//rtn structure
		List<String> rtn = new ArrayList<String>();
		
		//create instructions for effects
		if(n.getEffect().hasAnyEffect())
		{
			if(n.getEffect().isAccentuatedNote())
			{
				rtn.add("accentuated emphasis");
			}
			if(n.getEffect().isBend())
			{
				rtn.add("pitch bend effect");
			}
			if(n.getEffect().isDeadNote())
			{
				rtn.add("dead note effect");
			}
			if(n.getEffect().isFadeIn())
			{
				rtn.add("fade in");
			}
			if(n.getEffect().isGhostNote())
			{
				rtn.add("ghost note effect");
			}
			if(n.getEffect().isGrace())
			{
				rtn.add("grace note effect");
			}
			if(n.getEffect().isHammer())
			{
				rtn.add("hammer-on");
			}
			if(n.getEffect().isHarmonic())
			{
				rtn.add("harmonic effect");
			}
			if(n.getEffect().isHeavyAccentuatedNote())
			{
				rtn.add("heavy accentuated emphasis");
			}
			if(n.getEffect().isPalmMute())
			{
				rtn.add("palm mute");
			}
			if(n.getEffect().isPopping())
			{
				rtn.add("pop");
			}
			if(n.getEffect().isSlapping())
			{
				rtn.add("slap");
			}
			if(n.getEffect().isSlide())
			{
				rtn.add("slide");
			}
			if(n.getEffect().isTremoloBar())
			{
				rtn.add("tremolo bar");
			}
			if(n.getEffect().isStaccato())
			{
				rtn.add("stacatto");
			}
			if(n.getEffect().isTapping())
			{
				rtn.add("tap effect");
			}
			if(n.getEffect().isTremoloPicking())
			{
				rtn.add("tremolo picking");
			}
			if(n.getEffect().isTrill())
			{
				rtn.add("trill");
			}
			if(n.getEffect().isVibrato())
			{
				rtn.add("vibrato");
			}
		}
		
		return rtn;
	}
	
	//abstract methods
	public abstract String getPlayInstruction(TGBeat beat, int offset);
	
}
