package com.PP.LunarTabsAndroid.Activities;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.PP.APIs.FileOpAPI;
import com.PP.APIs.MediaPlayerAPI;
import com.PP.APIs.MetronomeAPI;
import com.PP.APIs.PlaybackEngineAPI;
import com.PP.APIs.TextToSpeechAPI;
import com.PP.APIs.VolumeAPI;
import com.PP.APIs.WordActivatorAPI;
import com.PP.AudioIcon.AudioIconAPI;
import com.PP.InstrumentModels.ChordDB;
import com.PP.InstrumentModels.ChordRecognizer;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.IntelliSeg.MarkerSegmenter.MarkerSegmenter;
import com.PP.IntelliSeg.MeasureIncrementSegmenter.MeasureIncrementSegmenter;
import com.PP.IntelliSeg.RepetionSegmenter.SMRSegmenter.SMRSegmenter;
import com.PP.LunarTabsAndroid.Dialogs.FileSelectionMethodDialog;
import com.PP.LunarTabsAndroid.Dialogs.GuitarFileLoaderDialog;
import com.PP.LunarTabsAndroid.UI.AccListView;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.InstructionContentDescription;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.PP.MidiServer.AbstractMidiServerActivity;
import com.PP.MidiServer.ChordRecognitionListener;
import com.PP.MidiServer.MidiServer;
import com.PP.StompDetector.InstructionStomp;
import com.PP.StompDetector.StompDetector;
import com.example.lunartabsandroid.R;
import com.root.gast.speech.activation.SpeechActivationListener;

public class MainActivity extends AbstractMidiServerActivity implements OnClickListener, SpeechActivationListener, ChordRecognitionListener  {
	
	//debug fags
	protected static final boolean MIDI_FOLLOWER_DEBUG = false;
	protected static final String FRAGMENT_MANAGER_TAG = "LunarTabs";

	//components
	protected Button loadTabFileButton;
	protected Button toggleModesButton;
	protected Button playSampleButton;
	protected Button prevMeasButton;
	protected Button nextMeasButton;
	protected Button upButton;
	protected Button downButton;
	protected Spinner trackChooser;
	protected AccListView instructionsList;
	
