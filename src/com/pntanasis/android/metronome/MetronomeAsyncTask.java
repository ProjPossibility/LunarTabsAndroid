package com.pntanasis.android.metronome;

import com.PP.LunarTabsAndroid.APIs.MetronomeAPI;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MetronomeAsyncTask extends AsyncTask<Void,Void,String> {
	protected Metronome metronome;
    protected Handler mHandler;
	
	public MetronomeAsyncTask() {
        mHandler = getHandler();
		metronome = new Metronome(mHandler);
	}

	@Override
	protected String doInBackground(Void... params) {
		MetronomeAPI m = MetronomeAPI.getInstance();
		metronome.setBeat(m.getBeats());
		metronome.setNoteValue(m.getNoteValue());
		metronome.setBpm(m.getBpm());
		metronome.setBeatSound(m.getBeatSound());
		metronome.setSound(m.getSound());

		metronome.play();
		
		return null;			
	}
	
	public void stop() {
		metronome.stop();
		metronome = null;
	}
	
	public void setBpm(short bpm) {
		metronome.setBpm(bpm);
		metronome.calcSilence();
	}
	
	public void setBeat(short beat) {
		if(metronome != null)
			metronome.setBeat(beat);
	}
	
    // have in mind that: http://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler
    // in this case we should be fine as no delayed messages are queued
    public Handler getHandler() {
    	return new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	String message = (String)msg.obj;
            	Log.d("M",message);
            }
        };
    }
	
}