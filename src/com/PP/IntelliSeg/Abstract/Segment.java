package com.PP.IntelliSeg.Abstract;

import java.util.List;

public class Segment {
	
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
}
