package com.tuxguitar.song.factory;

import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGChannel;
import com.tuxguitar.song.models.TGChord;
import com.tuxguitar.song.models.TGColor;
import com.tuxguitar.song.models.TGDuration;
import com.tuxguitar.song.models.TGLyric;
import com.tuxguitar.song.models.TGMarker;
import com.tuxguitar.song.models.TGMeasure;
import com.tuxguitar.song.models.TGMeasureHeader;
import com.tuxguitar.song.models.TGNote;
import com.tuxguitar.song.models.TGNoteEffect;
import com.tuxguitar.song.models.TGScale;
import com.tuxguitar.song.models.TGSong;
import com.tuxguitar.song.models.TGString;
import com.tuxguitar.song.models.TGStroke;
import com.tuxguitar.song.models.TGTempo;
import com.tuxguitar.song.models.TGText;
import com.tuxguitar.song.models.TGTimeSignature;
import com.tuxguitar.song.models.TGTrack;
import com.tuxguitar.song.models.TGTupleto;
import com.tuxguitar.song.models.TGVoice;
import com.tuxguitar.song.models.effects.TGEffectBend;
import com.tuxguitar.song.models.effects.TGEffectGrace;
import com.tuxguitar.song.models.effects.TGEffectHarmonic;
import com.tuxguitar.song.models.effects.TGEffectTremoloBar;
import com.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import com.tuxguitar.song.models.effects.TGEffectTrill;

public class TGFactory {
	
	public TGSong newSong(){
		return new TGSong() {
			//TGSong Implementation
		};
	}
	
	public TGLyric newLyric(){
		return new TGLyric(){
			//TGLyric Implementation
		};
	}
	
	public TGMarker newMarker(){
		return new TGMarker(this){
			//TGMarker Implementation
		};
	}
	
	public TGChord newChord(int length){
		return new TGChord(length){
			//TGChord Implementation
		};
	}
	
	public TGScale newScale(){
		return new TGScale(){
			//TGScale Implementation
		};
	}
	
	public TGColor newColor(){
		return new TGColor(){
			//TGColor Implementation
		};
	}
	
	public TGTupleto newTupleto(){
		return new TGTupleto(){
			//TGTupleto Implementation
		};
	}
	
	public TGDuration newDuration(){
		return new TGDuration(this){
			//TGDuration Implementation
		};
	}
	
	public TGTimeSignature newTimeSignature(){
		return new TGTimeSignature(this){
			//TGTimeSignature Implementation
		};
	}
	
	public TGTempo newTempo(){
		return new TGTempo(){
			//TGTempo Implementation
		};
	}
	
	public TGChannel newChannel(){
		return new TGChannel(){
			//TGChannel Implementation
		};
	}
	
	public TGTrack newTrack(){
		return new TGTrack(this){
			//TGTrack Implementation
		};
	}
	
	public TGMeasureHeader newHeader(){
		return new TGMeasureHeader(this){
			//TGMeasureHeader Implementation
		};
	}
	
	public TGMeasure newMeasure(TGMeasureHeader header){
		return new TGMeasure(header){
			//TGMeasure Implementation
		};
	}
	
	public TGBeat newBeat(){
		return new TGBeat(this){
			//TGBeat Implementation
		};
	}
	
	public TGVoice newVoice(int index){
		return new TGVoice(this, index){
			//TGVoice Implementation
		};
	}
	
	public TGNote newNote(){
		return new TGNote(this){
			//TGNote Implementation
		};
	}
	
	public TGString newString(){
		return new TGString(){
			//TGString Implementation
		};
	}
	
	public TGStroke newStroke(){
		return new TGStroke(){
			//TGString Implementation
		};
	}
	
	public TGText newText(){
		return new TGText(){
			//TGString Implementation
		};
	}
	
	public TGNoteEffect newEffect(){
		return new TGNoteEffect(){
			//TGNoteEffect Implementation
		};
	}
	
	public TGEffectBend newEffectBend(){
		return new TGEffectBend(){
			//TGEffectBend Implementation
		};
	}
	
	public TGEffectTremoloBar newEffectTremoloBar(){
		return new TGEffectTremoloBar(){
			//TGEffectTremoloBar Implementation
		};
	}
	
	public TGEffectGrace newEffectGrace(){
		return new TGEffectGrace(){
			//TGEffectGrace Implementation
		};
	}
	
	public TGEffectHarmonic newEffectHarmonic(){
		return new TGEffectHarmonic(){
			//TGEffectHarmonic Implementation
		};
	}
	
	public TGEffectTrill newEffectTrill(){
		return new TGEffectTrill(this){
			//TGEffectTrill Implementation
		};
	}
	
	public TGEffectTremoloPicking newEffectTremoloPicking(){
		return new TGEffectTremoloPicking(this){
			//TGEffectTremoloPicking Implementation
		};
	}
}
