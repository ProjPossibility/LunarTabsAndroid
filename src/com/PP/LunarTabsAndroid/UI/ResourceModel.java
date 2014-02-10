package com.PP.LunarTabsAndroid.UI;

import android.content.Context;

import com.example.lunartabsandroid.R;

public class ResourceModel {
	
	//context
	protected Context context;
	
	//fields
	public String[] voiceCommands;
	public String ERROR_NO_PREV_INST;
	public String ERROR_NO_DATA;
	public String ERROR_NO_FILE_LOADED;
	public String ERROR_NO_NEXT_INST; 
	public String ERROR_LAST_SECTION;
	public String ERROR_FIRST_SECTION;	
	public String CAPO;
	public String DISABLE_STOMP_MODE;
	public String ENABLE_STOMP_MODE;
	public String DISABLE_VOICE_ACTIONS;
	public String ENABLE_VOICE_ACTIONS;
	public String DISABLE_MIDI_FOLLOWING;
	public String ENABLE_MIDI_FOLLOWING;
	public String ERROR_NO_INST_SELECTED;
	public String FILE_LOADED_SPEECH;
	public String ERROR_FILE_NOT_LOADED; 
	public String MIDI_FOLLOWING_DIALOG_TITLE;
	public String MEASURE;
	public String TO_MEASURE;	
	public String STOMPER_ENABLE_DIALOG_TITLE;
	public String[] langMod;
	public String CHORD;
	public String SHARP;
	public String GUITAR_INST_GEN_STRING;
	public String GUITAR_INST_GEN_FRET;
	public String TIED_MOD;
	public String DOTTED_MOD;
	public String DOUBLE_DOTTED_MOD;
	public String REST_STR;
	public String[] DURATIONS;
	public String PLAY_WITH;
	public String AND;
	public String[] NOTE_EFFECTS;
	public String LYRIC;
	public String REPEAT_MEASURE;
	public String TIMES;
	public String REPEAT_MEASURES;
	public String TO;
	public String MORE_TIMES;
	public String[] BASE_NUMBERS;
	public String[] TENS_DIGIT;
	public String THOUSAND;
	public String MILLION;
	public String BILLION;
	public String[] CHORD_DESCRIPTORS;
	public String[] DRUMS;
	public String UNKNOWN_DRUM;
	public int ACTIVATOR_DELAY;
	public String STOMP_ON_NOTIF;
	public String STOMP_OFF_NOTIF;
	public String VOICE_OFF_NOTIF;
	
	//singleton
	protected ResourceModel() {
	}
	protected static ResourceModel instance;
	public static ResourceModel getInstance() {
		if(instance==null){
			instance = new ResourceModel();
		}
		return instance;
	}
	
