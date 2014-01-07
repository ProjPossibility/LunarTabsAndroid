package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.List;
import org.herac.tuxguitar.song.models.TGMeasure;
import android.util.SparseArray;

public class RepeatInstructionGenerator {
	
	/**
	 * Returns a list of repeat instructions to append to measures 
	 * if repeats exist.
	 * @param measures The measures to generate repeat instructions for
	 * @return Repeat Instructions
	 */
	public static SparseArray<String> getRepeatInstructions(List<TGMeasure> measures) {
		SparseArray<String> rtn = new SparseArray<String>();
		int lastOpen = 1;
		for(int x=0; x < measures.size(); x++) {
			TGMeasure measure = measures.get(x);
			if(measure.isRepeatOpen()) {
				lastOpen = (x+1);
			}
			if(measure.getRepeatClose()!=0) {
				String instruction = "";
				if(lastOpen==(x+1)) {
					instruction = "Repeat measure " + measure.getRepeatClose() + " times.";
				}
				else {
					if(measure.getRepeatClose()==1) {
						instruction = "Repeat measures " + lastOpen + " to " + (x+1)+ ".";
					}
					else {
						instruction = "Repeat measures " + lastOpen + " to " + (x+1) + " " + Words.getInstance(measure.getRepeatClose()).getNumberInWords() + "more times.";						
					}
				}				
				rtn.put((x+1), instruction);
			}
		}
		return rtn;
	}
}