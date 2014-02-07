package com.PP.LunarTabsAndroid.Dialogs;

import java.io.File;

import org.herac.tuxguitar.song.models.TGSong;

import android.R;
import android.app.Dialog;
import android.content.Context;

import com.PP.LunarTabsAndroid.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.PP.LunarTabsAndroid.UI.SerializedParams;
import com.daidalos.afiledialog.FileChooserDialog;

public class GuitarFileLoaderDialog extends FileChooserDialog {

	public GuitarFileLoaderDialog(final Context context, final MainActivity mainActivity) {
		super(context,SerializedParams.getInstance().getHomeDir());
		setCanCreateFiles(false);
		setFilter(".*gp1|.*gp2|.*gp3|.*gp4|.*gp5|.*gpx|.*ptb");
	    addListener(new FileChooserDialog.OnFileSelectedListener() {
	         @Override
			public void onFileSelected(Dialog source, File file) {
	        	 
	        	 //finish dialog
	             source.dismiss();
	             	             
	             //attempt file load and populate tracks
	             try {
	            	 
		             //populate GUI with selection       
//		             fileField.setText(file.getName());
//	            	 fileField.setContentDescription(file.getName());
	            	 
	            	 //load song and store in gui data model
	            	 TGSong song = TuxGuitarUtil.loadSong(file.getPath());
//	            	 fileField.setText(song.getName());
//	            	 fileField.setContentDescription(song.getName());
	            	 DataModel dataModel = DataModel.getInstance();
	            	 dataModel.setFilePath(file.getPath());
		             dataModel.setFileName(song.getName());	            	 
	            	 if(song!=null) {
	            		 dataModel.setSong(song);
	            	 }
	            	 
	            	 //create tracks
	            	 mainActivity.createTrackOptions();
	            	 
	             	//set first segment selected and load instructions
	             	if(dataModel.getTracksList().size() >0) {
	             		
	             		//set params
	             		dataModel.setTrackNum(0);
	             		dataModel.setCurrentSegment(0);
	             		
		             	//enable instructions list
		             	mainActivity.getInstructionsList().setHilightEnabled(true);
	             		
		             	//perform load and show on GUI
	         			mainActivity.loadInstructions();				    	
	         			DataModel.getInstance().clearSelectedInstructionIndex();
	         			mainActivity.getInstructionsList().refreshGUI();	         			
	             	}	            	 
	             		            	 
	            	 //notify user that track successfully loaded
	            	 TextToSpeechAPI.speak(ResourceModel.getInstance().FILE_LOADED_SPEECH);
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
