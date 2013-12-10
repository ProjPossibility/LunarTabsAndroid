package com.PP.LunarTabsAndroid.UI;

public class SpeechConst {
	
	//file loading speech feedback
	public static final String FILE_LOADED = "File successfully loaded. Choose an instrument track.";
	public static final String ERROR_FILE_NOT_LOADED = "Error: File could not be loaded.";
	
	//feedback on changing sections
	public static final String ERROR_NO_DATA = "Error: Current track contains no data.";
	public static final String ERROR_NO_FILE_LOADED = "Error: No Tab File Loaded. Please load a tab file first.";
	public static final String ERROR_FIRST_SECTION = "Error: You are at the beginning of the track. No previous section.";
	public static final String ERROR_LAST_SECTION = "Error: You are at the end of the track. No more sections.";
	public static final String ERROR_NO_INST_SELECTED = "Error: No Instruction Selected.";
	
	//feedback on changing instructions
	public static final String ERROR_NO_PREV_INST = "No previous instructions in section.";
	public static final String ERROR_NO_NEXT_INST = "No more instructions in section.";
	
	//voice activation command params	
	public static final String[] voiceCommands = {"Toggle","Play","Next","Back","Up","Down","Sample"};
	public static final int ACTIVATOR_DELAY = 5000;
	
}