	//stomp detector
	protected static StompDetector stomper = null;	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//init stuff
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_main);
	    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	
	    ResourceModel.getInstance().loadResources(this);
	    DataModel.getInstance().setMainActivity(this);
	    
		//load components
        loadTabFileButton = (Button) findViewById(R.id.loadTabFileButton);
        toggleModesButton = (Button) findViewById(R.id.toggleModesButton);
        playSampleButton = (Button) findViewById(R.id.playSampleButton);
        prevMeasButton = (Button) findViewById(R.id.prevMeasButton);
        nextMeasButton = (Button) findViewById(R.id.nextMeasButton);
        upButton = (Button) findViewById(R.id.upButton);
        downButton = (Button) findViewById(R.id.downButton);
        trackChooser = (Spinner) findViewById(R.id.trackChooser);
        instructionsList = (AccListView) findViewById(R.id.instructionsList);
                
        //register listeners
        if(!loadTabFileButton.hasOnClickListeners()) {
        	loadTabFileButton.setOnClickListener(this);
        }
        if(!toggleModesButton.hasOnClickListeners()) {
        	toggleModesButton.setOnClickListener(this);
        }
        if(!playSampleButton.hasOnClickListeners()) {
        	playSampleButton.setOnClickListener(this);
        }
        if(!prevMeasButton.hasOnClickListeners()) {
        	prevMeasButton.setOnClickListener(this);
        }
        if(!nextMeasButton.hasOnClickListeners()) {
        	nextMeasButton.setOnClickListener(this);
        }
        if(!upButton.hasOnClickListeners()) {
        	upButton.setOnClickListener(this);
        }
        if(!downButton.hasOnClickListeners()) {
        	downButton.setOnClickListener(this);
        }
        
        //colors
        loadTabFileButton.setBackgroundColor(Color.WHITE);
        loadTabFileButton.setTextColor(Color.BLACK);
        toggleModesButton.setBackgroundColor(Color.WHITE);
        toggleModesButton.setTextColor(Color.BLACK);
        playSampleButton.setBackgroundColor(Color.WHITE);
        playSampleButton.setTextColor(Color.BLACK);
        prevMeasButton.setBackgroundColor(Color.WHITE);
        prevMeasButton.setTextColor(Color.BLACK);
        nextMeasButton.setBackgroundColor(Color.WHITE);
        nextMeasButton.setTextColor(Color.BLACK);
        upButton.setBackgroundColor(Color.WHITE);
        upButton.setTextColor(Color.BLACK);
        downButton.setBackgroundColor(Color.WHITE);
        downButton.setTextColor(Color.BLACK);
        
        //init components
        int hilightColor = getResources().getColor(R.color.background_holo_light);        
        instructionsList.init(hilightColor,Color.WHITE);
        
        //set up segmenter
        int choice = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).getInt("set_section_increment_pref", 0);
		String[] incChoices = getResources().getStringArray(R.array.MeasureIncArr);        
		if(choice==(incChoices.length-3)) {
			DataModel dataModel = DataModel.getInstance();
			MeasureIncrementSegmenter m;
			if(dataModel.getSegmenter()!=null && dataModel.getSegmenter() instanceof MeasureIncrementSegmenter) {
				m = (MeasureIncrementSegmenter) dataModel.getSegmenter();
			}
			else {
				m = new MeasureIncrementSegmenter();
				dataModel.setSegmenter(m);
			}
        	DataModel.getInstance().setSegmenter(m);	 
			
		}
		else if(choice==(incChoices.length-2)) {
	        DataModel.getInstance().setSegmenter(new SMRSegmenter());	 
        }
        else if(choice==(incChoices.length-1)) {
	        DataModel.getInstance().setSegmenter(new MarkerSegmenter());	 
        }
        else {
        	MeasureIncrementSegmenter m = new MeasureIncrementSegmenter();
			int newInc = Integer.parseInt(incChoices[choice]);
			m.setIncrement(newInc);
        	DataModel.getInstance().setSegmenter(m);	 
        }
        
        //enable APIs
        TextToSpeechAPI.init(this);
        
        //init data directory
        FileOpAPI.init();
        PlaybackEngineAPI.cleanUp(FileOpAPI.SAVE_PATH);
        
        //Chord DB initialize
        ChordDB.getInstance();
        
        //init voice commands
        String[] voiceCommands = ResourceModel.getInstance().voiceCommands;
        WordActivatorAPI.getInstance().init(voiceCommands, this);
        
        //init stomper
        if(stomper==null) {
        	stomper = new StompDetector(this);
        	stomper.addStompListener(new InstructionStomp(this));
        }
        stomper.setMainActivity(this);
        
        //init Midi Server
        MidiServer.getInstance().clearChordRecognitionListeners();
        MidiServer.getInstance().addChordRecognitionListener(this);
        
        //init Audio Icon
        AudioIconAPI.getInstance().init(this);
        
        //set up application volume (based on preferences
        dynamicVolumeAdjustment();
                        
	}
	
	public void dynamicVolumeAdjustment() {
		if(PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).getBoolean("adjust_vol_pref", false)) {		
			AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
			boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();			
			if(!isExploreByTouchEnabled) {
		        VolumeAPI.getInstance().init(this);
		        VolumeAPI.getInstance().setVolume(VolumeAPI.DEFAULT_VOLUME_FACTOR);
			}
		}
	}
			
	@Override
	public void onStop() {
		
		//call on stop functions
		super.onStop();		
		WordActivatorAPI.getInstance().onStop();
		stomper.onStop();
		MidiServer.getInstance().onStop();
		
		//clean up and save
		PlaybackEngineAPI.cleanUp(FileOpAPI.SAVE_PATH);
		DataModel.getInstance().saveInstance();
	}
	
	@Override
	public void onResume() {
		
		//call on resume functions (if not already running)
		super.onResume();	
		WordActivatorAPI.getInstance().onResume();
		stomper.onResume(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("enable_stomp_mode_pref", false));
		MidiServer.getInstance().onResume(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("enable_midi_following_pref", false));
		if(MidiServer.getInstance().isRunning()) {
			updateGUIForNextAvailableIndex();
		}
		
		//reinit GUI from file (if exists)
        refreshGUI();		
        
        //garbage collect
        System.gc();
	}
		
	/**
	 * Refresh GUI based on current data model (either from file or in memory).
	 */
	public void refreshGUI() {
		DataModel dataModel = DataModel.getInstance();
		int prevInstSel = dataModel.getSelectedInstructionIndex();
		if(dataModel.getFileName()!=null && !dataModel.getFileName().trim().equals("")) {
			this.setTitle(dataModel.getFileName().trim());
		}
		if(dataModel.getSong()!=null && dataModel.getTracksList()!=null) {
			populateTrackOptions(dataModel.getTracksList(),dataModel.getTrackNum());
		}
		if(dataModel.getCurrentSegment()!=-1 && dataModel.getInstSegments()!=null) {
			
			//populate instructions
			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());			
			if(dataModel.isVerbose()) {
				populateInstructionPane(c_seg.getSfInst());					
			}
			else {
				populateInstructionPane(c_seg.getChordInst());
			}
			
			//re-enable highlighting and instruction selected
			instructionsList.setHilightEnabled(true);
			if(prevInstSel!=-1) {
				dataModel.setSelectedInstructionIndex(prevInstSel);
			}
			
			//set title
			this.setTitle(dataModel.getFileName().trim() + " " + c_seg.getTitlePresentation());			
		}
		
		//refresh instructions list
		instructionsList.refreshGUI();
	}
	
	public void updateOnFileLoad(TGSong song, File file) {
		
		//update data model basic prams
		DataModel dataModel = DataModel.getInstance();
	    dataModel.setFileName(song.getName());	            	 
    	dataModel.setSong(song);
    	dataModel.setPlaybackTrackInds(null);
    	
	    //create tracks
	   	createTrackOptions();
   	 
    	//set first segment selected and load instructions
    	if(dataModel.getTracksList().size() >0) {
    		
    		//set params
    		dataModel.setTrackNum(0);
    		dataModel.setCurrentSegment(0);
    		
        	//enable instructions list
        	getInstructionsList().setHilightEnabled(true);
    		
        	//perform load and show on GUI
			loadInstructions();				    	
			DataModel.getInstance().clearSelectedInstructionIndex();
			getInstructionsList().refreshGUI();	         			
    	}	            	 
    		            	 
   	 	//notify user that track successfully loaded
   	 	TextToSpeechAPI.speak(ResourceModel.getInstance().FILE_LOADED_SPEECH);
	}

	@Override
	public void onClick(View v) {
		
		//stop media player
		MediaPlayerAPI.getInstance().stop();
		
		//handle button press
		if(v.getId()==loadTabFileButton.getId()) {
			showLoadFileDialog();
		}
		else if(v.getId()==toggleModesButton.getId()) {
			toggleModes();
		}
		else if(v.getId()==playSampleButton.getId()) {
			playSample();
		}		
		else if(v.getId()==prevMeasButton.getId()) {
			prevMeasure();
		}
		else if(v.getId()==nextMeasButton.getId()) {
			nextMeasure();
		}
		else if(v.getId()==upButton.getId()) {
			prevInstruction();
		}		
		else if(v.getId()==downButton.getId()) {
			nextInstruction();
		}				
	}
	
	public void prevInstruction() {
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null &&
		dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && 
		dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 &&
		dataModel.getCurrentSegment()>=0 &&
		dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst().size()>0) {
			
			//update index and perform click
      	  	int selectedInstructionIndex = DataModel.getInstance().getSelectedInstructionIndex();			
			if(selectedInstructionIndex >= 0) {
				
				//decrement instruction index
				selectedInstructionIndex--;
				
				//perform click on GUI
				instructionsList.programmaticSelect(selectedInstructionIndex);
				
				//find and read instruction
				if(selectedInstructionIndex >= 0) {
					String c_inst = null;
					Segment cSeg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());				
					if(dataModel.isVerbose()) {
						c_inst = cSeg.getSfInst().get(selectedInstructionIndex);
					}
					else {
						c_inst = cSeg.getChordInst().get(selectedInstructionIndex);					
					}
					if(c_inst!=null) {
						TextToSpeechAPI.speak(
								InstructionContentDescription.makeAccessibleInstruction(c_inst));
					}
				}
			}
			else {
				
				//no previous instruction
	    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_PREV_INST);				
			}			
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {

    		//no data in section
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}				
	}
	
	public void nextInstruction() {
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null &&
		dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && 
		dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 &&
		dataModel.getCurrentSegment()>=0) {
			
			//update index and perform click
			Segment cSeg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
			int numInst = cSeg.getChordInst().size();
      	  	int selectedInstructionIndex = DataModel.getInstance().getSelectedInstructionIndex();			
			if(selectedInstructionIndex < (numInst-1)) {
				
				//increment instruction
				selectedInstructionIndex++;
				
				//perform click on GUI
				instructionsList.programmaticSelect(selectedInstructionIndex);
				
				//find and read instruction
				String c_inst = null;
				if(dataModel.isVerbose()) {
					c_inst = cSeg.getSfInst().get(selectedInstructionIndex);
				}
				else {
					c_inst = cSeg.getChordInst().get(selectedInstructionIndex);					
				}
				if(c_inst!=null) {
					TextToSpeechAPI.speak(
							InstructionContentDescription.makeAccessibleInstruction(c_inst));
				}
				
			}
			else {
				//no next instruction
	    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_NEXT_INST);				
			}			
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		//no data in section
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}				
	}
	
	public void playSample() {	
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0) {
			Segment cSeg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
			cSeg.play();
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}		
	}
	
	public void toggleModes() {
		DataModel dataModel=  DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0) {		
			if(!dataModel.isOnPercussionTrack()) {
				if(dataModel.isVerbose()) {
					
					//populate instructions
					populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());
					
					//flip stored flag
					dataModel.setVerbose(false);										
					
					//read currently selected instruction
					if(dataModel.getSelectedInstructionIndex() >= 0) {
						instructionsList.programmaticSelect(dataModel.getSelectedInstructionIndex());
						List<String> inst = dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst();
						String instr = inst.get(dataModel.getSelectedInstructionIndex());
						TextToSpeechAPI.speak(
								InstructionContentDescription.makeAccessibleInstruction(instr));
					}
										
				}
				else {
					
					//populate instructions
					populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());

					//flip stored flag
					dataModel.setVerbose(true);
					
					//read currently selected instruction
					if(dataModel.getSelectedInstructionIndex() >= 0) {
						instructionsList.programmaticSelect(dataModel.getSelectedInstructionIndex());						
						List<String> inst = dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst();
						String instr = inst.get(dataModel.getSelectedInstructionIndex());
						TextToSpeechAPI.speak(
								InstructionContentDescription.makeAccessibleInstruction(instr));
					}					
				}
			}
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}		
	}
	
	public void nextMeasure() {
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getCurrentSegment() < (dataModel.getInstSegments().size()-1)) {
			dataModel.setCurrentSegment(dataModel.getCurrentSegment()+1);
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());
			}
			else {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());				
			}
			DataModel.getInstance().clearSelectedInstructionIndex();
			instructionsList.refreshGUI();
			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());				
			this.setTitle(dataModel.getFileName().trim() + " " + c_seg.getTitlePresentation());			
		}
		else if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getCurrentSegment() == (dataModel.getInstSegments().size()-1)) {
			TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_LAST_SECTION);
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}		
	}
	
	public void prevMeasure() {
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 && dataModel.getCurrentSegment() > 0) {
			dataModel.setCurrentSegment(dataModel.getCurrentSegment()-1);
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());				
			}
			else {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());								
			}
			DataModel.getInstance().clearSelectedInstructionIndex();
			instructionsList.refreshGUI();			
			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());							
			this.setTitle(dataModel.getFileName().trim() + " " + c_seg.getTitlePresentation());			
		}
		else if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getCurrentSegment() == 0) {
			TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_FIRST_SECTION);
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}
	}
	
	public void loadInstructions() {
		
		//get model
		DataModel dataModel = DataModel.getInstance();
		
		//generate instructions
		dataModel.genInstructions();
		
		//populate instructions pane with current measure
		if(dataModel.getCurrentSegment() < dataModel.getInstSegments().size()) {
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());				
			}
			else {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());								
			}
		}
		
		//bug fix -- if out of bounds, reset back to start
		if(dataModel.getCurrentSegment() >= dataModel.getInstSegments().size()) {
			dataModel.setCurrentSegment(0);
		}
		
		//display
		Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());							
		this.setTitle(dataModel.getFileName().trim() + " " + c_seg.getTitlePresentation());					
	}
	
	public void populateInstructionPane(List<String> instructions) {
    	ArrayAdapter<String> a_opts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,instructions);
    	instructionsList.setAdapter(a_opts);
 	}
	
	public void showLoadFileDialog() {
		if(PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).getBoolean("enable_google_drive_pref", false)) {
			FileSelectionMethodDialog dialog = new FileSelectionMethodDialog(this);
			dialog.show(getFragmentManager(), FRAGMENT_MANAGER_TAG);
		}
		else {		
			GuitarFileLoaderDialog dialog = new GuitarFileLoaderDialog(this,this);
			dialog.show();
		}
	}
		
	public void populateTrackOptions(List<String> tracksList, int start_sel_position) {
    	ArrayAdapter<String> a_opts = new ArrayAdapter<String>(this, R.layout.my_spinner,tracksList);
    	trackChooser.setAdapter(a_opts);
    	trackChooser.setSelection(start_sel_position);
    	trackChooser.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				DataModel.getInstance().setTrackNum(arg2);
				loadInstructions();				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
    		
    	});
	}
		
    public void createTrackOptions() {
    	
    	//load data model
    	DataModel dataModel = DataModel.getInstance();
    	
        //populate options in list. Avoid duplicates.
        ArrayList<String> tracksList = new ArrayList<String>();
        Map<String,Integer> tracksDD = new HashMap<String,Integer>();
        Set<String> multipleEntries = new HashSet<String>();
        TGSong songLoaded = dataModel.getSong();
        if(songLoaded!=null && songLoaded.countTracks() > 0) {
        	for(int x=0; x < songLoaded.countTracks(); x++) {
        		TGTrack track = songLoaded.getTrack(x);
        		int offset = track.getOffset();
        		String capoStr = "";
        		if(offset!=0) {
        			String CAPO = ResourceModel.getInstance().CAPO;
        			capoStr = " ["+CAPO+" "+offset+"]";
        		}
        		String trackHash = track.getName().trim().toLowerCase() + capoStr;
        		if(tracksDD.containsKey(trackHash)) {
        			int newCnt = tracksDD.get(trackHash) + 1;
        			tracksDD.put(trackHash, newCnt);
        			multipleEntries.add(trackHash);
        		}
        		else {
        			tracksDD.put(trackHash, 1);
        		}
        	}
        	for(int x=(songLoaded.countTracks()-1); x>=0; x--) {
        		TGTrack track = songLoaded.getTrack(x);
        		int offset = track.getOffset();
        		String capoStr = "";
        		if(offset!=0) {
        			String CAPO = ResourceModel.getInstance().CAPO;
        			capoStr = " ["+CAPO+" "+offset+"]";
        		}
        		String trackHash = track.getName().trim().toLowerCase() + capoStr;
        		String trackName = track.getName().trim() + capoStr;
        		if(multipleEntries.contains(trackHash)) {
        			tracksList.add(0,trackName + " (" + tracksDD.get(trackHash) + ")");
        			tracksDD.put(trackHash, tracksDD.get(trackHash)-1);
        		}
        		else {
        			tracksList.add(0,trackName);        			
        		}
        	}
        }
        
        //populate in GUI
        populateTrackOptions(tracksList,0);
        
    	//store
    	DataModel.getInstance().setTracksList(tracksList);
    }	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
		this.getActionBar().setDisplayShowHomeEnabled(false);
        return true;
    }   
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//stop media player
    	MediaPlayerAPI.getInstance().stop();
    	
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.GeneralSettingsMenuItem:
        		showGeneralSettings();
        		return true;
        	case R.id.HandsFreeMenuItem:
        		showHandsFreeSettings();
        		return true;
        	case R.id.InstrSettingsMenuItem:
        		showInstructionSettings();
        		return true;
        	case R.id.PlayBackOptionsMenuItem:
        		showPlaybackOptions();
        		return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void showGeneralSettings() {
    	
    	//start new activity
		Intent i = new Intent(this, GeneralSettingsPrefActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);												
    	
    }
    
    public void showHandsFreeSettings() {

    	//start new activity
		Intent i = new Intent(this, HandsFreeSettingsPrefActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);												
    	
    }

    public void showInstructionSettings() {

    	//start new activity
		Intent i = new Intent(this, InstructionSettingsPrefActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);												
    	
    }
    
    public void showPlaybackOptions() {

    	//start new activity
		Intent i = new Intent(this, PlaybackOptionsPrefActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);												
    	
    }
                            
    public void playAudioIcon() {
    	DataModel dataModel = DataModel.getInstance();
    	if(dataModel.getSong()!=null && 
    			dataModel.getInstSegments()!=null && dataModel.getTrackNum()!=-1 && 
    			dataModel.getCurrentSegment()!=-1 && dataModel.getInstSegments()!=null
    			&& dataModel.getSelectedInstructionIndex()!=-1) {
    			
    			//get beat and play (if not on percussion track)
    			if(!dataModel.isOnPercussionTrack()) {
	    			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
	    			List<TGBeat> beats = c_seg.getBeats();
	    			TGBeat beat = beats.get(dataModel.getSelectedInstructionIndex());
	    			if(beat!=null) {
	    				AudioIconAPI.getInstance().playBeatAudioIcon(beat);
	    			}
    			}
    			
    	}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_DATA);
    	}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()!=0 &&
    			dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && dataModel.getSelectedInstructionIndex()==-1) {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_INST_SELECTED);
    	}    	
    	else {
    		TextToSpeechAPI.speak(ResourceModel.getInstance().ERROR_NO_FILE_LOADED);
    	}
    	
    }
    
    /**
     * Voice Activator callback
     */
	@Override
	public void activated(boolean success, String wordHeard) {
		
		//stop media player
		MediaPlayerAPI.getInstance().stop();
		
		//handle activation
		Log.d("ACTIVATED", wordHeard);
		String[] VOICE_COMMANDS = ResourceModel.getInstance().voiceCommands;
		String[] DIRECT_MATCH = ResourceModel.getInstance().VOICE_ACTIONS_DIRECT_MATCH;
		if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[0])) {
			this.toggleModesButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[1])) {
			this.playSampleButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[2])) {
			this.nextMeasButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[3])) {
			this.prevMeasButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[4])) {
			this.upButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[5])) {
			this.downButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[6])) {
			playAudioIcon();
		}		
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[7])) {
			playNext_voiceCommand();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[8])) {
			playPrevious_voiceCommand();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[9])) {
			stompOn_voiceCommand();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[10])) {
			stompOff_voiceCommand();
		}
		else if(wordHeard.equalsIgnoreCase(VOICE_COMMANDS[11])) {
			voiceOff_voiceCommand();
		}
		else {
			
			//if metronome actions enabled, use them.
			if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("enable_voice_activated_metronome_pref", false)) {
				if(wordHeard.equalsIgnoreCase(DIRECT_MATCH[0]) || wordHeard.equalsIgnoreCase(DIRECT_MATCH[1])){
					MetronomeAPI.getInstance().stop();			
				}
				else if(wordHeard.equalsIgnoreCase(DIRECT_MATCH[2])) {
					MetronomeAPI.getInstance().stop();						
					MetronomeAPI.getInstance().setBeats((short) 4);
				}
				else if(wordHeard.equalsIgnoreCase(DIRECT_MATCH[3])) {
					MetronomeAPI.getInstance().stop();			
					MetronomeAPI.getInstance().setBeats((short) 3);
				}		
				else if(wordHeard.equalsIgnoreCase(DIRECT_MATCH[4])) {
					MetronomeAPI.getInstance().stop();						
					MetronomeAPI.getInstance().setBeats((short) 2);
				}
				else if(wordHeard.toLowerCase().indexOf("metronome") > -1) {
					try {
						String[] toks = wordHeard.split(" ");
						int bpm = Integer.parseInt(toks[1]);
						MetronomeAPI.getInstance().stop();
						MetronomeAPI.getInstance().setBpm((short)bpm);
						MetronomeAPI.getInstance().start();
					}
					catch(Exception e) {}
				}
			}
		}
	}
	
	public void playNext_voiceCommand() {
		int cSeg = DataModel.getInstance().getCurrentSegment();
		nextMeasure();
		int nSeg = DataModel.getInstance().getCurrentSegment();
		if(cSeg!=nSeg) {
			playSample();
		}
	}
	
	public void playPrevious_voiceCommand() {
		int cSeg = DataModel.getInstance().getCurrentSegment();
		prevMeasure();
		int nSeg = DataModel.getInstance().getCurrentSegment();
		if(cSeg!=nSeg) {
			playSample();
		}
	}
	
	public void stompOn_voiceCommand() {
		stomper.start();
		TextToSpeechAPI.speak(ResourceModel.getInstance().STOMP_ON_NOTIF);
	}
	
	public void stompOff_voiceCommand() {
		stomper.stop();
		TextToSpeechAPI.speak(ResourceModel.getInstance().STOMP_OFF_NOTIF);		
	}
	
	public void voiceOff_voiceCommand() {
			
		//turn off
  	    WordActivatorAPI.getInstance().stopListening();
  	
		//update data model
 	    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
 	    Editor e =sharedPrefs.edit();
 	    e.putBoolean("enable_voice_actions_pref", true);
 	    e.commit();
  	    
  	    //notify user
  	    TextToSpeechAPI.speak(ResourceModel.getInstance().VOICE_OFF_NOTIF);
	}
	
	/**
	 * @return the instructionsList
	 */
	public AccListView getInstructionsList() {
		return instructionsList;
	}
	
	@Override
	public void chordRecognized(final String chord) {    
				
		//get chord hash
		final String chordHash = ChordRecognizer.getChordHash(chord);
		
		//match
		final DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && 
			dataModel.getInstSegments()!=null && dataModel.getTrackNum()!=-1 && 
			dataModel.getCurrentSegment()!=-1 && dataModel.getInstSegments()!=null
			&& dataModel.getSelectedInstructionIndex()!=-1) {
			
			//see if chord hash matches target
			final Segment seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
			final String target = seg.getMatchTargets().get(dataModel.getSelectedInstructionIndex());
			if(target.equals(chordHash) || ChordRecognizer.robustMidiMatch(chordHash, target)) {
	       
				//play success track
				if(MIDI_FOLLOWER_DEBUG) {
					this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "Success: " + target, Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				//update gui for next available index
				this.updateGUIForNextAvailableIndex();
				
			}
			else {
				
				//play buzzer
				if(MIDI_FOLLOWER_DEBUG) {
					this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "Failure: " + chordHash + " ::: " + target, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}    
	}
	
	public boolean updateToNextAvailableIndex() {
		
		//get data model
		DataModel dataModel = DataModel.getInstance();		

		//increment to next available one or say end of track if not anymore.
		int initSeg = dataModel.getCurrentSegment();
		int segCtr = dataModel.getCurrentSegment();
		int instCtr = dataModel.getSelectedInstructionIndex()+1;
		outer:while(segCtr < dataModel.getInstSegments().size()) {
			if(segCtr >= 0) {
				Segment seg = dataModel.getInstSegments().get(segCtr);			
				while(instCtr < seg.getMatchTargets().size()) {
					if(instCtr >= 0) {
						String newTarget = seg.getMatchTargets().get(instCtr);
						if(!newTarget.equals("")) {
							dataModel.setCurrentSegment(segCtr);
							dataModel.setSelectedInstructionIndex(instCtr);
							break outer;
						}
					}
					instCtr++;					
				}
			}
			segCtr++;
			instCtr=0;
		}
		
		//if chose end of track, just set to last instruction.
		if(segCtr==dataModel.getInstSegments().size()) {
			dataModel.setCurrentSegment(segCtr-1);
			dataModel.setSelectedInstructionIndex(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst().size()-1);
		}
		
		return (initSeg!=segCtr);
	}
	
	public void updateGUIForNextAvailableIndex() {

		//update to next index
		final boolean segChanged = updateToNextAvailableIndex();

		//refresh gui
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				//update gui and perform programmatic selection
				int temp = DataModel.getInstance().getSelectedInstructionIndex();
				if(segChanged) {
					DataModel.getInstance().clearSelectedInstructionIndex();
					MainActivity.this.refreshGUI();				
				}
				MainActivity.this.instructionsList.programmaticSelect(temp);

				//find and read instruction using TTS
				String c_inst = null;
				final DataModel dataModel = DataModel.getInstance();	
				final Segment seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());				
				if(dataModel.isVerbose()) {
					c_inst = seg.getSfInst().get(DataModel.getInstance().getSelectedInstructionIndex());
				}
				else {
					c_inst = seg.getChordInst().get(DataModel.getInstance().getSelectedInstructionIndex());          
				}
				if(c_inst!=null) {
					TextToSpeechAPI.speak(
							InstructionContentDescription.makeAccessibleInstruction(c_inst));
				}
			}
		});
	}
	
	/**
	 * @return the downButton
	 */
	public Button getDownButton() {
		return downButton;
	}	
}