package com.PP.LunarTabsAndroid.Activities;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.PP.LunarTabsAndroid.APIs.TextToSpeechAPI;
import com.PP.LunarTabsAndroid.UI.GUIDataModel;
import com.PP.LunarTabsAndroid.UI.SpeechConst;
import com.daidalos.afiledialog.FileChooserDialog;
import com.example.lunartabsandroid.R;
import com.tuxguitar.TuxGuitarUtil;
import com.tuxguitar.song.models.TGSong;

import android.location.Address;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	
	//components
	protected Button loadTabFileButton;
	protected Button toggleModesButton;
	protected Button playSampleButton;
	protected Button prevMeasButton;
	protected Button nextMeasButton;
	protected EditText fileField;
	protected Spinner trackChooser;
	protected ListView instructionsList;
	
	//Data model
	protected GUIDataModel dataModel;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//init stuff
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//load components
        loadTabFileButton = (Button) findViewById(R.id.loadTabFileButton);
        toggleModesButton = (Button) findViewById(R.id.toggleModesButton);
        playSampleButton = (Button) findViewById(R.id.playSampleButton);
        prevMeasButton = (Button) findViewById(R.id.prevMeasButton);
        nextMeasButton = (Button) findViewById(R.id.nextMeasButton);
        fileField = (EditText) findViewById(R.id.songField);
        trackChooser = (Spinner) findViewById(R.id.trackChooser);
        instructionsList = (ListView) findViewById(R.id.instructionsList);
        
        //register listeners
        loadTabFileButton.setOnClickListener(this);
        toggleModesButton.setOnClickListener(this);
        playSampleButton.setOnClickListener(this);
        prevMeasButton.setOnClickListener(this);
        nextMeasButton.setOnClickListener(this);
        
        //enable APIs
        TextToSpeechAPI.init(this);
        
        //init data model
        dataModel = new GUIDataModel();
        
        //cosmetic
        fileField.setEnabled(false);
                
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==loadTabFileButton.getId()) {
			showLoadFileDialog();
		}
		else if(v.getId()==toggleModesButton.getId()) {
			toggleModes();
		}
		else if(v.getId()==prevMeasButton.getId()) {
			prevMeasure();
		}
		else if(v.getId()==nextMeasButton.getId()) {
			nextMeasure();
		}
		else if(v.getId()==playSampleButton.getId()) {
			playSample();
		}
	}
	
	public void playSample() {		
		if(dataModel.getFilePath()!=null && dataModel.getSong()!=null && dataModel.getCurrentMeas()>=0 && dataModel.getTrackNum()>=0) {
			TuxGuitarUtil.playClip(dataModel.getFilePath(), dataModel.getCurrentMeas(), dataModel.getCurrentMeas(),dataModel.getTrackNum());		
		}
	}
	
	public void toggleModes() {
		if(!dataModel.isOnPercussionTrack()) {
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getMeasureInst().get(dataModel.getCurrentMeas()));
				dataModel.setVerbose(false);
			}
			else {
				populateInstructionPane(dataModel.getSfInst().get(dataModel.getCurrentMeas()));
				dataModel.setVerbose(true);
			}
		}
	}
	
	public void nextMeasure() {
		if(dataModel.getCurrentMeas() < (dataModel.getMeasureInst().size()-1)) {
			dataModel.setCurrentMeas(dataModel.getCurrentMeas()+1);
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getSfInst().get(dataModel.getCurrentMeas()));
			}
			else {
				populateInstructionPane(dataModel.getMeasureInst().get(dataModel.getCurrentMeas()));				
			}
		}
	}
	
	public void prevMeasure() {
		if(dataModel.getCurrentMeas() > 0) {
			dataModel.setCurrentMeas(dataModel.getCurrentMeas()-1);
			if(dataModel.isVerbose()) {
				populateInstructionPane(dataModel.getSfInst().get(dataModel.getCurrentMeas()));				
			}
			else {
				populateInstructionPane(dataModel.getMeasureInst().get(dataModel.getCurrentMeas()));								
			}
		}		
	}
	
	public void loadInstructions() {
		
		//generate instructions
		dataModel.genInstructions();
		
		//populate instructions pane with first measure
		dataModel.setCurrentMeas(0);
		populateInstructionPane(dataModel.getSfInst().get(dataModel.getCurrentMeas()));
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
		             fileField.setText(file.getPath());	            	 
	            	 
	            	 //load song and store in gui data model
	            	 TGSong song = TuxGuitarUtil.loadSong(file.getPath());
	            	 dataModel.setFilePath(file.getPath());
	            	 if(song!=null) {
	            		 dataModel.setSong(song);
	            	 }
	            	 
	            	 //populate tracks
	            	 populateTrackOptions();
	            	 
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
    	
        //populate options in list
        List<String> tracksList = new ArrayList<String>();
        TGSong songLoaded = dataModel.getSong();
        if(songLoaded!=null && songLoaded.countTracks() > 0) {
        	for(int x=0; x < songLoaded.countTracks(); x++) {
        		tracksList.add(songLoaded.getTrack(x).getName());
        	}
        }
    	ArrayAdapter<String> a_opts = new ArrayAdapter<String>(this, R.layout.my_spinner,tracksList);
    	trackChooser.setAdapter(a_opts);
    	trackChooser.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				dataModel.setTrackNum(arg2);
				loadInstructions();				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
    		
    	});
    	
    	//set first index selected and load instructions
    	if(tracksList.size() >0) {
    		dataModel.setTrackNum(0);
			loadInstructions();				    		
    	}
    }	
}