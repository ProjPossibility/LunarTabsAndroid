package com.PP.MidiServer;

import org.jfugue.Note;


public class NoteEvent {
	
	//fields
	protected long timestamp;
	protected Note note;
	
	/**
	 * Constructor
	 * @param timestamp
	 * @param note
	 */
	public NoteEvent(long timestamp, Note note) {
		this.timestamp = timestamp;
		this.note = note;
	}
	
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the note
	 */
	public Note getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(Note note) {
		this.note = note;
	}
}
