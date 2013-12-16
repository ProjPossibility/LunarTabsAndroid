package com.PP.LunarTabsAndroid.Activities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.PP.AudioIcon.AudioIconAPI;
import com.PP.IntelliSeg.Abstract.Segment;
import com.PP.IntelliSeg.MeasureIncrementSegmenter.MeasureIncrementSegmenter;
import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.APIs.WordActivatorAPI;
import com.PP.LunarTabsAndroid.Dialogs.GuitarFileLoaderDialog;
import com.PP.LunarTabsAndroid.Dialogs.MeasureIncrementDialog;
import com.PP.LunarTabsAndroid.Dialogs.MidiFollowingEnableDialog;
import com.PP.LunarTabsAndroid.Dialogs.SelectSectionDialog;
import com.PP.LunarTabsAndroid.Dialogs.StomperEnableDialog;
import com.PP.LunarTabsAndroid.Dialogs.VoiceActionsDialog;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordDB;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordRecognizer;
import com.PP.LunarTabsAndroid.UI.AccListView;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.InstructionContentDescription;
import com.PP.LunarTabsAndroid.UI.SpeechConst;
import com.PP.LunarTabsAndroid.UI.StomperParams;
import com.PP.MidiServer.AbstractMidiServerActivity;
import com.PP.MidiServer.ChordRecognitionListener;
import com.PP.MidiServer.MidiServer;
import com.PP.StompDetector.InstructionStomp;
import com.PP.StompDetector.StompDetector;
import com.example.lunartabsandroid.R;
import com.root.gast.speech.activation.SpeechActivationListener;
import com.tuxguitar.song.models.TGBeat;
import com.tuxguitar.song.models.TGSong;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class MainActivity extends AbstractMidiServerActivity implements OnClickListener, SpeechActivationListener, ChordRecognitionListener  {
	
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
	protected StompDetector stomper;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//init stuff
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_main);
	    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	
	    
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
        loadTabFileButton.setOnClickListener(this);
        toggleModesButton.setOnClickListener(this);
        playSampleButton.setOnClickListener(this);
        prevMeasButton.setOnClickListener(this);
        nextMeasButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);
        
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
        DataModel.getInstance().setSegmenter(new MeasureIncrementSegmenter());
//        GUIDataModel.getInstance().setSegmenter(new SMRSegmenter());
        
        //enable APIs
        TextToSpeechAPI.init(this);
        
        //init data directory
        FileOpAPI.init();
        TuxGuitarUtil.cleanUp(FileOpAPI.SAVE_PATH);
        
        //Chord DB initialize
        ChordDB.getInstance();
//        ChordDBGenerator.getInstance().debugDump();
        
        //init voice commands
        WordActivatorAPI.getInstance().init(SpeechConst.voiceCommands, this);
        
        //init Stomp detector
        stomper = new StompDetector(this);
        stomper.addStompListening(new InstructionStomp(this));
        
        //init Midi Server
        MidiServer.getInstance().addChordRecognitionListener(this);
        
        //init Audio Icon
        AudioIconAPI.getInstance().init(this);
        
