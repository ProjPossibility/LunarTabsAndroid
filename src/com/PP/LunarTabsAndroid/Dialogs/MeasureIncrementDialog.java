package com.PP.LunarTabsAndroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.PP.IntelliSeg.MarkerSegmenter.MarkerSegmenter;
import com.PP.IntelliSeg.MeasureIncrementSegmenter.MeasureIncrementSegmenter;
import com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter.SMRSegmenter;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.example.lunartabsandroid.R;

public class MeasureIncrementDialog extends DialogFragment {
	
	//parent
	protected Activity parent;
	
	public MeasureIncrementDialog(Activity parent) {
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
	    		
	    		//store preference choice
	          	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(parent.getApplicationContext());
	         	Editor e1 = sharedPrefs.edit();
	         	e1.putInt("set_section_increment_pref", which);
	         	e1.commit();
	    		
	    		//get choice
	    		Resources r = getActivity().getResources();
	    		String[] incChoices = r.getStringArray(R.array.MeasureIncArr);
	    		if(which==(incChoices.length-3)) {
	    			
	    			//redo data model w/ max value measurement increment segmenter
	    			DataModel dataModel = DataModel.getInstance();
	    			MeasureIncrementSegmenter m;
	    			if(dataModel.getSegmenter()!=null && dataModel.getSegmenter() instanceof MeasureIncrementSegmenter) {
	    				m = (MeasureIncrementSegmenter) dataModel.getSegmenter();
	    			}
	    			else {
	    				m = new MeasureIncrementSegmenter();
	    				dataModel.setSegmenter(m);
	    			}
	    			m.setIncrement(Integer.MAX_VALUE);
	    			
	    		}
	    		else if(which==(incChoices.length-2)) {
	    			
	    			//redo data model w/ repetition segmenter
	    			DataModel dataModel = DataModel.getInstance();	    			
	    	        dataModel.setSegmenter(new SMRSegmenter());	 
	    	        	    			
	    		}
	    		else if(which==(incChoices.length-1)) {
	    			
	    			//redo data model w/ marker segmenter
	    			DataModel dataModel = DataModel.getInstance();	    			
	    	        dataModel.setSegmenter(new MarkerSegmenter());	
	    	        	    	    	
	    		}
	    		else {
	    			
	    			//redo data model w/ measurement increment segmenter
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
		    					    	    	
	    		}
	    		
    	        //refresh currently loaded tab (if currently loaded)
    			DataModel dataModel = DataModel.getInstance();	    		
    	    	if(dataModel.getSong()!=null && 
    	    			dataModel.getInstSegments()!=null && 
    	    			dataModel.getTrackNum()!=-1 && dataModel.getCurrentSegment()!=-1 && 
    	    			dataModel.getInstSegments().get(dataModel.getCurrentSegment())!=null) {
    	    		
    	    		//refresh currently loaded tab
	    			dataModel.genInstructions();
	    			dataModel.setCurrentSegment(0);
	    			dataModel.clearSelectedInstructionIndex();	    			
    	    	}
	    	}
	    });
	    return builder.create();
	}
}