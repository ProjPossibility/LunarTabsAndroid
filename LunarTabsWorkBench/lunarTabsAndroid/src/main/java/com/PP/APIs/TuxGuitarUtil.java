package com.PP.APIs;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.herac.tuxguitar.io.base.TGSongLoader;
import org.herac.tuxguitar.io.gtp.GP4OutputStream;
import org.herac.tuxguitar.io.gtp.GTPSettings;
import org.herac.tuxguitar.io.midi.MidiSongExporter;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;

import android.content.res.AssetManager;


public class TuxGuitarUtil {
	
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
	
			
	public static void exportToMidi(String file, TGSong song) {
		MidiSongExporter e = new MidiSongExporter();
		try {
			e.init(new TGFactory(), new BufferedOutputStream(new FileOutputStream(file)));
			e.configure(true);
			e.exportSong(song);
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
        	
	/**
	 * Extract measures for particular track range
	 * @param song
	 * @param minTrack
	 * @param maxTrack
	 * @return
	 */
	public static Map<String, List<TGMeasure>> extractAllMeasures(TGSong song, int minTrack, int maxTrack) 
	{
		Map<String, List<TGMeasure>> rtn = new LinkedHashMap<String,List<TGMeasure>>();
		for(int x=minTrack; x <= maxTrack && x < song.countTracks(); x++) {
			TGTrack t = song.getTrack(x);
			TGChannel c= song.getChannel(x);
			String name = "T" + x + "_" + c.getBank() + "_" + t.getName();
			List<TGMeasure> measures = extractMeasures_rtnMeas(song, x, 0, t.countMeasures()-1);
			rtn.put(name, measures);
		}
		return rtn;
	}
	
	/**
	 * Extract measures from song for particular track
	 * @param song
	 * @param track
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<TGMeasure> extractMeasures_rtnMeas(TGSong song, int track, int min, int max) {
		TGTrack t = song.getTrack(track);
		List<TGMeasure> rtn = new ArrayList<TGMeasure>();
		for(int x=min; x <= max; x++) {
			rtn.add(t.getMeasure(x));
		}
		return rtn;
	}
	
	/**
	 * Extract measures from song for particular track
	 * @param song
	 * @param track
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<TGMeasure> extractMeasures_rtnMeas(TGTrack t, int min, int max) {
		List<TGMeasure> rtn = new ArrayList<TGMeasure>();
		for(int x=min; x <= max; x++) {
			rtn.add(t.getMeasure(x));
		}
		return rtn;
	}
		
	/**
	 * Extracts beat in particular range from track
	 * @param track The track to extract from
	 * @param b0 first beat (inclusive)
	 * @param bf last beat (inclusive)
	 * @return
	 */
	public static List<TGBeat> getBeats(TGTrack track, int b0, int bf) {
		List<TGBeat> rtn = new ArrayList<TGBeat>();
		int beat_ctr=0;
		outer:for(int x=0; x < track.countMeasures(); x++) {
			TGMeasure measure = track.getMeasure(x);
			for(int y=0; y < measure.countBeats(); y++) {
				if(beat_ctr >= b0 && beat_ctr <= bf) {
					rtn.add(measure.getBeat(y));
				}
				if(beat_ctr > bf) {
					break outer;
				}
				beat_ctr++;
			}
		}
		return rtn;
	}
	
	/**
	 * Gets measures as list from track
	 * @param track The track to extract measures from
	 * @return
	 */
	public static List<TGMeasure> getMeasures(TGTrack track) {
		List<TGMeasure> rtn = new ArrayList<TGMeasure>();
		for(int x=0; x < track.countMeasures(); x++) {
			rtn.add(track.getMeasure(x));
		}
		return rtn;
	}
	
	/**
	 * Returns track from song of particular index.
	 * @param song Original Song
	 * @param trackIndex The track index
	 * @return
	 */
	public static TGTrack getTrack(TGSong song, int trackIndex) {
		return song.getTrack(trackIndex);
	} 
	
	/**
	 * Returns song with particular track isolated. Song only contains 
	 * specified track of track number.
	 * @param song Original Song
	 * @param t track number
	 * @return
	 */
	public static TGSong isolateTrack(TGSong song, int t) {
		TGSong rtn = song.clone(new TGFactory());
		rtn.clearTracks();
		TGTrack track = song.getTrack(t).clone(new TGFactory(), song);
		rtn.addTrack(track);
		rtn.clearChannels();
		rtn.addChannel(song.getChannel(t));
		return rtn;
	}
	
	/**
	 * Returns song with particular tracks isolated. Song only contains 
	 * specified track of track number.
	 * @param song Original Song
	 * @param t track number
	 * @return
	 */
	public static TGSong isolateTracks(TGSong song, List<Integer> tracks) {
		TGSong rtn = song.clone(new TGFactory());
		rtn.clearTracks();
		rtn.clearChannels();		
		for(int x=0; x < tracks.size(); x++) {
			int t = tracks.get(x);
			TGTrack track = song.getTrack(t).clone(new TGFactory(), song);
			rtn.addTrack(track);
			rtn.addChannel(song.getChannel(t));			
		}
		return rtn;
	}
	
	
	/**
	 * Load song file from assets.
	 * @param file
	 * @param assManager
	 * @return
	 */
	public static TGSong loadSong(String file, AssetManager assManager) {
		InputStream is;
		try {
	        is = assManager.open(file);
			TGSongLoader l = new TGSongLoader();	        
	        return l.load(new TGFactory(), is);
	    }
		catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static TGSong loadSong(InputStream is) throws Exception {
		
		//load song using song loader
		TGSongLoader l = new TGSongLoader();
		TGSong song = l.load(new TGFactory(), is);
		
		//reformat structure to add lyrics to beat objects
		addDerivedLyricsToBeatsOfSong(song);
		
		//return
		return song;
		
	}
	
	/**
	 * Restore lyrics in beat objects of song.
	 * @param song
	 */
	public static void addDerivedLyricsToBeatsOfSong(TGSong song) {
		for(int x=0; x < song.countTracks(); x++) {
			TGTrack track = song.getTrack(x);
			
			//lyrics stored by beat
			TGLyric lyrics = track.getLyrics();
			if(lyrics!=null) {
				String[] lyricBeats = lyrics.getLyricBeats();
				int lyricStart = lyrics.getFrom();
				if(lyricStart >= 0) {
					List<TGMeasure> measures = getMeasures(track);
					int lyricBeatIndex=0;
					for(int y=lyricStart; y< measures.size() && lyricBeatIndex < lyricBeats.length; y++) {
						TGMeasure measure = measures.get(y);
						for(int z=0; z < measure.countBeats() && lyricBeatIndex < lyricBeats.length; z++) {
							TGBeat b = measure.getBeat(z);
							if(!b.isRestBeat()) {
								b.setStoredLyric(lyricBeats[lyricBeatIndex]);
								lyricBeatIndex++;
							}
						}
					}
				}				
			}			
		}
	}
}