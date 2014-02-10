package com.PP.IntelliSeg.RepetionSegmenter.CrochemoreSegmenter.base;

import android.preference.PreferenceManager;

import com.PP.APIs.FileOpAPI;
import com.PP.APIs.PlaybackEngineAPI;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.UI.DataModel;

public class CrochemoreSegment extends Segment {

	/**
	 * Instantiate
	 * @param start
	 * @param end
	 */
	public CrochemoreSegment(int start, int end) {
		super(start, end);
	}
	
	@Override
	public void play() {
		DataModel dataModel = DataModel.getInstance();
        String tempo_str = PreferenceManager.getDefaultSharedPreferences(dataModel.getMainActivity().getApplicationContext())
        		.getString("playback_speed_pref", "1.00");
        double tempoScale = Double.parseDouble(tempo_str);
//		TuxGuitarUtil.playClip_beats(dataModel.getFilePath(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum());
		PlaybackEngineAPI.playClip(dataModel.getSong(), FileOpAPI.SAVE_PATH, getStart(),getEnd(),dataModel.getTrackNum(), tempoScale);				
	}
	
	@Override
	public String getTitlePresentation() {
		return "(M " + (getStart()+1) + "-" + "M" + (getEnd()+1) + ")";
	}

}
