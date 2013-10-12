package com.PP.LunarTabsAndroid.APIs;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import java.util.*;

import android.media.MediaPlayer;

import com.tuxguitar.io.base.TGSongLoader;
import com.tuxguitar.io.gtp.GP4OutputStream;
import com.tuxguitar.io.gtp.GTPSettings;
import com.tuxguitar.io.midi.MidiSongExporter;
import com.tuxguitar.song.factory.TGFactory;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGMeasure;
import com.tuxguitar.song.models.TGMeasureHeader;
import com.tuxguitar.song.models.TGNote;
import com.tuxguitar.song.models.TGSong;
import com.tuxguitar.song.models.TGTrack;
import com.tuxguitar.song.models.TGVoice;

public class TuxGuitarUtil {
	
	public static void cleanUp(String dirPath) {
		try {
			File f1 = new File(dirPath + FileOpAPI.TEMP_GP4);
			f1.delete();
			File f2 = new File(dirPath + FileOpAPI.TEMP_MID);
			f2.delete();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playClip(String file, String dirPath, int m0, int mf,int track) {
		try {
			
			//write MIDI file for measure
			TGSong song = TuxGuitarUtil.loadSong(file);
			TGSong newSong = TuxGuitarUtil.extractMeasures(song, track,m0, mf);
			TuxGuitarUtil.exportToGP4(dirPath + FileOpAPI.TEMP_GP4, newSong);		
			TGSong s3 = TuxGuitarUtil.loadSong(dirPath + FileOpAPI.TEMP_GP4);			
			TuxGuitarUtil.exportToMidi(dirPath + FileOpAPI.TEMP_MID, s3);
			
			//play
			MediaPlayerAPI.getInstance().play(dirPath + FileOpAPI.TEMP_MID);
			
			/*
			Sequence sequence = MidiSystem.getSequence(new File("test.mid"));
			
			// Create a sequencer for the sequence
	        Sequencer sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        sequencer.setSequence(sequence);
	        
	//        Start playing
	        sequencer.start();
	        */
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportToGP4(String file, TGSong song) {
		GP4OutputStream g = new GP4OutputStream(new GTPSettings());
		try {
			g.init(new TGFactory(), new BufferedOutputStream(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.writeSong(song);
	}
	
	public static TGSong extractMeasures(TGSong originalSong, int track, int m0, int mf) {
		TGTrack trackObj = originalSong.getTrack(track);
		List<TGMeasure> relMeasures = new ArrayList<TGMeasure>();
		List<TGMeasureHeader> relHeaders = new ArrayList<TGMeasureHeader>();
		for(int x=m0; x <= mf; x++) {
			relMeasures.add(trackObj.getMeasure(x));
			relHeaders.add(originalSong.getMeasureHeader(x));
		}
				
		//create new TGSong
		TGSong song = isolateTrack(originalSong, track);
		song.getTrack(0).clear();
		song.clearMeasureHeaders();
		for(int x=0; x < relMeasures.size(); x++) {
			TGMeasure m = relMeasures.get(x);
			TGMeasureHeader h = relHeaders.get(x);
			song.addMeasureHeader(h);
			song.getTrack(0).addMeasure(m);
		}
				
		return song;
	}

	public static TGSong isolateTrack(TGSong song, int t) {
		TGSong rtn = song.clone(new TGFactory());
		rtn.clearTracks();
		TGTrack track = song.getTrack(t);
		rtn.addTrack(track);
		return rtn;
	}
		
	public static TGSong loadSong(String file) throws Exception{
		TGSongLoader l = new TGSongLoader();
		return l.load(new TGFactory(), new FileInputStream(file));
	}
	
	public static void exportToMidi(String file, TGSong song) {
		MidiSongExporter e = new MidiSongExporter();
		try {
			e.exportSong(new BufferedOutputStream(new FileOutputStream(file)), song);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
    public static List<TGNote> getNotesForBeat(TGBeat beat) {
        List<TGNote> rtn = new ArrayList<TGNote>();
        for(int x=0; x < beat.countVoices(); x++) {
                TGVoice currentVoice = beat.getVoice(x);
                for(int y=0; y < currentVoice.countNotes(); y++) {
                        rtn.add(currentVoice.getNote(y));
                }
        }
        return rtn;
    }
    
	public static List<TGMeasure> getMeasures(TGTrack track) {
		List<TGMeasure> rtn = new ArrayList<TGMeasure>();
		for(int x=0; x < track.countMeasures(); x++) {
			rtn.add(track.getMeasure(x));
		}
		return rtn;
	}
	
	public static TGTrack getTrack(TGSong song, int trackIndex) {
		return song.getTrack(trackIndex);
	}    
}
