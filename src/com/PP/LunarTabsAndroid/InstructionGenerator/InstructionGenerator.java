package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.*;

import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordRecognizer;
import com.PP.LunarTabsAndroid.InstrumentModels.GuitarModel;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGDuration;
import com.tuxguitar.song.models.TGNote;
import com.tuxguitar.song.models.TGVoice;

public abstract class InstructionGenerator {
			
	/*
	 * Returns an instruction about the duration of the beat.
	 */
	public static String getDurationInstruction(TGBeat beat) {

		//get duration of beat
		TGVoice voice = beat.getVoice(0);
		TGDuration duration = voice.getDuration();
				
		if(beat.isRestBeat())
		{
			if(duration.getValue() == TGDuration.EIGHTH)
			{
				return "Rest eighth note.";
			}
			if(duration.getValue() == TGDuration.HALF)
			{
				return "Rest half note.";
			}
			if(duration.getValue() == TGDuration.QUARTER)
			{
				return "Rest quarter note.";
			}
			if(duration.getValue() == TGDuration.SIXTEENTH)
			{
				return "Rest sixteenth note.";
			}
			if(duration.getValue() == TGDuration.SIXTY_FOURTH)
			{
				return "Rest sixty-fourth note.";
			}
			if(duration.getValue() == TGDuration.THIRTY_SECOND)
			{
				return "Rest thirty-second note.";
			}
			if(duration.getValue() == TGDuration.WHOLE)
			{
				return "Rest whole note.";
			}

		}
		else
		{
			if(voice.getNotes().size() > 1)
			{
				if(duration.getValue() == TGDuration.EIGHTH)
				{
					return "eighth note";
				}
				if(duration.getValue() == TGDuration.HALF)
				{
					return "half note";
				}
				if(duration.getValue() == TGDuration.QUARTER)
				{
					return "quarter note";
				}
				if(duration.getValue() == TGDuration.SIXTEENTH)
				{
					return "sixteenth note";
				}
				if(duration.getValue() == TGDuration.SIXTY_FOURTH)
				{
					return "sixty-fourth note";
				}
				if(duration.getValue() == TGDuration.THIRTY_SECOND)
				{
					return "thirty-second note";
				}
				if(duration.getValue() == TGDuration.WHOLE)
				{
					return "whole note";
				}
			}
			else
			{
				if(duration.getValue() == TGDuration.EIGHTH)
				{
					return "eighth note";
				}
				if(duration.getValue() == TGDuration.HALF)
				{
					return "half note";
				}
				if(duration.getValue() == TGDuration.QUARTER)
				{
					return "quarter note";
				}
				if(duration.getValue() == TGDuration.SIXTEENTH)
				{
					return "sixteenth note";
				}
				if(duration.getValue() == TGDuration.SIXTY_FOURTH)
				{
					return "sixty-fourth note";
				}
				if(duration.getValue() == TGDuration.THIRTY_SECOND)
				{
					return "thirty-second note";
				}
				if(duration.getValue() == TGDuration.WHOLE)
				{
					return "whole note";
				}
			}
		}
		
		//no instruction - error
		return null;
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
			if(n.isTiedNote())
			{
				rtn.add("This is a tied note.");
			}
			if(n.getEffect().isAccentuatedNote())
			{
				rtn.add("This is an accentuated note.");
			}
			if(n.getEffect().isBend())
			{
				rtn.add("This note has a bend.");
			}
			if(n.getEffect().isDeadNote())
			{
				rtn.add("This is a dead note.");
			}
			if(n.getEffect().isFadeIn())
			{
				rtn.add("You must fade in for this note.");
			}
			if(n.getEffect().isGhostNote())
			{
				rtn.add("This is a ghost note.");
			}
			if(n.getEffect().isGrace())
			{
				rtn.add("This is a grace note.");
			}
			if(n.getEffect().isHammer())
			{
				rtn.add("This note is a hammer-on.");
			}
			if(n.getEffect().isHarmonic())
			{
				rtn.add("This note is a harmonic.");
			}
			if(n.getEffect().isHeavyAccentuatedNote())
			{
				rtn.add("This is a heavey accentuated note.");
			}
			if(n.getEffect().isPalmMute())
			{
				rtn.add("This note requires palm muting.");
			}
			if(n.getEffect().isPopping())
			{
				rtn.add("This note requires popping.");
			}
			if(n.getEffect().isSlapping())
			{
				rtn.add("This note requires slapping.");
			}
			if(n.getEffect().isSlide())
			{
				rtn.add("This note has a slide.");
			}
			if(n.getEffect().isTremoloBar())
			{
				rtn.add("This note requires use of the Tremolo bar.");
			}
			if(n.getEffect().isStaccato())
			{
				rtn.add("This note is staccato.");
			}
			if(n.getEffect().isTapping())
			{
				rtn.add("This note requires tapping.");
			}
			if(n.getEffect().isTremoloPicking())
			{
				rtn.add("This note requires Tremolo picking.");
			}
			if(n.getEffect().isTrill())
			{
				rtn.add("This note is played with trilling.");
			}
			if(n.getEffect().isVibrato())
			{
				rtn.add("This note is vibrato.");
			}
		}
		
		return rtn;
	}
	
	
	//abstract methods
	public abstract String getPlayInstruction(TGBeat beat);
	
	
}
