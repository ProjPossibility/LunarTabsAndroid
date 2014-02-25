package com.PP.LunarTabsAndroid.Dialogs;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;

public class SelectSectionDialog extends DialogFragment {
	
	//parent
	protected MainActivity parent;
	
	public SelectSectionDialog(MainActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//dynamically create section arrList<E>	
		DataModel dataModel = DataModel.getInstance();
		List<Segment> segments = dataModel.getInstSegments();
		CharSequence[] items = new CharSequence[segments.size()];
		for(int x=0; x < segments.size(); x++) {
			Segment segment = segments.get(x);
<<<<<<< HEAD
			String MEASURE = ResourceModel.getInstance().MEASURE;
			String TO_MEASURE = ResourceModel.getInstance().TO_MEASURE;
			items[x] = MEASURE + " " + (segment.getStart()+1) + " " + TO_MEASURE + " "+ (segment.getEnd()+1);
=======
			items[x] = "Measure " + segment.getStart() + " to Measure " + segment.getEnd();
>>>>>>> parent of 6146a03... Supports Repeat Instructions, Assorted Bug Fixes
		}
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.GoTo);
	    builder.setItems(items, new DialogInterface.OnClickListener() {
	    	@Override
			public void onClick(DialogInterface dialog, int which) {
	    		// The 'which' argument contains the index position
	    		// of the selected item
	    		
	    		//redo data model
	    		try {
	    			DataModel dataModel = DataModel.getInstance();
	    			dataModel.setCurrentSegment(which);
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