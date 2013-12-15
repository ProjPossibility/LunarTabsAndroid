package com.PP.LunarTabsAndroid.Dialogs;

import java.util.List;

import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;
import com.example.lunartabsandroid.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class SelectSectionDialog extends DialogFragment {
	
	//parent
	protected MainActivity parent;
	
	public SelectSectionDialog(MainActivity parent) {
		this.parent = parent;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//dynamically create section arrList<E>	
		GUIDataModel dataModel = GUIDataModel.getInstance();
		List<Segment> segments = dataModel.getInstSegments();
		CharSequence[] items = new CharSequence[segments.size()];
		for(int x=0; x < segments.size(); x++) {
			Segment segment = segments.get(x);
			items[x] = "Measure " + segment.getStart() + " to Measure " + segment.getEnd();
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
	    			GUIDataModel dataModel = GUIDataModel.getInstance();
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