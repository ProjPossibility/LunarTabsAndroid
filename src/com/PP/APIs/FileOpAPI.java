package com.PP.APIs;

import java.io.File;

import android.os.Environment;

public class FileOpAPI {
	
	//global storage path
	public static final String SAVE_PATH_DIR = "/Android/data/com.PP.LunarTabsAndroid/files/";
	public static final String SAVE_PATH = Environment.getExternalStoragePublicDirectory(SAVE_PATH_DIR).getPath() + "/";
	
	//temporary files created for playback
	public static final String TEMP_GP4 = "tmp.gp4";
	public static final String TEMP_MID = "tmp.mid";
	
	//model files
	public static final String GUI_MODEL_FILE = "MOD.tmp";
	public static final String STOMPER_MODEL_FILE = "STOMPER_MOD.tmp";
	
	/**
	 * Called to init directory structure.
	 */
	public static void init() {
		File f = Environment.getExternalStoragePublicDirectory(SAVE_PATH_DIR);
		if(!f.exists()) {
			boolean created = f.mkdirs();
		}
	}
}