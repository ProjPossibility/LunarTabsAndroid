package com.PP.LunarTabsAndroid.UI;

public class InstructionContentDescription {
		
	public static String makeAccessibleInstruction(String instruction) {
		if(DataModel.getInstance().isVerbose()) {
			if(instruction.indexOf("-") > -1) {
				String DASH = 
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
