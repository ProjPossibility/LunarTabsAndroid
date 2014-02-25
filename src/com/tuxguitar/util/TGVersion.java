package com.tuxguitar.util;

import java.io.Serializable;

public class TGVersion implements Serializable {
	
	public static final TGVersion CURRENT = new TGVersion(1,1,0);
	
	private int major;
	private int minor;
	private int revision;
	
	public TGVersion(int major,int minor, int revision){
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}
	
	public int getMajor() {
		return this.major;
	}
	
	public int getMinor() {
		return this.minor;
	}
	
	public int getRevision() {
		return this.revision;
	}
	
	public boolean isSameVersion(TGVersion version){
		if( version == null ){
			return false;
		}
		return ( version.getMajor() == getMajor() && version.getMinor() == getMinor() && version.getRevision() == getRevision());
	}
	
	public String getVersion(){
		String version = (getMajor() + "." + getMinor());
		if( getRevision() > 0 ){
			version += ("." + getRevision());
		}
		return version;
	}
	
	@Override
	public String toString(){
		return getVersion();
	}
}
