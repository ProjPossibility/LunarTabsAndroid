package com.PP.LunarTabsAndroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.PP.IntelliSeg.MarkerSegmenter.MarkerSegmenter;
import com.PP.IntelliSeg.MeasureIncrementSegmenter.MeasureIncrementSegmenter;
import com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter.SMRSegmenter;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.example.lunartabsandroid.R;

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
	    		String[] incChoices = r.getStringArray(R.array.MeasureIncArr);
	    		if(which==(incChoices.length-2)) {
	    			
	    			//redo data model w/ repetition segmenter
	    			DataModel dataModel = DataModel.getInstance();	    			
	    	        dataModel.setSegmenter(new SMRSegmenter());	 
	    			dataModel.genInstructions();
	    			dataModel.setCurrentSegment(0);
	    			dataModel.clearSelectedInstructionIndex();	    			
	            	parent.refreshGUI();	    				    	        
	    			
	    		}
	    		else if(which==(incChoices.length-1)) {
	    			
	    			//redo data model w/ marker segmenter
	    			DataModel dataModel = DataModel.getInstance();	    			
	    	        dataModel.setSegmenter(new MarkerSegmenter());	 
	    			dataModel.genInstructions();
	    			dataModel.setCurrentSegment(0);
	    			dataModel.clearSelectedInstructionIndex();	    			
	            	parent.refreshGUI();	    				    	        	    			
	    		}
	    		else {
	    			
		    		//redo data model w/ measurement increment
		    		try {
		    			int newInc = Integer.parseInt(incChoices[which]);
		    			DataModel dataModel = DataModel.getInstance();
		    			MeasureIncrementSegmenter m;
		    			if(dataModel.getSegmenter() instanceof MeasureIncrementSegmenter) {
		    				m = (MeasureIncrementSegmenter) dataModel.getSegmenter();
		    			}
		    			else {
		    				m = new MeasureIncrementSegmenter();
		    				dataModel.setSegmenter(m);
		    			}
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
	    	}
	    });
	    return builder.create();
	}
}