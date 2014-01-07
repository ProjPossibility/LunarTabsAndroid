package com.PP.IntelliSeg.Abstract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGBeat;


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
	 * List of instructions in segment.
	 */
	protected List<Instruction> instructions;
	
	//caches that are populated once
	protected List<String> sfInst = null;
	protected List<String> chordInst = null;
	protected List<TGBeat> beats = null;
	protected List<String> matchTargets = null;
		
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
	 * @return the instructions
	 */
	public List<Instruction> getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(List<Instruction> instructions) {
		this.instructions = instructions;
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
	
	//utility methods
	public List<String> getSfInst() {
		if(sfInst==null) {
			sfInst = new ArrayList<String>();
			for(Instruction i : instructions) {
				sfInst.add(i.getSfInst());
			}
		}
		return sfInst;
	}
	public List<String> getChordInst() {
		if(chordInst==null) {
			chordInst = new ArrayList<String>();
			for(Instruction i : instructions) {
				chordInst.add(i.getChordInst());
			}
		}
		return chordInst;
	}
	public List<TGBeat> getBeats() {
		if(beats==null) {
			beats = new ArrayList<TGBeat>();
			for(Instruction i : instructions) {
				beats.add(i.getBeat());
			}
		}
		return beats;
	}
	public List<String> getMatchTargets() {
		if(matchTargets==null) {
			matchTargets = new ArrayList<String>();
			for(Instruction i : instructions) {
				matchTargets.add(i.getMatchTarget());
			}
		}
		return matchTargets;
	}
}