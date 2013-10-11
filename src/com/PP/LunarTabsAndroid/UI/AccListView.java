package com.PP.LunarTabsAndroid.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;

public class AccListView extends ListView {

	public AccListView(Context context, AttributeSet aSet) { 
		super(context, aSet); 
	}
	
	public AccListView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
	}
		
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		event.getText().clear();
	}

}
