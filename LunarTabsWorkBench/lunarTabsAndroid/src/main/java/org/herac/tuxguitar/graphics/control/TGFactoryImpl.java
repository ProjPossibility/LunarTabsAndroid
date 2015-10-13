package org.herac.tuxguitar.graphics.control;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;

public class TGFactoryImpl extends TGFactory{
	
	public TGFactoryImpl(){
		super();
	}
	
	@Override
	public TGMeasureHeader newHeader(){
		return new TGMeasureHeaderImpl(this);
	}
	
	@Override
	public TGTrack newTrack(){
		return new TGTrackImpl(this);
	}
	
	@Override
	public TGMeasure newMeasure(TGMeasureHeader header){
		return new TGMeasureImpl(header);
	}
	
	@Override
	public TGNote newNote(){
		return new TGNoteImpl(this);
	}
	
	@Override
	public TGBeat newBeat(){
		return new TGBeatImpl(this);
	}
	
	@Override
	public TGVoice newVoice(int index){
		return new TGVoiceImpl(this, index);
	}
	
	@Override
	public TGLyric newLyric(){
		return new TGLyricImpl();
	}
	
	@Override
	public TGChord newChord(int length){
		return new TGChordImpl(length);
	}
	
	@Override
	public TGText newText(){
		return new TGTextImpl();
	}
}
