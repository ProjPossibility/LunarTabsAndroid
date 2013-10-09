package com.PP.LunarTabsAndroid.FileOp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;
import android.os.Environment;

public class FileOp {
	
	//global storage path
	public static final String SAVE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

	//temporary files created for playback
	public static final String TEMP_GP4 = "tmp.gp4";
	public static final String TEMP_MID = "tmp.mid";
	
	//model file
	public static final String GUI_MODEL_FILE = "MOD.tmp";
	
	/*
	 * Serialize model to file.
	 */
	public static void writeModel(GUIDataModel m, String file) {
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
	
	public static GUIDataModel readModel(String file) {
		try {
			String path = SAVE_PATH+file;		  
			File f = new File(path);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream is = new ObjectInputStream(fis);
			GUIDataModel rtn = (GUIDataModel) is.readObject();
			is.close();
			return rtn;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}