	public void loadResources(Context context) {
        voiceCommands = context.getResources().getStringArray(R.array.VOICE_COMMANDS); 		
		ERROR_NO_PREV_INST = context.getString(R.string.ERROR_NO_PREV_INST);
		ERROR_NO_DATA = context.getString(R.string.ERROR_NO_DATA);
		ERROR_NO_FILE_LOADED = context.getString(R.string.ERROR_NO_FILE_LOADED);
		ERROR_NO_NEXT_INST = context.getString(R.string.ERROR_NO_NEXT_INST);
		ERROR_LAST_SECTION = context.getString(R.string.ERROR_LAST_SECTION);
		ERROR_FIRST_SECTION = context.getString(R.string.ERROR_FIRST_SECTION);
		CAPO = context.getString(R.string.Capo);
		DISABLE_STOMP_MODE = context.getString(R.string.DisableStompMode);
		ENABLE_STOMP_MODE = context.getString(R.string.EnableStompMode);
		DISABLE_VOICE_ACTIONS = context.getString(R.string.DisableVoiceActions);
		ENABLE_VOICE_ACTIONS = context.getString(R.string.EnableVoiceActions);
		DISABLE_MIDI_FOLLOWING = context.getString(R.string.DisableMidiFollowing);
		ENABLE_MIDI_FOLLOWING = context.getString(R.string.EnableMidiFollowing);
		ERROR_NO_INST_SELECTED = context.getString(R.string.ERROR_NO_INST_SELECTED);
    	FILE_LOADED_SPEECH = context.getString(com.example.lunartabsandroid.R.string.FILE_LOADED);
    	ERROR_FILE_NOT_LOADED = context.getString(com.example.lunartabsandroid.R.string.ERROR_FILE_NOT_LOADED);
		MIDI_FOLLOWING_DIALOG_TITLE = context.getString(R.string.midi_following_dialog_title);    	
		MEASURE = context.getString(R.string.select_section_dialog_measure);
		TO_MEASURE = context.getString(R.string.select_section_dialog_to_measure);
		STOMPER_ENABLE_DIALOG_TITLE = context.getString(R.string.stomper_enable_dialog_title);
		langMod = context.getResources().getStringArray(R.array.LANG_MOD);
		CHORD = context.getString(R.string.guitar_inst_gen_chord);
		SHARP = context.getString(R.string.guitar_inst_gen_sharp);
		GUITAR_INST_GEN_STRING = context.getString(R.string.guitar_inst_gen_string);
		GUITAR_INST_GEN_FRET = context.getString(R.string.guitar_inst_gen_fret);
		TIED_MOD = context.getString(com.example.lunartabsandroid.R.string.inst_gen_tied);
		DOTTED_MOD = context.getString(com.example.lunartabsandroid.R.string.inst_gen_dotted);
		DOUBLE_DOTTED_MOD = context.getString(com.example.lunartabsandroid.R.string.inst_gen_double_dotted);
		REST_STR = context.getString(com.example.lunartabsandroid.R.string.inst_gen_rest); 
		DURATIONS = context.getResources().getStringArray(com.example.lunartabsandroid.R.array.INST_GEN_DURATIONS);
		PLAY_WITH = context.getString(com.example.lunartabsandroid.R.string.inst_gen_play_with);
		AND = context.getString(com.example.lunartabsandroid.R.string.inst_gen_and);
		NOTE_EFFECTS = context.getResources().getStringArray(com.example.lunartabsandroid.R.array.INST_GEN_NOTE_EFFECTS);
		LYRIC = context.getString(com.example.lunartabsandroid.R.string.inst_gen_lyric);
		REPEAT_MEASURE = context.getString(R.string.repeat_inst_gen_repeat_measure);
		TIMES = context.getString(R.string.repeat_inst_gen_times);
		REPEAT_MEASURES = context.getString(R.string.repeat_inst_gen_repeat_measures);						
		TO = context.getString(R.string.repeat_inst_gen_to);
		MORE_TIMES = context.getString(R.string.repeat_inst_gen_more_times);
		BASE_NUMBERS = context.getResources().getStringArray(R.array.BASE_NUMBERS);
		TENS_DIGIT = context.getResources().getStringArray(R.array.TENS_DIGIT);
		THOUSAND = context.getString(R.string.words_thousand);
		MILLION = context.getString(R.string.words_million);
		BILLION = context.getString(R.string.words_billion);
		CHORD_DESCRIPTORS = context.getResources().getStringArray(R.array.CHORD_DESCRIPTORS);
		DRUMS = context.getResources().getStringArray(R.array.DRUMS);
		UNKNOWN_DRUM = context.getResources().getString(R.string.unknown_drum);
		ACTIVATOR_DELAY = context.getResources().getInteger(R.integer.ACTIVATOR_DELAY);
		STOMP_ON_NOTIF = context.getResources().getString(R.string.stomp_mode_enabled_notif);
		STOMP_OFF_NOTIF = context.getResources().getString(R.string.stomp_mode_disabled_notif);
		VOICE_OFF_NOTIF = context.getResources().getString(R.string.voice_disabled_notif);
	}
}
