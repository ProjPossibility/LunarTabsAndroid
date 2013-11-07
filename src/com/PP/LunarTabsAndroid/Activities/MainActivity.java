package com.PP.LunarTabsAndroid.Activities;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.LunarTabsAndroid.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.APIs.TuxGuitarUtil;
import com.PP.LunarTabsAndroid.Dialogs.MeasureIncrementDialog;
import com.PP.LunarTabsAndroid.Dialogs.SelectSectionDialog;
import com.PP.LunarTabsAndroid.InstrumentModels.ChordDB;
import com.PP.LunarTabsAndroid.IntelliSeg.Abstract.Segment;
import com.PP.LunarTabsAndroid.IntelliSeg.MeasureIncrementSegmenter.MeasureIncrementSegmenter;
import com.PP.LunarTabsAndroid.Metronome.Metronome;
import com.PP.LunarTabsAndroid.StompDetector.StompDetector;
import com.PP.LunarTabsAndroid.StompDetector.StompDetectorImpl;
import com.PP.LunarTabsAndroid.UI.AccListView;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;
import com.PP.LunarTabsAndroid.UI.SpeechConst;
import com.daidalos.afiledialog.FileChooserDialog;
import com.example.lunartabsandroid.R;
import com.tuxguitar.song.models.TGSong;

