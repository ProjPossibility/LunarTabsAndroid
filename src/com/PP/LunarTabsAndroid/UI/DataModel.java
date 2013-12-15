package com.PP.LunarTabsAndroid.UI;
import java.io.Serializable;
import java.util.*;

import android.os.Bundle;

import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.Resumable.ResumableUtility;
import com.PP.StompDetector.StompDetector;
import com.tuxguitar.song.models.TGSong;
import com.tuxguitar.song.models.TGTrack;

public class DataModel implements Serializable {
	
	//Presentation model params
	protected String filePath;
	protected String fileName;
	protected TGSong song;
	protected int trackNum;
	protected List<String> tracksList;		
	protected List<Segment> instSegments;
	protected int currentSegment;
	protected int selectedInstructionIndex;	
	protected boolean verbose;
	protected AbstractSegmenter segmenter;
	protected volatile boolean voiceActionsEnabled;
	protected List<ResumableUtility> resumableUtilities;

	//singleton
	protected DataModel() {
		song = null;
		trackNum = -1;
		currentSegment = -1;
		filePath = null;
		fileName = null;
		tracksList = null;
		verbose = false;
		selectedInstructionIndex = -1;
		voiceActionsEnabled = false;
		resumableUtilities = new ArrayList<ResumableUtility>();
	}
	protected static DataModel instance = null;
	public static DataModel getInstance() {
		if(instance==null) {
//			instance = FileOpAPI.readModel(FileOpAPI.GUI_MODEL_FILE);
			if(instance==null) {
				instance = new DataModel();
			}
		}
		return instance;
	}
	
	/**
	 * Send instance to bundle.
	 */
	public void saveInstance(Bundle bundle) {
		bundle.putString("filePath", filePath);
		bundle.putString("fileName", fileName);
		bundle.putInt("trackNum", trackNum);
		bundle.putStringArrayList("tracksList", (ArrayList<String>)tracksList);
		bundle.putInt("currentSegment",currentSegment);
		bundle.putInt("selectedInstructionIndex",selectedInstructionIndex);
		bundle.putBoolean("verbose", verbose);
		bundle.putBoolean("voiceActionsEnabled", voiceActionsEnabled);
		for(ResumableUtility utility : resumableUtilities) {
			bundle.putBoolean(utility.toString(), utility.isOnStop_state());
		}		
//		FileOpAPI.writeModel(instance, FileOpAPI.GUI_MODEL_FILE);
	}
	
	public void loadInstance(Bundle bundle) {
		filePath = bundle.getString("filePath");
		fileName = bundle.getString("fileName");
		trackNum = bundle.getInt("trackNum");
		tracksList = bundle.getStringArrayList("tracksList");
		currentSegment = bundle.getInt("currentSegment");
		selectedInstructionIndex = bundle.getInt("selectedInstructionIndex");
		verbose = bundle.getBoolean("verbose");
		voiceActionsEnabled = bundle.getBoolean("voiceActionsEnabled");
		for(ResumableUtility utility : resumableUtilities) {
			utility.setOnStop_state(bundle.getBoolean(utility.toString()));
		}
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
				
		//get track
		TGTrack track  = TuxGuitarUtil.getTrack(song,trackNum);
		
		//generate segments based on segmenter
		instSegments = segmenter.segment(track);
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
	 * @return the currentSegment
	 */
	public int getCurrentSegment() {
		return currentSegment;
	}

	/**
	 * @param currentSegment the currentSegment to set
	 */
	public void setCurrentSegment(int currentSegment) {
		this.currentSegment = currentSegment;
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

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the tracksList
	 */
	public List<String> getTracksList() {
		return tracksList;
	}

	/**
	 * @param tracksList the tracksList to set
	 */
	public void setTracksList(List<String> tracksList) {
		this.tracksList = tracksList;
	}

	/**
	 * @return the instSegments
	 */
	public List<Segment> getInstSegments() {
		return instSegments;
	}

	/**
	 * @param instSegments the instSegments to set
	 */
	public void setInstSegments(List<Segment> instSegments) {
		this.instSegments = instSegments;
	}

	/**
	 * @return the segmenter
	 */
	public AbstractSegmenter getSegmenter() {
		return segmenter;
	}

	/**
	 * @param segmenter the segmenter to set
	 */
	public void setSegmenter(AbstractSegmenter segmenter) {
		this.segmenter = segmenter;
	}

	/**
	 * @return the selectedInstructionIndex
	 */
	public int getSelectedInstructionIndex() {
		return selectedInstructionIndex;
	}

	/**
	 * @param selectedInstructionIndex the selectedInstructionIndex to set
	 */
	public void setSelectedInstructionIndex(int selectedInstructionIndex) {
		this.selectedInstructionIndex = selectedInstructionIndex;
	}	
	
	public void clearSelectedInstructionIndex() {
		this.selectedInstructionIndex = -1;
	}

	/**
	 * @return the voiceActionsEnabled
	 */
	public boolean isVoiceActionsEnabled() {
		return voiceActionsEnabled;
	}

	/**
	 * @param voiceActionsEnabled the voiceActionsEnabled to set
	 */
	public void setVoiceActionsEnabled(boolean voiceActionsEnabled) {
		this.voiceActionsEnabled = voiceActionsEnabled;
	}
	
	public void addResumableUtility(ResumableUtility utility) {
		if(!resumableUtilities.contains(utility)) {
			resumableUtilities.add(utility);
		}
	}
	
	public void removeResuableUtility(ResumableUtility utility) {
		resumableUtilities.remove(utility);
	}

	/**
	 * @return the resumableUtilities
	 */
	public List<ResumableUtility> getResumableUtilities() {
		return resumableUtilities;
	}	
}