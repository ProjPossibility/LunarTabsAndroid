package com.PP.LunarTabsAndroid.Dialogs;

import java.io.File;
import java.io.FileInputStream;

import org.herac.tuxguitar.song.models.TGSong;

import android.app.Dialog;
import android.content.Context;
import android.preference.PreferenceManager;

import com.PP.APIs.TextToSpeechAPI;
import com.PP.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.daidalos.afiledialog.FileChooserDialog;

public class GuitarFileLoaderDialog extends FileChooserDialog {

	public GuitarFileLoaderDialog(final Context context, final MainActivity mainActivity) {
		super(context,PreferenceManager.getDefaultSharedPreferences(context).getString("set_tab_home_dir", null));
		setCanCreateFiles(false);
		setFilter(".*gp1|.*gp2|.*gp3|.*gp4|.*gp5|.*gpx|.*ptb");
	    addListener(new FileChooserDialog.OnFileSelectedListener() {
	         @Override
			public void onFileSelected(Dialog source, File file) {
	        	 
	        	 //finish dialog
	             source.dismiss();
	             	             
	             //attempt file load and populate tracks
	             try {
	            	 //load song and call update function.
	            	 TGSong song = TuxGuitarUtil.loadSong(new FileInputStream(file.getPath()));
	            	 mainActivity.updateOnFileLoad(song, file);
	             }
	             catch(Exception e) {
	            	 //say could not be loaded
	            	 TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_FILE_NOT_LOADED);
	             }	             
	         }
	         @Override
			public void onFileSelected(Dialog source, File folder, String name) {}
	     });		    
	}
}
