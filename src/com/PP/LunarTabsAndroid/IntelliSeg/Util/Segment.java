package com.PP.LunarTabsAndroid.IntelliSeg.Util;

public class Segment {
	
	//fields
	protected int start;
	protected int end;
	
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
}