//        stomper = new MetronomeStomp(this);
//        stomper.setUntrigger_delay(0);
//        stomper.start();
	}
		
	@Override
	public void onStop() {
		
		//call on stop functions
		super.onStop();		
		WordActivatorAPI.getInstance().onStop();
		stomper.onStop();
		MidiServer.getInstance().onStop();
		
		//clean up and save
		TuxGuitarUtil.cleanUp(FileOpAPI.SAVE_PATH);
		DataModel.getInstance().saveInstance();
		StomperParams.getInstance().saveInstance();
	}
	
	@Override
	public void onResume() {
		
		//call on resume functions
		super.onResume();		
		WordActivatorAPI.getInstance().onResume();		
		stomper.onResume();
		MidiServer.getInstance().onResume();
		
		//reinit GUI from file (if exists)
        refreshGUI();		
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

	@Override
	public void onClick(View v) {
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
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null &&
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
	    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_PREV_INST);				
			}			
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		//no data in section
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}				
	}
	
	public void nextInstruction() {
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null &&
		dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && 
		dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 &&
		dataModel.getCurrentSegment()>=0 &&
		dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst().size()>0) {
			
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
	    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_NEXT_INST);				
			}			
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		//no data in section
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}				
	}
	
	public void playSample() {	
		DataModel dataModel = DataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null && dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0) {
			Segment cSeg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
			cSeg.play();
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}		
	}
	
	public void toggleModes() {
		DataModel dataModel=  DataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null && dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0) {		
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
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
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
			TextToSpeechAPI.speak(SpeechConst.ERROR_LAST_SECTION);
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
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
			TextToSpeechAPI.speak(SpeechConst.ERROR_FIRST_SECTION);
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
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
		GuitarFileLoaderDialog dialog = new GuitarFileLoaderDialog(this,this);
		dialog.show();
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
        		String trackHash = songLoaded.getTrack(x).getName().trim().toLowerCase();
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
        		String trackHash = songLoaded.getTrack(x).getName().trim().toLowerCase();
        		String trackName = songLoaded.getTrack(x).getName().trim();
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
        return true;
    }   
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
		//set title of menu buttons
		if(stomper.isEnabled()) {
			String new_title = getResources().getString(R.string.DisableStompMode);
			MenuItem stompModeMenuItem = menu.findItem(R.id.StompModeMenuItem);
			if(stompModeMenuItem!=null) {
				stompModeMenuItem.setTitle(new_title);						
			}
		}
		else {
			String new_title = getResources().getString(R.string.EnableStompMode);
			MenuItem stompModeMenuItem = menu.findItem(R.id.StompModeMenuItem);
			if(stompModeMenuItem!=null) {
				stompModeMenuItem.setTitle(new_title);						
			}
		}
    	if(DataModel.getInstance().isVoiceActionsEnabled()) {
			String new_title = getResources().getString(R.string.DisableVoiceActions);
			MenuItem voiceActionsMenuItem = menu.findItem(R.id.VoiceActionsMenuItem);
			if(voiceActionsMenuItem!=null) {
				voiceActionsMenuItem.setTitle(new_title);										
			}
    	}
    	else {
			String new_title = getResources().getString(R.string.EnableVoiceActions);
			MenuItem voiceActionsMenuItem = menu.findItem(R.id.VoiceActionsMenuItem);			
			if(voiceActionsMenuItem!=null) {
				voiceActionsMenuItem.setTitle(new_title);										
			}
    	}    	
    	if(MidiServer.getInstance().isRunning()) {
			String new_title = getResources().getString(R.string.DisableMidiFollowing);
			MenuItem voiceActionsMenuItem = menu.findItem(R.id.MidiFollowingMenuItem);
			if(voiceActionsMenuItem!=null) {
				voiceActionsMenuItem.setTitle(new_title);										
			}
    	}
    	else {
			String new_title = getResources().getString(R.string.EnableMidiFollowing);
			MenuItem voiceActionsMenuItem = menu.findItem(R.id.MidiFollowingMenuItem);
			if(voiceActionsMenuItem!=null) {
				voiceActionsMenuItem.setTitle(new_title);										
			}
    	}    	    	
        return true;    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.SecIncMenuItem:
            	showSelectIncDialog();
                return true;
            case R.id.GoToMenuItem:
            	showSelectSectionDialog();
                return true;
            case R.id.StompModeMenuItem:
            	stompModeDialog(item);
            	return true;
            case R.id.VoiceActionsMenuItem:
            	voiceActionsDialog(item);
            	return true;
            case R.id.CalibStompModeMenuItem:
            	calibrateStompMode();
            	return true;
            case R.id.MidiFollowingMenuItem:
            	midiFollowingDialog(item);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void midiFollowingDialog(MenuItem item) {
    	
    	//tab file must be loaded for stomper
    	DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
    	
	    	//enable stomper if not active
	    	if(!MidiServer.getInstance().isRunning()) {
	    		
				final Dialog dialog = new MidiFollowingEnableDialog(this);
				dialog.show();	    		    		
				
				//change text on menu item
				String new_title = getResources().getString(R.string.DisableMidiFollowing);
				item.setTitle(new_title);				
				
	    	}	    	
	    	else if(MidiServer.getInstance().isRunning()) {
	    		
	    		//stop stomper
	    		MidiServer.getInstance().stop();
	    		
	    		//change text on menu item
				String new_title = getResources().getString(R.string.EnableMidiFollowing);
				item.setTitle(new_title);
	    	}
	    	
		}
		else {
			TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
		} 
	}
    
    public void calibrateStompMode() {
    	
    	//stop stomper and voice actions
    	stomper.onStop();
    	WordActivatorAPI.getInstance().onStop();
    	
    	//start new activity
		Intent i = new Intent(this, StomperCalibActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);												
    }
    
    public void voiceActionsDialog(MenuItem menuItem) {
    	if(!DataModel.getInstance().isVoiceActionsEnabled()) {
    		
    		//show dialog for voice actions
        	VoiceActionsDialog m = new VoiceActionsDialog(menuItem);
        	m.show(getFragmentManager(), "LOZ");   		
        	
    	}
    	else {
    		
    		//stop voice actions
     	   DataModel.getInstance().setVoiceActionsEnabled(false);
     	   WordActivatorAPI.getInstance().stopListening();
    		
    		//relabel menu item
     	   String new_title = getResources().getString(R.string.EnableVoiceActions);
     	   menuItem.setTitle(new_title);
    		
    	}
    }
    
    public void stompModeDialog(MenuItem item) {
    	
    	//tab file must be loaded for stomper
    	DataModel dataModel = DataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
    	
	    	//enable stomper if not active
	    	if(!stomper.isEnabled()) {
	    		
	    		//show stomper enabled dialog
				final Dialog dialog = new StomperEnableDialog(this,stomper);
				dialog.show();	    		    		
				
				//change text on menu item
				String new_title = getResources().getString(R.string.DisableStompMode);
				item.setTitle(new_title);				
				
	    	}	    	
	    	else if(stomper.isEnabled()) {
	    		
	    		//stop stomper
	    		stomper.stop();
	    		
	    		//change text on menu item
				String new_title = getResources().getString(R.string.EnableStompMode);
				item.setTitle(new_title);					    		
	    	}
	    	
		}
		else {
			TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
		}
    }
    
    public void showSelectIncDialog() {
    	DataModel dataModel = DataModel.getInstance();    	
    	if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getTrackNum()!=-1 && dataModel.getCurrentSegment()!=-1 && dataModel.getInstSegments()!=null) {    	
    		MeasureIncrementDialog m = new MeasureIncrementDialog(this);
    		m.show(getFragmentManager(), "LOZ");    	
    	}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}
    }
    
    public void showSelectSectionDialog() {
    	DataModel dataModel = DataModel.getInstance();
    	if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getTrackNum()!=-1 && dataModel.getCurrentSegment()!=-1 && dataModel.getInstSegments()!=null) {
        	SelectSectionDialog m = new SelectSectionDialog(this);
        	m.show(getFragmentManager(), "LOZ");    		
    	}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}
    }
    
    public void playAudioIcon() {
    	DataModel dataModel = DataModel.getInstance();
    	if(dataModel.getSong()!=null && 
    			dataModel.getInstSegments()!=null && dataModel.getTrackNum()!=-1 && 
    			dataModel.getCurrentSegment()!=-1 && dataModel.getInstSegments()!=null
    			&& dataModel.getSelectedInstructionIndex()!=-1) {
    			
    			//get beat and play
    			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
    			List<TGBeat> beats = c_seg.getBeats();
    			TGBeat beat = beats.get(dataModel.getSelectedInstructionIndex());
    			AudioIconAPI.getInstance().playBeatAudioIcon(beat);
    			
    	}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()!=0 &&
    			dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && dataModel.getSelectedInstructionIndex()==-1) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_INST_SELECTED);
    	}    	
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}
    	
    }
    
    /**
     * Voice Activator callback
     */
	@Override
	public void activated(boolean success, String wordHeard) {
		Log.d("ACTIVATED", wordHeard);
		if(wordHeard.equalsIgnoreCase("toggle")) {
			this.toggleModesButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase("play")) {
			this.playSampleButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase("next")) {
			this.nextMeasButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase("back")) {
			this.prevMeasButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase("up")) {
			this.upButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase("down")) {
			this.downButton.performClick();
		}
		else if(wordHeard.equalsIgnoreCase("sample")) {
			playAudioIcon();
		}
	}

	/**
	 * @return the instructionsList
	 */
	public AccListView getInstructionsList() {
		return instructionsList;
	}
	
	@Override
	public void chordRecognized(final String chord) {    
		
		/*
		this.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MainActivity.this, "CHORD: " + chord, Toast.LENGTH_SHORT).show();
			}
		});
			*/
		
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
			if(target.equals(chordHash)) {
	       
				//play success track
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainActivity.this, "Success: " + target, Toast.LENGTH_SHORT).show();
					}
				});
				
				//update to next index
				updateToNextAvailableIndex();
	 
				//refresh gui
				this.runOnUiThread(new Runnable() {
					public void run() {
						
						//update gui and perform programmatic selection
						MainActivity.this.refreshGUI();
						MainActivity.this.getInstructionsList().programmaticSelect(DataModel.getInstance().getSelectedInstructionIndex());

						//find and read instruction using tts
						String c_inst = null;
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
			else {
				
				//play buzzer
				this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainActivity.this, "Failure: " + chordHash + " ::: " + target, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}    
	}
	
	public void updateToNextAvailableIndex() {
		
		//get data model
		DataModel dataModel = DataModel.getInstance();		

		//increment to next available one or say end of track if not anymore.
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
	}
	
}