package com.PP.MidiServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.jfugue.Note;

public class MidiServer {
	
	//const
	public static final long CHORD_BUFFER_TIME = 500;
	
	//data
	protected LinkedBlockingQueue<NoteEvent> noteQueue;
	protected List<ChordRecognitionListener> chordRecListeners;
	
	//state
	protected boolean running;

	//stop/resume state
	protected volatile boolean onStop_state = false;	
	
	//singleton
	protected MidiServer() {
		noteQueue = new LinkedBlockingQueue<NoteEvent>();
		chordRecListeners = new ArrayList<ChordRecognitionListener>();
		running = false;
	}
	protected static MidiServer instance = null;
	public static MidiServer getInstance() {
		if(instance==null) {
			instance = new MidiServer();
		}
		return instance;
	}

	/**
	 * Adds note events to queue.
	 * @param noteEvent
	 */
	public void addNoteEvent(NoteEvent noteEvent) {
		noteQueue.add(noteEvent);
	}
	
	/**
	 * Starts up server processing of MiDi.
	 */
	public void start() {
		if(!running) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					queue_thread();
				}
			});
			t.start();
		}
	}
	
	/**
	 * Stops midi processing.
	 */
	public void stop() {
		running = false;
	}
		
	/**
	 * Queue thread processing function
	 */
	public void queue_thread() {
		
		//init state
		String chord="";
		long lastTS = -1;		
		long lastSysTS = -1;
		
		//start loop
		running = true;
		while(running) {
			
			//something in queue
			while(!noteQueue.isEmpty()) {
				
				//get next note event
				NoteEvent noteEvent = noteQueue.remove();
				String noteName = parseNoteName(noteEvent.getNote());
				if(noteName.equals("")) {
					continue;
				}
				
				//no notes before
				if(lastTS==-1) {
					chord = noteName;
					lastTS = noteEvent.getTimestamp();
					lastSysTS = System.currentTimeMillis();
				}				
				else {
					long diffTS = Math.abs(noteEvent.getTimestamp()-lastTS);
					System.out.println(lastTS + " " + noteEvent.getTimestamp() + " " + diffTS);
					if(diffTS <= CHORD_BUFFER_TIME) {
						if(chord.indexOf(noteName)==-1) {
							chord = chord + " " + noteName;
						}
					}
					else {
						notifyRecognizers(chord);
						chord = noteName;
						lastTS = -1;
						lastSysTS = System.currentTimeMillis();
					}
				}				
			}
		
			//check case when nothing in queue and chord in progress
			if(!chord.equals("") && lastTS!=-1) {
				try {
					
					//wait time for buffer
					long elapsedTime = (System.currentTimeMillis()-lastSysTS);
					if(elapsedTime < CHORD_BUFFER_TIME) {
						long remainingTime = CHORD_BUFFER_TIME-elapsedTime;
						Thread.sleep(remainingTime);
					}
					
					//recheck queue to see if empty or if first elem is past time.
					if(noteQueue.isEmpty() || Math.abs(noteQueue.peek().getTimestamp()-lastTS) > CHORD_BUFFER_TIME) {
						if(!noteQueue.isEmpty()) {
							System.out.println(Math.abs(noteQueue.peek().getTimestamp()-lastTS));
						}
						notifyRecognizers(chord);
						chord = "";
						lastTS = -1;
						lastSysTS = System.currentTimeMillis();
					}
					
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Parse Note Names
	 * @param n Note
	 * @return
	 */
	public String parseNoteName(Note n) {
		String ms = n.getMusicString();
		for(int x=0; x < ms.length(); x++) {
			try {
				Integer.parseInt(""+ms.charAt(x));
				return ms.substring(0,x+1);
			}
			catch(Exception e) {}
		}
		
		//failure
		return "";
	}

	
	/**
	 * Adds Chord Recognition Listener
	 * @param l
	 */
	public void addChordRecognitionListener(ChordRecognitionListener l) {
		if(!chordRecListeners.contains(l)) {
			chordRecListeners.add(l);
		}
	}
	
	/**
	 * Removes Chord Recognition Listener
	 * @param l
	 */
	public void removeChordRecognitionListener(ChordRecognitionListener l) {
		chordRecListeners.remove(l);
	}
	
	/**
	 * Notify listeners that particular sequence of notes has been recognized.
	 * @param chord
	 */
	protected void notifyRecognizers(String chord) {
		for(ChordRecognitionListener l : chordRecListeners) {
			l.chordRecognized(chord);
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}		
	
	public void onStop() {
		onStop_state = running;
		if(onStop_state) {
			this.stop();
		}
	}
	
	public void onResume() {
		if(onStop_state) {
			this.start();
		}
	}
}