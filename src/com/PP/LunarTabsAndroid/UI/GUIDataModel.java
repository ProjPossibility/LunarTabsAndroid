package com.PP.LunarTabsAndroid.UI;
import java.util.*;

import com.PP.LunarTabsAndroid.InstructionGenerator.DrumInstructionGenerator;
import com.PP.LunarTabsAndroid.InstructionGenerator.GuitarInstructionGenerator;
import com.tuxguitar.TuxGuitarUtil;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGMeasure;
import com.tuxguitar.song.models.TGSong;
import com.tuxguitar.song.models.TGTrack;

public class GUIDataModel {
	
	//Presentation model params
	protected String filePath;
	protected TGSong song;
	protected int trackNum;
	protected List<List<String>> measureInst;
	protected List<List<String>> sfInst;
	protected int currentMeas;
	protected boolean verbose;
	
	/**
	 * Constructor
	 */
	public GUIDataModel() {
		song = null;
		trackNum = -1;
		currentMeas = -1;
		verbose = false;
	}
		
	/**
	 * Is on percussion track
	 */
	public boolean isOnPercussionTrack() {
		TGTrack track  = TuxGuitarUtil.getTrack(song,trackNum);
		return track.isPercussionTrack();
	}
	
	/**
	 * Dynamically generates instructions
	 */
	public void genInstructions() {
				
		//see if can return early
		if(song==null || trackNum==-1 || song.countTracks()==0) {
			return;
		}
		
		//rtn
		measureInst = new ArrayList<List<String>>();
		sfInst = new ArrayList<List<String>>();
		
		//load beats
		TGTrack track  = TuxGuitarUtil.getTrack(song,trackNum);
		List<TGMeasure> measures = TuxGuitarUtil.getMeasures(track);
		for(TGMeasure measure : measures) {
			List<String> measInst = new ArrayList<String>();
			List<String> sfI = new ArrayList<String>();
			List beats = measure.getBeats();
			for(int x=0; x < beats.size(); x++) {
				String i1="";
				String i2="";
				TGBeat b = (TGBeat)beats.get(x);
				if(track.isPercussionTrack()) {
					i1 = DrumInstructionGenerator.getInstance().getPlayInstruction(b);
				}
				else {
					i1 = GuitarInstructionGenerator.getInstance().getPlayInstruction(b);
					i2 = GuitarInstructionGenerator.getInstance().getStringFretInstruction(b);
				}
				measInst.add(i1);
				sfI.add(i2);
			}
			measureInst.add(measInst);
			sfInst.add(sfI);
		}
	}
		
	/**
	 * @return the song
	 */
	public TGSong getSong() {
		return song;
	}
	/**
	 * @param song the song to set
	 */
	public void setSong(TGSong song) {
		this.song = song;
	}

	/**
	 * @return the trackNum
	 */
	public int getTrackNum() {
		return trackNum;
	}

	/**
	 * @param trackNum the trackNum to set
	 */
	public void setTrackNum(int trackNum) {
		this.trackNum = trackNum;
	}

	/**
	 * @return the measureInst
	 */
	public List<List<String>> getMeasureInst() {
		return measureInst;
	}

	/**
	 * @param measureInst the measureInst to set
	 */
	public void setMeasureInst(List<List<String>> measureInst) {
		this.measureInst = measureInst;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the sfInst
	 */
	public List<List<String>> getSfInst() {
		return sfInst;
	}

	/**
	 * @param sfInst the sfInst to set
	 */
	public void setSfInst(List<List<String>> sfInst) {
		this.sfInst = sfInst;
	}

	/**
	 * @return the currentMeas
	 */
	public int getCurrentMeas() {
		return currentMeas;
	}

	/**
	 * @param currentMeas the currentMeas to set
	 */
	public void setCurrentMeas(int currentMeas) {
		this.currentMeas = currentMeas;
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}	
}