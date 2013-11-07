package com.PP.LunarTabsAndroid.UI;

public class SpeechConst {

	//file loading speech feedback
	public static String FILE_LOADED = "File successfully loaded. Choose an instrument track.";
	public static String ERROR_FILE_NOT_LOADED = "Error: File could not be loaded.";
	
	//feedback on changing sections
	public static String ERROR_NO_DATA = "Error: Current track contains no data.";
	public static String ERROR_NO_FILE_LOADED = "Error: No Tab File Loaded. Please load a tab file first.";
	public static String ERROR_FIRST_SECTION = "Error: You are at the beginning of the track. No previous section.";
	public static String ERROR_LAST_SECTION = "Error: You are at the end of the track. No more sections.";
	
	//feedback on changing instructions
	public static String ERROR_NO_PREV_INST = "No previous instructions in section.";
	public static String ERROR_NO_NEXT_INST = "No more instructions in section.";
}
