package com.PP.LunarTabsAndroid.UI;
import java.io.Serializable;
import java.util.List;

import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;

import com.PP.APIs.TuxGuitarUtil;
import com.PP.IntelliSeg.Abstract.AbstractSegmenter;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.Activities.MainActivity;

public class DataModel implements Serializable {
	
	//Presentation model params
	protected String fileName;
	protected TGSong song;
	protected int trackNum;
	protected List<String> tracksList;		
	protected List<Segment> instSegments;
	protected int currentSegment;
	protected int selectedInstructionIndex;	
	protected boolean verbose;
	protected AbstractSegmenter segmenter;
	protected MainActivity mainActivity;
	protected List<Integer> playbackTrackInds;
	
	//singleton
	protected DataModel() {
		song = null;
		trackNum = -1;
		currentSegment = -1;
		fileName = null;
		tracksList = null;
		verbose = false;
		selectedInstructionIndex = -1;
		mainActivity = null;
		playbackTrackInds = null;
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
	 * Serialize instance to file
	 */
	public void saveInstance() {
//		FileOpAPI.writeModel(instance, FileOpAPI.GUI_MODEL_FILE);
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
	 * @return the mainActivity
	 */
	public MainActivity getMainActivity() {
		return mainActivity;
	}

	/**
	 * @param mainActivity the mainActivity to set
	 */
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	/**
	 * @return the playbackTrackInds
	 */
	public List<Integer> getPlaybackTrackInds() {
		return playbackTrackInds;
	}

	/**
	 * @param playbackTrackInds the playbackTrackInds to set
	 */
	public void setPlaybackTrackInds(List<Integer> playbackTrackInds) {
		this.playbackTrackInds = playbackTrackInds;
	}
}