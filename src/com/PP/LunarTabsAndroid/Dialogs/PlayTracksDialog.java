package com.PP.LunarTabsAndroid.Dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.PP.LunarTabsAndroid.UI.DataModel;
import com.example.lunartabsandroid.R;

public class PlayTracksDialog extends DialogFragment {
		
	public PlayTracksDialog() {}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//dynamically make items and checked array
		DataModel dataModel = DataModel.getInstance();
		List<String> tracks = dataModel.getTracksList();
		CharSequence[] items = new CharSequence[tracks.size()];
		for(int x=0; x < tracks.size(); x++) {
			items[x] = tracks.get(x);
		}
		boolean[] checked = new boolean[tracks.size()];		
		final List<Integer> mSelectedItems = new ArrayList<Integer>();
		List<Integer> prechecked = dataModel.getPlaybackTrackInds();
		if(prechecked==null) {
			checked[dataModel.getTrackNum()] = true;
			mSelectedItems.add(dataModel.getTrackNum());
		}
		else {
			for(int x=0; x < prechecked.size(); x++) {
				checked[prechecked.get(x)] = true;
				mSelectedItems.add(prechecked.get(x));
			}
		}		
				
		//build dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.sel_pbtracks_dialog_title)
	           .setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedItems.add(which);
	                   } else if (mSelectedItems.contains(which)) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	           .setPositiveButton(R.string.OK_button_text, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	   //update data model with new selection
	            	   DataModel.getInstance().setPlaybackTrackInds(mSelectedItems);
	            	   
	               }
	           })
	           .setNegativeButton(R.string.Cancel_button_text, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {}
	           });

	    return builder.create();
	}
}