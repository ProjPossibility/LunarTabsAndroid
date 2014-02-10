package com.PP.LunarTabsAndroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.PP.GoogleDrivePicker.GoogleDriveFilePickerActivity;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.example.lunartabsandroid.R;

public class FileSelectionMethodDialog extends DialogFragment {
	
	//parent
	protected MainActivity parent;
	
	public FileSelectionMethodDialog(MainActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.file_selection_dialog_title);
	    builder.setItems(R.array.file_selection_methods, new DialogInterface.OnClickListener() {
	    	@Override
			public void onClick(DialogInterface dialog, int which) {
	    		// The 'which' argument contains the index position
	    		// of the selected item
	    		
	    		//dismiss
	    		dialog.dismiss();
	    		
	    		//cases
	    		switch(which){
	    			case 0:
	    				
	    				//google drive
	    				Intent i = new Intent(parent, GoogleDriveFilePickerActivity.class);
	    				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    				startActivity(i);												
	    				
	    				break;
	    			case 1:
	    				
	    				//hard disk
	    				GuitarFileLoaderDialog dialog2 = new GuitarFileLoaderDialog(parent,parent);
	    				dialog2.show();	    				
	    				
	    				break;
	    		}
	    		
	    	}
	    });
	    return builder.create();
	}
}