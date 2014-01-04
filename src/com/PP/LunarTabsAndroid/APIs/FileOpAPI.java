package com.PP.LunarTabsAndroid.APIs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.StomperParams;

import android.os.Environment;
import android.util.Log;

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
	
	/*
	 * Serialize model to file.
	 */
	public static void writeModel(StomperParams m, String file) {
		try {
			String path = SAVE_PATH+file;		  
			File f = new File(path);
			FileOutputStream fOut = new FileOutputStream(f,false);		
			ObjectOutputStream os = new ObjectOutputStream(fOut);
			os.writeObject(m);
			os.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static StomperParams readModel(String file) {
		try {
			String path = SAVE_PATH+file;		  
			File f = new File(path);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream is = new ObjectInputStream(fis);
			StomperParams rtn = (StomperParams) is.readObject();
			is.close();
			return rtn;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}