package com.PP.LunarTabsAndroid.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.PP.LunarTabsAndroid.InstrumentModels.ListUtil;

import android.util.Log;

public class InstructionContentDescription {
	
	public static String makeAccessibleInstruction(String instruction) {
		if(DataModel.getInstance().isVerbose()) {
			if(instruction.indexOf("-") > -1) {
				instruction = instruction.replaceAll("-", "dash");
			}
		}
		else {
			if(instruction.indexOf("A")>-1) {
				instruction = instruction.replaceAll("A", "ae");
			}
		}
		return instruction;
	}
}
