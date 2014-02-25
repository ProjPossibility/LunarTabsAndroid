package com.PP.InstructionGenerator;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGVoice;

import com.PP.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.UI.ResourceModel;

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
				tiedMod = ResourceModel.getInstance().TIED_MOD;
			}
		}
		
		//determine whether dotted
		String dottedMod = "";
		if(duration.isDotted()) {
			dottedMod = ResourceModel.getInstance().DOTTED_MOD;
		}
		
		//determine whether double dotted
		String doubleDottedMod = "";
		if(duration.isDoubleDotted()) {
			doubleDottedMod = ResourceModel.getInstance().DOUBLE_DOTTED_MOD;
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
		
		//get context parts
		String REST_STR =ResourceModel.getInstance().REST_STR; 
		String[] DURATIONS = ResourceModel.getInstance().DURATIONS;
		
		//instructions
		if(beat.isRestBeat())
		{
			if(duration.getValue() == TGDuration.EIGHTH)
			{
				return REST_STR + ", " + mod + DURATIONS[0] +".";
			}
			if(duration.getValue() == TGDuration.HALF)
			{
				return REST_STR+", " + mod+ DURATIONS[1]+".";
			}
			if(duration.getValue() == TGDuration.QUARTER)
			{
				return REST_STR+", " + mod + DURATIONS[2]+".";
			}
			if(duration.getValue() == TGDuration.SIXTEENTH)
			{
				return REST_STR+", " + mod + DURATIONS[3]+".";
			}
			if(duration.getValue() == TGDuration.SIXTY_FOURTH)
			{
				return REST_STR+", " + mod + DURATIONS[4]+".";
			}
			if(duration.getValue() == TGDuration.THIRTY_SECOND)
			{
				return REST_STR+", " + mod + DURATIONS[5]+".";
			}
			if(duration.getValue() == TGDuration.WHOLE)
			{
				return REST_STR+", " + mod + DURATIONS[6]+".";
			}

		}
		else
		{
			if(voice.getNotes().size() > 1)
			{
				if(duration.getValue() == TGDuration.EIGHTH)
				{
					return mod + DURATIONS[0];
				}
				if(duration.getValue() == TGDuration.HALF)
				{
					return mod + DURATIONS[1];
				}
				if(duration.getValue() == TGDuration.QUARTER)
				{
					return mod + DURATIONS[2];
				}
				if(duration.getValue() == TGDuration.SIXTEENTH)
				{
					return mod + DURATIONS[3];
				}
				if(duration.getValue() == TGDuration.SIXTY_FOURTH)
				{
					return mod + DURATIONS[4];
				}
				if(duration.getValue() == TGDuration.THIRTY_SECOND)
				{
					return mod + DURATIONS[5];
				}
				if(duration.getValue() == TGDuration.WHOLE)
				{
					return mod + DURATIONS[6];
				}
			}
			else
			{
				if(duration.getValue() == TGDuration.EIGHTH)
				{
					return mod + DURATIONS[0];
				}
				if(duration.getValue() == TGDuration.HALF)
				{
					return mod + DURATIONS[1];
				}
				if(duration.getValue() == TGDuration.QUARTER)
				{
					return mod + DURATIONS[2];
				}
				if(duration.getValue() == TGDuration.SIXTEENTH)
				{
					return mod + DURATIONS[3];
				}
				if(duration.getValue() == TGDuration.SIXTY_FOURTH)
				{
					return mod + DURATIONS[4];
				}
				if(duration.getValue() == TGDuration.THIRTY_SECOND)
				{
					return mod + DURATIONS[5];
				}
				if(duration.getValue() == TGDuration.WHOLE)
				{
					return mod + DURATIONS[6];
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
			String PLAY_WITH = ResourceModel.getInstance().PLAY_WITH;
			String AND = ResourceModel.getInstance().AND;
			rtn.append(PLAY_WITH + " ");
			if(noteEffects.size()==1) {
				rtn.append(noteEffects.get(0) + ".");
			}
			else if(noteEffects.size()==2) {
				rtn.append(noteEffects.get(0) + " "+AND+" " + noteEffects.get(1) + ".");
			}
			else {
				for(int x=0; x < noteEffects.size(); x++) {
					String effect = noteEffects.get(x);
					if(x!=(noteEffects.size()-1)) {
						rtn.append(effect + ", ");
					}
					else {
						rtn.append(AND+" " + effect + ".");
					}
				}
			}
			return rtn.toString();
		}
	}
	
	/*
	 * Returns instructions about the effect modifiers of the note.
	 */
	public List<String> getNoteEffectsInstructions(TGNote n) {
		
		//rtn structure
		List<String> rtn = new ArrayList<String>();
		String[] NOTE_EFFECTS = ResourceModel.getInstance().NOTE_EFFECTS;
		
		//create instructions for effects
		if(n.getEffect().hasAnyEffect())
		{
			if(n.getEffect().isAccentuatedNote())
			{
				rtn.add(NOTE_EFFECTS[0]);
			}
			if(n.getEffect().isBend())
			{
				rtn.add(NOTE_EFFECTS[1]);
			}
			if(n.getEffect().isDeadNote())
			{
				rtn.add(NOTE_EFFECTS[2]);
			}
			if(n.getEffect().isFadeIn())
			{
				rtn.add(NOTE_EFFECTS[3]);
			}
			if(n.getEffect().isGhostNote())
			{
				rtn.add(NOTE_EFFECTS[4]);
			}
			if(n.getEffect().isGrace())
			{
				rtn.add(NOTE_EFFECTS[5]);
			}
			if(n.getEffect().isHammer())
			{
				rtn.add(NOTE_EFFECTS[6]);
			}
			if(n.getEffect().isHarmonic())
			{
				rtn.add(NOTE_EFFECTS[7]);
			}
			if(n.getEffect().isHeavyAccentuatedNote())
			{
				rtn.add(NOTE_EFFECTS[8]);
			}
			if(n.getEffect().isPalmMute())
			{
				rtn.add(NOTE_EFFECTS[9]);
			}
			if(n.getEffect().isPopping())
			{
				rtn.add(NOTE_EFFECTS[10]);
			}
			if(n.getEffect().isSlapping())
			{
				rtn.add(NOTE_EFFECTS[11]);
			}
			if(n.getEffect().isSlide())
			{
				rtn.add(NOTE_EFFECTS[12]);
			}
			if(n.getEffect().isTremoloBar())
			{
				rtn.add(NOTE_EFFECTS[13]);
			}
			if(n.getEffect().isStaccato())
			{
				rtn.add(NOTE_EFFECTS[14]);
			}
			if(n.getEffect().isTapping())
			{
				rtn.add(NOTE_EFFECTS[15]);
			}
			if(n.getEffect().isTremoloPicking())
			{
				rtn.add(NOTE_EFFECTS[16]);
			}
			if(n.getEffect().isTrill())
			{
				rtn.add(NOTE_EFFECTS[17]);
			}
			if(n.getEffect().isVibrato())
			{
				rtn.add(NOTE_EFFECTS[18]);
			}
		}
		
		return rtn;
	}
	
	public String getLyricInstruction(TGBeat beat) {
		if(beat.getStoredLyric()!=null && !beat.getStoredLyric().trim().equals("")) {
			return ResourceModel.getInstance().LYRIC+": " + beat.getStoredLyric();
		}
		else {
			return null;
		}
	}
	
	//abstract methods
	public abstract String getPlayInstruction(TGBeat beat, int offset);
	
}