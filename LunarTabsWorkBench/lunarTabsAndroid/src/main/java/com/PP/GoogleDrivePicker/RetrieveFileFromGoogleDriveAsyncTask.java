package com.PP.GoogleDrivePicker;

import java.io.File;

import org.herac.tuxguitar.song.models.TGSong;

import android.os.AsyncTask;

import com.PP.APIs.TextToSpeechAPI;
import com.PP.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

public class RetrieveFileFromGoogleDriveAsyncTask extends AsyncTask<DriveId, Boolean, Boolean> {
	
	//google client
	protected GoogleApiClient mGoogleApiClient;
	protected MainActivity parent;
	protected GoogleDriveFilePickerActivity gdriveActivity;
	
	public RetrieveFileFromGoogleDriveAsyncTask(GoogleApiClient mGoogleApiClient, GoogleDriveFilePickerActivity gdriveActivity, MainActivity parent) {
		this.mGoogleApiClient = mGoogleApiClient;
		this.gdriveActivity = gdriveActivity;
		this.parent = parent;
	}
	
    @Override
    protected Boolean doInBackground(DriveId... params) {
    	try {
    		
    		//get TGSong from file on google drive
	        DriveFile file = params[0].asDriveFile();
	        DriveContentsResult driveContentsResult =
	                file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).await();
	        if (!driveContentsResult.getStatus().isSuccess()) {
           	 	TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_FILE_NOT_LOADED);	        	
	        	return false;
	        }
        	final TGSong song = TuxGuitarUtil.loadSong(driveContentsResult.getDriveContents().getInputStream());

        	//finish google drive activity
        	gdriveActivity.finish();

        	//call update function
        	parent.runOnUiThread(new Runnable() {
        		@Override
				public void run() {
                	parent.updateOnFileLoad(song, new File(""));        			
        		}
        	});
        	        	
	       	//return
        	return true;
        }
        catch(Exception e) {
        	TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_FILE_NOT_LOADED);
        	e.printStackTrace();
        	return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {}
}