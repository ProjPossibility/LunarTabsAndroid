package com.PP.IntelliSeg.Abstract;

import org.herac.tuxguitar.song.models.TGBeat;

public class Instruction {
	
	//flags
	public static final int PLAY_INSTRUCTION = 1;
	public static final int REST_INSTRUCTION = 2;
	public static final int REPEAT_INSTRUCTION = 3;
	
	//fields
	protected int type;
	protected String sfInst;
	protected String chordInst;
	protected String matchTarget;
	protected TGBeat beat;
	
	/**
	 * Constructor
	 * @param type
	 */
	public Instruction(int type) {
		this.type = type;
		sfInst = null;
		chordInst = null;
		matchTarget = "";
		beat = null;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the sfInst
	 */
	public String getSfInst() {
		return sfInst;
	}

	/**
	 * @param sfInst the sfInst to set
	 */
	public void setSfInst(String sfInst) {
		this.sfInst = sfInst;
	}

	/**
	 * @return the chordInst
	 */
	public String getChordInst() {
		return chordInst;
	}

	/**
	 * @param chordInst the chordInst to set
	 */
	public void setChordInst(String chordInst) {
		this.chordInst = chordInst;
	}

	/**
	 * @return the matchTarget
	 */
	public String getMatchTarget() {
		return matchTarget;
	}

	/**
	 * @param matchTarget the matchTarget to set
	 */
	public void setMatchTarget(String matchTarget) {
		this.matchTarget = matchTarget;
	}

	/**
	 * @return the beat
	 */
	public TGBeat getBeat() {
		return beat;
	}

	/**
	 * @param beat the beat to set
	 */
	public void setBeat(TGBeat beat) {
		this.beat = beat;
	}	
}