import android.graphics.Color;
import android.location.Address;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class MainActivity extends Activity implements OnClickListener {
	
	//components
	protected Button loadTabFileButton;
	protected Button toggleModesButton;
	protected Button playSampleButton;
	protected Button prevMeasButton;
	protected Button nextMeasButton;
	protected Button upButton;
	protected Button downButton;
//	protected TextView fileField;
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
        GUIDataModel.getInstance().setSegmenter(new MeasureIncrementSegmenter());
        
        //enable APIs
        TextToSpeechAPI.init(this);
        
        //clean up
        TuxGuitarUtil.cleanUp(FileOpAPI.SAVE_PATH);
        
        //Chord DB initialize
        ChordDB.getInstance();
//        ChordDBGenerator.getInstance().debugDump();
        
        //Stomp detector
//        stomper = new StompDetectorImpl(this);
        
//        Metronome m = new Metronome();
 //       m.play();
        
	}
		
	@Override
	public void onStop() {
		super.onStop();
		TuxGuitarUtil.cleanUp(FileOpAPI.SAVE_PATH);
	}
	
	@Override
	public void onResume() {
		super.onResume();
        //reinit GUI from file (if exists)
        refreshGUI();		
	}
		
	/**
	 * Refresh GUI based on current data model (either from file or in memory).
	 */
	public void refreshGUI() {
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getFileName()!=null && !dataModel.getFileName().trim().equals("")) {
			this.setTitle(dataModel.getFileName().trim());
		}
		if(dataModel.getSong()!=null) {
			populateTrackOptions();
		}
		if(dataModel.getSong()!=null && dataModel.getTrackNum()!=-1) {
			trackChooser.setSelection(dataModel.getTrackNum());
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
			
			//set title
			this.setTitle(dataModel.getFileName().trim() + " (M" + (c_seg.getStart()+1) + "-" + (c_seg.getEnd()+1) + ")");			
						
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
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null &&
		dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && 
		dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 &&
		dataModel.getCurrentSegment()>=0 &&
		dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst().size()>0) {
			
			//update index and perform click
      	  	int selectedInstructionIndex = GUIDataModel.getInstance().getSelectedInstructionIndex();			
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
						TextToSpeechAPI.speak(c_inst);
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
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null &&
		dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && 
		dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 &&
		dataModel.getCurrentSegment()>=0 &&
		dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst().size()>0) {
			
			//update index and perform click
			Segment cSeg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
			int numInst = cSeg.getChordInst().size();
      	  	int selectedInstructionIndex = GUIDataModel.getInstance().getSelectedInstructionIndex();			
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
					TextToSpeechAPI.speak(c_inst);
				}
				
			}
			else {
				//no previous instruction
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
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null && dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0 && dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0) {
			Segment cSeg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());
			TuxGuitarUtil.playClip(dataModel.getFilePath(), FileOpAPI.SAVE_PATH, cSeg.getStart(),cSeg.getEnd(),dataModel.getTrackNum());		
		}
    	else if(dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()==0) {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_DATA);
    	}
    	else {
    		TextToSpeechAPI.speak(SpeechConst.ERROR_NO_FILE_LOADED);
    	}		
	}
	
	public void toggleModes() {
		GUIDataModel dataModel=  GUIDataModel.getInstance();
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null && dataModel.getCurrentSegment()>=0 && dataModel.getTrackNum()>=0) {		
			if(!dataModel.isOnPercussionTrack()) {
				if(dataModel.isVerbose()) {
					
					//populate instructions
					populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());
					
					//read currently selected instruction
					if(dataModel.getSelectedInstructionIndex() >= 0) {
						instructionsList.programmaticSelect(dataModel.getSelectedInstructionIndex());
						List<String> inst = dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst();
						String instr = inst.get(dataModel.getSelectedInstructionIndex());
						TextToSpeechAPI.speak(instr);
					}
										
					//flip stored flag
					dataModel.setVerbose(false);					
				}
				else {
					
					//populate instructions
					populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());

					//read currently selected instruction
					if(dataModel.getSelectedInstructionIndex() >= 0) {
						instructionsList.programmaticSelect(dataModel.getSelectedInstructionIndex());						
						List<String> inst = dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst();
						String instr = inst.get(dataModel.getSelectedInstructionIndex());
						TextToSpeechAPI.speak(instr);
					}
					
					//flip stored flag
					dataModel.setVerbose(true);
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
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getCurrentSegment() < (dataModel.getInstSegments().size()-1)) {
			dataModel.setCurrentSegment(dataModel.getCurrentSegment()+1);
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());
			}
			else {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());				
			}
			GUIDataModel.getInstance().clearSelectedInstructionIndex();
			instructionsList.refreshGUI();
			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());							
			this.setTitle(dataModel.getFileName().trim() + " (M" + (c_seg.getStart()+1) + "-" + (c_seg.getEnd()+1) + ")");			
			
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
		GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getInstSegments()!=null && dataModel.getInstSegments().size()>0 && dataModel.getCurrentSegment() > 0) {
			dataModel.setCurrentSegment(dataModel.getCurrentSegment()-1);
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getSfInst());				
			}
			else {
				populateInstructionPane(dataModel.getInstSegments().get(dataModel.getCurrentSegment()).getChordInst());								
			}
			GUIDataModel.getInstance().clearSelectedInstructionIndex();
			instructionsList.refreshGUI();			
			Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());							
			this.setTitle(dataModel.getFileName().trim() + " (M" + (c_seg.getStart()+1) + "-" + (c_seg.getEnd()+1) + ")");			
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
		GUIDataModel dataModel = GUIDataModel.getInstance();
		
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
		GUIDataModel.getInstance().clearSelectedInstructionIndex();
		instructionsList.refreshGUI();		
		Segment c_seg = dataModel.getInstSegments().get(dataModel.getCurrentSegment());							
		this.setTitle(dataModel.getFileName().trim() + " (M" + (c_seg.getStart()+1) + "-" + (c_seg.getEnd()+1) + ")");					
	}
	
	public void populateInstructionPane(List<String> instructions) {
    	ArrayAdapter<String> a_opts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,instructions);
    	instructionsList.setAdapter(a_opts);
 	}
	
	public void showLoadFileDialog() {
		FileChooserDialog dialog = new FileChooserDialog(this);
		dialog.setCanCreateFiles(false);
		dialog.setFilter(".*gp1|.*gp2|.*gp3|.*gp4|.*gp5|.*gpx|.*ptb");
	    dialog.show();
	    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
	         public void onFileSelected(Dialog source, File file) {
	        	 
	        	 //stuff
	             source.hide();
	             	             
	             //attempt file load and populate tracks
	             try {
	            	 
		             //populate GUI with selection       
//		             fileField.setText(file.getName());
//	            	 fileField.setContentDescription(file.getName());
	            	 
	            	 //load song and store in gui data model
	            	 TGSong song = TuxGuitarUtil.loadSong(file.getPath());
//	            	 fileField.setText(song.getName());
//	            	 fileField.setContentDescription(song.getName());
	            	 GUIDataModel dataModel = GUIDataModel.getInstance();
	            	 dataModel.setFilePath(file.getPath());
		             dataModel.setFileName(song.getName());	            	 
	            	 if(song!=null) {
	            		 dataModel.setSong(song);
	            	 }
	            	 
	            	 //populate tracks
	            	 populateTrackOptions();
	            	 
	             	//set first segment selected and load instructions
	             	if(dataModel.getTracksList().size() >0) {
	             		
	             		//set params
	             		dataModel.setTrackNum(0);
	             		dataModel.setCurrentSegment(0);
	             		
		             	//enable instructions list
		             	instructionsList.setHilightEnabled(true);
	             		
		             	//perform load and show on GUI
	         			loadInstructions();				    		
	             	}	            	 
	             		            	 
	            	 //notify user that track successfully loaded
	            	 TextToSpeechAPI.speak(SpeechConst.FILE_LOADED);
	             }
	             catch(Exception e) {
	            	 //say could not be loaded
	            	 TextToSpeechAPI.speak(SpeechConst.ERROR_FILE_NOT_LOADED);
	             }	             
	         }
	         public void onFileSelected(Dialog source, File folder, String name) {}
	     });		    
	}
	
    public void populateTrackOptions() {
    	
    	//load data model
    	GUIDataModel dataModel = GUIDataModel.getInstance();
    	
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
    	ArrayAdapter<String> a_opts = new ArrayAdapter<String>(this, R.layout.my_spinner,tracksList);
    	trackChooser.setAdapter(a_opts);
    	trackChooser.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				GUIDataModel.getInstance().setTrackNum(arg2);
				loadInstructions();				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
    		
    	});
    	
    	//store
    	GUIDataModel.getInstance().setTracksList(tracksList);
    }	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void stompModeDialog(MenuItem item) {
    	
    	//tab file must be loaded for stomper
    	GUIDataModel dataModel = GUIDataModel.getInstance();
		if(dataModel.getSong()!=null && dataModel.getTrackNum() >=0) {
    	
	    	//enable stomper if not active
	    	if(!stomper.isEnabled()) {
	    		
	    		//show stomper enabled dialog
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.stomper_dialog_layout);
				dialog.setTitle("Stomp Mode");	 
				Button dialogButton = (Button) dialog.findViewById(R.id.OK_BUTTON);
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
			    		//enable stomper
			    		stomper.start();						
			    		
			    		//close dialog
						dialog.dismiss();
					}
				});	 
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
    	GUIDataModel dataModel = GUIDataModel.getInstance();    	
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
    	GUIDataModel dataModel = GUIDataModel.getInstance();
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
}