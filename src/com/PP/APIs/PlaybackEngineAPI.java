package com.PP.APIs;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGTrack;

public class PlaybackEngineAPI {

	
	public static void playClip(TGSong song, String dirPath, int m0, int mf,int track, double tempoScale) {
		try {
			
			//write MIDI file for selection
			TGSong newSong = extractMeasures(song, track,m0, mf);
			TuxGuitarUtil.exportToGP4(dirPath + FileOpAPI.TEMP_GP4, newSong);		
			TGSong s3 = TuxGuitarUtil.loadSong(new FileInputStream(dirPath + FileOpAPI.TEMP_GP4));			
			scaleTempo(s3, tempoScale); //scale tempo for playback speed			
			TuxGuitarUtil.exportToMidi(dirPath + FileOpAPI.TEMP_MID, s3);
			
			//play
			MediaPlayerAPI.getInstance().play(dirPath + FileOpAPI.TEMP_MID);
						
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playClip_multiTrack(TGSong song, String dirPath, int m0, int mf,List<Integer> trackInds, double tempoScale) {
		try {
			
			//write MIDI file for selection
			TGSong newSong = extractMeasures_multiTrack(song, trackInds,m0, mf);
			TuxGuitarUtil.exportToGP4(dirPath + FileOpAPI.TEMP_GP4, newSong);		
			TGSong s3 = TuxGuitarUtil.loadSong(new FileInputStream(dirPath + FileOpAPI.TEMP_GP4));			
			scaleTempo(s3, tempoScale); //scale tempo for playback speed			
			TuxGuitarUtil.exportToMidi(dirPath + FileOpAPI.TEMP_MID, s3);
			
			//play
			MediaPlayerAPI.getInstance().play(dirPath + FileOpAPI.TEMP_MID);
						
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playClip_beats(TGSong song, String dirPath, int b0, int bf, int track, double tempoScale) {
		try {
			
			//compute which measures to extract
			TGTrack trackObj = song.getTrack(track);
			int m0=-1;
			int m0_beat = -1;
			int mf=-1;
			int c_beat=0;
			for(int x=0; x < trackObj.countMeasures(); x++) {
				TGMeasure measure = trackObj.getMeasure(x);
				for(int y=0; y < measure.countBeats(); y++) {
					if(c_beat == b0 && m0==-1) {
						m0 = x;
						m0_beat = (c_beat-y);
					}
					if(c_beat == bf && mf==-1) {
						mf = x;
					}
					if(m0!=-1 && mf!=-1) {
						break;
					}
					c_beat = c_beat + 1;
				}
			}
			
			//extract measures and find make rests of stuff that don't fall
			//into beat range
			TGSong newSong = extractMeasures(song, track,m0, mf);
			trackObj = song.getTrack(track);
			int beat_ctr=m0_beat;
			for(int x=0; x < trackObj.countMeasures(); x++) {
				TGMeasure measure = trackObj.getMeasure(x);
				for(int y=0; y < measure.countBeats(); y++) {
					TGBeat beat = measure.getBeat(y);
					if(!(beat_ctr >= b0 && beat_ctr <= bf)) {
						beat.clearVoices();
					}
					beat_ctr++;
				}
			}
						
			//write MIDI file for selection
			TuxGuitarUtil.exportToGP4(dirPath + FileOpAPI.TEMP_GP4, newSong);
			FileInputStream fs = new FileInputStream(dirPath + FileOpAPI.TEMP_GP4);
			TGSong s3 = TuxGuitarUtil.loadSong(fs);
			scaleTempo(s3,tempoScale);			
			TuxGuitarUtil.exportToMidi(dirPath + FileOpAPI.TEMP_MID, s3);
			
			//play
			MediaPlayerAPI.getInstance().play(dirPath + FileOpAPI.TEMP_MID);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void scaleTempo(TGSong song, double tempoScale) {
		TGTrack track = song.getTrack(0);
		for(int y=0; y<track.countMeasures(); y++) {
			TGMeasure m = track.getMeasure(y);
			TGMeasureHeader h = m.getHeader();
			TGTempo tempo = h.getTempo();
			tempo.setValue((int)Math.round(tempo.getValue()*tempoScale));
		}
	}	
	
	public static void cleanUp(String dirPath) {
		try {
			
			//remove temporary files
			File f1 = new File(dirPath + FileOpAPI.TEMP_GP4);
			f1.delete();
			File f2 = new File(dirPath + FileOpAPI.TEMP_MID);
			f2.delete();
			
			//call garbage collector
			System.gc();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static TGSong extractMeasures_multiTrack(TGSong originalSong, List<Integer> trackInds, int m0, int mf) {
		List<List<TGMeasure>> relMeasures = new ArrayList<List<TGMeasure>>();
		List<TGMeasureHeader> relHeaders = new ArrayList<TGMeasureHeader>();
		TGFactory f = new TGFactory();		
		for(int y=0; y < trackInds.size(); y++) {
			int track = trackInds.get(y);
			TGTrack trackObj = originalSong.getTrack(track);
			List<TGMeasure> relMeasuresC = new ArrayList<TGMeasure>();
			for(int x=m0; x <= mf; x++) 
			{				
				TGMeasureHeader clonedMeasureHeader = originalSong.getMeasureHeader(x).clone(f);
				TGMeasure clonedMeasure = trackObj.getMeasure(x).clone(f,clonedMeasureHeader);				
				relMeasuresC.add(clonedMeasure);
				if(y==0) {
					relHeaders.add(clonedMeasureHeader);					
				}				
			}
			relMeasures.add(relMeasuresC);
		}
				
		//create new TGSong (cloned)
		TGSong song = TuxGuitarUtil.isolateTracks(originalSong, trackInds);
		for(int x=0; x < song.countTracks(); x++) {
			song.getTrack(x).clear();			
		}
		song.clearMeasureHeaders();
		for(int x=0; x < relHeaders.size(); x++) {
			
			//get
			TGMeasureHeader h = relHeaders.get(x);			
			
			//remove repeats
			h.setRepeatOpen(false);
			h.setRepeatClose(0);
			
			//add
			song.addMeasureHeader(h);
			
			//add all track measures
			for(int y=0; y < trackInds.size(); y++) {
			
				//get
				TGMeasure m = relMeasures.get(y).get(x);
						
				//add
				song.getTrack(y).addMeasure(m);
			}
		}
				
		return song;
	}

	
	public static TGSong extractMeasures(TGSong originalSong, int track, int m0, int mf) {
		TGTrack trackObj = originalSong.getTrack(track);
		List<TGMeasure> relMeasures = new ArrayList<TGMeasure>();
		List<TGMeasureHeader> relHeaders = new ArrayList<TGMeasureHeader>();
		TGFactory f = new TGFactory();
		for(int x=m0; x <= mf; x++) {
			TGMeasureHeader clonedMeasureHeader = originalSong.getMeasureHeader(x).clone(f);
			TGMeasure clonedMeasure = trackObj.getMeasure(x).clone(f,clonedMeasureHeader);
			relMeasures.add(clonedMeasure);
			relHeaders.add(clonedMeasureHeader);
		}
				
		//create new TGSong (cloned)
		TGSong song = TuxGuitarUtil.isolateTrack(originalSong, track);
		song.getTrack(0).clear();
		song.clearMeasureHeaders();
		for(int x=0; x < relMeasures.size(); x++) {
			
			//get
			TGMeasure m = relMeasures.get(x);
			TGMeasureHeader h = relHeaders.get(x);
			
			//remove repeats
			h.setRepeatOpen(false);
			h.setRepeatClose(0);
			
			//add
			song.addMeasureHeader(h);
			song.getTrack(0).addMeasure(m);
		}
				
		return song;
	}
	
}
