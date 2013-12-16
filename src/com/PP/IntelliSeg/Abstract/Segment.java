package com.PP.IntelliSeg.Abstract;

import java.io.Serializable;
import java.util.List;

import com.tuxguitar.song.models.TGBeat;

public abstract class Segment implements Serializable {
	
	/**
	 * Start of segment (for indexing)
	 */
	protected int start;
	
	/**
	 * End of segment (for indexing)
	 */
	protected int end;
	
	/**
	 * List of (string,fret) instructions corresponding to segment.
	 */
	protected List<String> sfInst;
	
	/**
	 * List of chord instructions corresponding to segment.
	 */
	protected List<String> chordInst;
	
	/**
	 * List of string match targets for chords.
	 */
	 protected List<String> matchTargets;
	 
	/**
	 * List of Beats
	 */
	protected List<TGBeat> beats;
	
	/**
	 * Constructor
	 * @param start Start of Segment
	 * @param end End of Segment
	 */
	public Segment(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the sfInst
	 */
	public List<String> getSfInst() {
		return sfInst;
	}

	/**
	 * @param sfInst the sfInst to set
	 */
	public void setSfInst(List<String> sfInst) {
		this.sfInst = sfInst;
	}

	/**
	 * @return the chordInst
	 */
	public List<String> getChordInst() {
		return chordInst;
	}

	/**
	 * @param chordInst the chordInst to set
	 */
	public void setChordInst(List<String> chordInst) {
		this.chordInst = chordInst;
	}	
	
	/**
	 * @return the beats
	 */
	public List<TGBeat> getBeats() {
		return beats;
	}

	/**
	 * @param beats the beats to set
	 */
	public void setBeats(List<TGBeat> beats) {
		this.beats = beats;
	}

	/**
	 * Abstract method for playing segment in MediaPlayer.
	 */
	public abstract void play();
	
	/**
	 * Abstract method for presenting the title of the segment
	 * @return
	 */
	public abstract String getTitlePresentation();

	/**
	 * @return the matchTargets
	 */
	public List<String> getMatchTargets() {
		return matchTargets;
	}

	/**
	 * @param matchTargets the matchTargets to set
	 */
	public void setMatchTargets(List<String> matchTargets) {
		this.matchTargets = matchTargets;
	}	
}