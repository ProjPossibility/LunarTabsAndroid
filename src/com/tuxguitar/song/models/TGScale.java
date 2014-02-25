package com.tuxguitar.song.models;

import java.io.Serializable;

public abstract class TGScale implements Serializable {
	private final boolean[] notes = new boolean[12];
	
	private int key;
	
	public TGScale(){
		this.clear();
	}
	
	public void setKey(int key){
		this.key = key;
	}
	
	public int getKey(){
		return this.key;
	}
	
	public void setNote(int note,boolean on){
		this.notes[note] = on;
	}
	
	public boolean getNote(int note){
		return this.notes[((note + (12 - this.key)) % 12)];
	}
	
	public void clear(){
		this.setKey(0);
		for(int i = 0; i < this.notes.length; i++){
			this.setNote(i,false);
		}
	}
	
}
