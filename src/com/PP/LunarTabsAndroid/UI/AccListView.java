package com.PP.LunarTabsAndroid.UI;

import com.PP.LunarTabsAndroid.APIs.WordActivatorAPI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class AccListView extends ListView {
	
	//params
	protected int hilightColor;
	protected int bgColor;
	protected boolean hilightEnabled;

	public AccListView(Context context, AttributeSet aSet) { 
		super(context, aSet);		
	}
	
	public AccListView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
	}
	
	/**
	 * Init code
	 */
	public void init(int highlightColor, final int bgColor) {
		
		//single choice mode, set focusable, and set highlight and background color
		this.hilightColor = highlightColor;
		this.bgColor = bgColor;
        setBackgroundColor(bgColor);		
		setChoiceMode(1);
		setFocusable(true);	
        hilightEnabled = false;
        
		//clicking item polices
		this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
            	
            	//insert activator delay into stt for finishing explore by touch speaking.
            	if(DataModel.getInstance().isVoiceActionsEnabled()) {
            		WordActivatorAPI.getInstance().disableFor(SpeechConst.ACTIVATOR_DELAY);
            	}
            	
            	//debug
            	Log.d("CLICKED", "SEL: " + position + 
            			" NumCHILD: " + parent.getChildCount()
            			+ " First Vis: " + parent.getFirstVisiblePosition()
            			+ " Count: " + parent.getCount()
            			+ " Hilight: " + hilightEnabled);
            	
            	//if enabled, do update.
            	if(hilightEnabled) {
            		
	            	//set selection in gui model
	            	DataModel.getInstance().setSelectedInstructionIndex(position);
	            	            	
	            	//blank out all child views and set accessible corrections
	            	for(int x=0; x < parent.getChildCount(); x++) {
	            		
	            		//color
	            		View child = parent.getChildAt(x);
	            		child.setBackgroundColor(bgColor);
	            		
	    	    		//make accessible text corrections
	    	    		if(child instanceof TextView) {
	    	    			TextView tx = (TextView) child;
	    	    			tx.setContentDescription(
	    	    					InstructionContentDescription.makeAccessibleInstruction(
	    	    							tx.getText().toString()));
	    	    		}
	            	}
	            	            	
	            	//highlight selected
	            	View selected = parent.getChildAt(position-parent.getFirstVisiblePosition());
	            	if(selected!=null) {
	            		selected.setBackgroundColor(hilightColor);
	            	}  
	           }
            }
        });
		
		//scroll policy
        this.setOnScrollListener(new OnScrollListener(){
            @Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            	
            	//if enabled, do update.
            	if(hilightEnabled) {
            		
	            	//on start of scroll, clear all highlighting and accessible text correction
	            	for(int x=0; x < view.getChildCount(); x++) {
	            		
	            		//clear hilighting
	            		View child = view.getChildAt(x);
	            		child.setBackgroundColor(bgColor);
	            		
	    	    		//make accessible text corrections
	    	    		if(child instanceof TextView) {
	    	    			TextView tx = (TextView) child;
	    	    			tx.setContentDescription(
	    	    					InstructionContentDescription.makeAccessibleInstruction(
	    	    							tx.getText().toString()));
	    	    		}
	            		
	            	}            	            	
	            	
	            	//figure out whether we need to re-highlight the selected thing.
	          	  	int firstVis = view.getFirstVisiblePosition();
	          	  	int childCount = view.getChildCount();
	          	  	int selectedInstructionIndex = DataModel.getInstance().getSelectedInstructionIndex();
	          	  	int newIndex = selectedInstructionIndex - firstVis;
	          	  	if(newIndex >= 0 && newIndex < childCount) {
	          	  		View child = view.getChildAt(newIndex);
	          	  		child.setBackgroundColor(hilightColor);
	          	  	}            	
	          	  
	            }
            }
            @Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
            
          });
	}
	
		
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		event.getText().clear();
	}
		
	/**
	 * Performs manual refreshes list selection.
	 */
	public void refreshGUI() {
		Log.d("REFRESH CALLED", "RFRESH " + hilightEnabled);
		
		//if enabled, do update.
		if(hilightEnabled) {
			
			//clear all list view selections and set accessible text
	    	for(int x=0; x < this.getChildCount(); x++) {
	    		
	    		//color text
	    		View child = this.getChildAt(x);
	    		child.setBackgroundColor(bgColor);
	    		
	    		//make accessible text corrections
	    		if(child instanceof TextView) {
	    			TextView tx = (TextView) child;
	    			tx.setContentDescription(
	    					InstructionContentDescription.makeAccessibleInstruction(
	    							tx.getText().toString()));
	    		}
	    	}
	    	
	    	//figure out whether we need to re-highlight the selected thing.
	    	//highlight if necessary.
	  	  	int firstVis = this.getFirstVisiblePosition();
	  	  	int childCount = this.getChildCount();
	  	  	int selectedInstructionIndex = DataModel.getInstance().getSelectedInstructionIndex();
	  	  	int newIndex = selectedInstructionIndex - firstVis;
  	  		Log.d("LOG","REHI " + newIndex);	  	  	
	  	  	if(newIndex >= 0 && newIndex < childCount) {
	  	  		View child = this.getChildAt(newIndex);
	  	  		Log.d("LOG","REHI: " + newIndex);
	  	  		child.setBackgroundColor(hilightColor);
	  	  	}            	
		}
	}
	
	/**
	 * Performs a programmatic selection of this index. Includes GUI updating.
	 * @param selectedInstructionIndex
	 */
	public void programmaticSelect(int selectedInstructionIndex) {
		
		//set parameter
		DataModel.getInstance().setSelectedInstructionIndex(selectedInstructionIndex);
		
		//if enabled, do update.
		if(hilightEnabled) {
			
			//update
			if(selectedInstructionIndex==-1) {
				refreshGUI(); //just clear hilight
			}
			else if(selectedInstructionIndex >= 0 ){
				
				//perform click on selected index.
				this.setItemChecked(selectedInstructionIndex, true);
				this.setSelection(selectedInstructionIndex);
				this.performItemClick(this.findViewWithTag(this.getAdapter().getItem(selectedInstructionIndex)), 
					selectedInstructionIndex, this.getAdapter().getItemId(selectedInstructionIndex));
			}
		}
	}

	/**
	 * @return the hilightColor
	 */
	public int getHilightColor() {
		return hilightColor;
	}

	/**
	 * @param hilightColor the hilightColor to set
	 */
	public void setHilightColor(int hilightColor) {
		this.hilightColor = hilightColor;
	}

	/**
	 * @return the enabled
	 */
	public boolean isHilightEnabled() {
		return hilightEnabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setHilightEnabled(boolean h_enabled) {
		this.hilightEnabled = h_enabled;
	}
}
