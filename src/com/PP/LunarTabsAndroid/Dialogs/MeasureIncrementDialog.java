package com.PP.LunarTabsAndroid.Dialogs;

import com.PP.IntelliSeg.MeasureIncrementSegmenter.MeasureIncrementSegmenter;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;
import com.example.lunartabsandroid.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

public class MeasureIncrementDialog extends DialogFragment {
	
	//parent
	protected MainActivity parent;
	
	public MeasureIncrementDialog(MainActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.SectionIncrement);
	    builder.setItems(R.array.MeasureIncChoices, new DialogInterface.OnClickListener() {
	    	@Override
			public void onClick(DialogInterface dialog, int which) {
	    		// The 'which' argument contains the index position
	    		// of the selected item
	    		
	    		//get choice
	    		Resources r = getActivity().getResources();
	    		int[] incChoices = r.getIntArray(R.array.MeasureIncArr);
	    		int newInc = incChoices[which];
	    		
	    		
	    		//redo data model
	    		try {
	    			GUIDataModel dataModel = GUIDataModel.getInstance();
	    			MeasureIncrementSegmenter m = (MeasureIncrementSegmenter) dataModel.getSegmenter();
	    			m.setIncrement(newInc);
	    			dataModel.genInstructions();
	    			dataModel.setCurrentSegment(0);
	    			dataModel.clearSelectedInstructionIndex();	    			
	            	parent.refreshGUI();	    			
	    		}
	    		catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    });
	    return builder.create();
	}
}