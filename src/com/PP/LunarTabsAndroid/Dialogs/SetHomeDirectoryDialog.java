package com.PP.LunarTabsAndroid.Dialogs;

import java.io.File;

import org.herac.tuxguitar.song.models.TGSong;

import android.app.Dialog;
import android.content.Context;

import com.PP.LunarTabsAndroid.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.SerializedParams;
import com.daidalos.afiledialog.FileChooserDialog;
import com.example.lunartabsandroid.R;

public class SetHomeDirectoryDialog extends FileChooserDialog {

	public SetHomeDirectoryDialog(final Context context, final MainActivity mainActivity) {
		super(context,SerializedParams.getInstance().getHomeDir());
		setCanCreateFiles(false);
		setFolderMode(true);
	    addListener(new FileChooserDialog.OnFileSelectedListener() {
	         @Override
			public void onFileSelected(Dialog source, File file) {
	        	 
	        	 //finish dialog
	             source.dismiss();
	             	             
	             //attempt directory set and save params
	             try {
	            	 
	            	 //do stuff
	            	 String homeDir = file.getPath();
	            	 SerializedParams.getInstance().setHomeDir(homeDir);
	            	 SerializedParams.getInstance().saveInstance();
	            	 
	            	 //say success
	            	 String HOME_DIR_SET_SUCCESS = context.getString(R.string.HOME_DIR_SET_SUCCESS);
	            	 TextToSpeechAPI.speak(HOME_DIR_SET_SUCCESS);
	            	 
	             }
	             catch(Exception e) {
	            	 e.printStackTrace();
	             }	             
	         }
	         @Override
			public void onFileSelected(Dialog source, File folder, String name) {}
	     });		    
		
	}

}
