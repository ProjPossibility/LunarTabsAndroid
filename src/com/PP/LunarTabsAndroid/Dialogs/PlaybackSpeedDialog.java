package com.PP.LunarTabsAndroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.example.lunartabsandroid.R;

public class PlaybackSpeedDialog extends DialogFragment {
	
	//parent
	protected MainActivity parent;
	
	public PlaybackSpeedDialog(MainActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.PlaybackSpeed);
	    builder.setItems(R.array.PlaybackSpeedChoices, new DialogInterface.OnClickListener() {
	    	@Override
			public void onClick(DialogInterface dialog, int which) {
	    		// The 'which' argument contains the index position
	    		// of the selected item
	    		
	    		//get choice
	    		Resources r = getActivity().getResources();
	    		String[] incChoices = r.getStringArray(R.array.PlaybackSpeedArr);
	    		try {
	    			
	    			//set data model playback speed choice
	    			double tempoScale = Double.parseDouble(incChoices[which].trim());
	    			DataModel dataModel = DataModel.getInstance();
	    			dataModel.setTempoScale(tempoScale);
	    		}
	    		catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    });
	    return builder.create();
	}
}