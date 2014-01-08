package com.PP.IntelliSeg.Util;

public class HashBuffer {
	
	//flags
	public static final String DEFAULT_DELIM = ":";
	
	//fields
	protected StringBuffer buffer;
	protected String delim;
	
	//ctr
	public HashBuffer(String delim) {
		this.delim = delim;
		buffer = new StringBuffer();
	}
	
	public void append(String s) {
		buffer.append(s);
		buffer.append(delim);
	}
	
	public void append(boolean b) {
		buffer.append(b);
		buffer.append(delim);
	}
	
	public void append(int i) {
		buffer.append(i);
		buffer.append(delim);
	}
	
	public String toString() {
		return buffer.toString();
	}

}
