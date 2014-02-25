package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.List;

import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.InstrumentModels.DrumModel;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGNote;

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
	public String getPlayInstruction(TGBeat beat) {
		if(beat.isRestBeat()) {
			return getDurationInstruction(beat);
		}
		else {
			List<TGNote> notes = TuxGuitarUtil.getNotesForBeat(beat);
			StringBuffer rtn = new StringBuffer();
			for(TGNote note : notes) {
				rtn.append(DrumModel.getDrumName(note.getValue()) + ", ");
			}
			rtn.append(getDurationInstruction(beat) + ".");
			return rtn.toString();
		}
	}
}
