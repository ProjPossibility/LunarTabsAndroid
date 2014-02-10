package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGNote;

import com.PP.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstrumentModels.DrumModel;

public class DrumInstructionGenerator extends InstructionGenerator {
	
	//singleton
	private static DrumInstructionGenerator instance;
	
	/*
	 * Ctr
	 */
	protected DrumInstructionGenerator() {}
	
	/**
	 * Singleton
	 * @return
	 */
	public static DrumInstructionGenerator getInstance() {
		if(instance==null) {
			instance = new DrumInstructionGenerator();
		}
		return instance;
	}
	
	/*
	 * Get play instruction
	 */
	@Override
	public String getPlayInstruction(TGBeat beat, int offset) {
		if(beat.isRestBeat()) {
			return getDurationInstruction(beat);
		}
		else {
			List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
			StringBuffer rtn = new StringBuffer();
			for(TGNote note : notes) {
				rtn.append(DrumModel.getDrumName(note.getValue()) + ", ");
			}
			rtn.append(getDurationInstruction(beat) + ". ");
			rtn.append(getNoteEffectInstruction(beat));
			return rtn.toString().trim();
		}
	}
}