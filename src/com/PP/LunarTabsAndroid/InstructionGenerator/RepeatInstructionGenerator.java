package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.util.List;

import org.herac.tuxguitar.song.models.TGMeasure;

import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
					instruction = ResourceModel.getInstance().REPEAT_MEASURE+" " + measure.getRepeatClose() + " "+ResourceModel.getInstance().TIMES+".";
				}
				else {
					if(measure.getRepeatClose()==1) {
						instruction = ResourceModel.getInstance().REPEAT_MEASURES+" " + lastOpen + " "+ResourceModel.getInstance().TO+" " + (x+1)+ ".";
					}
					else {
						instruction = ResourceModel.getInstance().REPEAT_MEASURES+" " + lastOpen + " "+ResourceModel.getInstance().TO+" " + (x+1) + " " + Words.getInstance(measure.getRepeatClose()).getNumberInWords() + ResourceModel.getInstance().MORE_TIMES+".";
					}
				}				
				rtn.put((x+1), instruction);
			}
		}
		return rtn;
	}
	
	/**
	 * Returns a list of repeat counts indexed by measure.
	 * @param measures The measures to generate repeat counts for
	 * @return Repeat Counts
	 */
	public static SparseIntArray getNumRepeats(List<TGMeasure> measures) {
		SparseIntArray rtn = new SparseIntArray();
		int lastOpen = 1;
		for(int x=0; x < measures.size(); x++) {
			TGMeasure measure = measures.get(x);
			if(measure.isRepeatOpen()) {
				lastOpen = (x+1);
			}
			if(measure.getRepeatClose()!=0) {
				for(int ind=lastOpen; ind <= (x+1); ind++) {
					int priorCount=0;
					if(rtn.get(ind)!=0) {
						priorCount = rtn.get(ind);
					}
					rtn.put(ind, priorCount + measure.getRepeatClose());
				}
			}
		}
		return rtn;
	}
}