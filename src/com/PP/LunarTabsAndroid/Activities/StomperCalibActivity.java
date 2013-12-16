package com.PP.LunarTabsAndroid.Activities;
import java.text.DecimalFormat;

import com.PP.ChartBean.TimeSeriesChartBean;
import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.StomperParams;
import com.PP.StompDetector.StompDetector;
import com.PP.StompDetector.StompListener;
import com.example.lunartabsandroid.R;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;


public class StomperCalibActivity extends Activity implements SensorEventListener, StompListener, OnClickListener  {
	
	//const
	public static final float THRESHOLD_INCREMENT = 0.05f;
	public static final int DELAY_INCREMENT = 200;
	public static final int MAX_DELAY = 5000;
	public static final int MIN_DELAY = 0;
	public static final float MAX_THRESHOLD = 10.0f;
	public static final float MIN_THRESHOLD = 0.01f;
	
	//vars
	protected TimeSeriesChartBean bean;
	protected DecimalFormat decFormat;
	
	//sensor
	protected SensorManager mSensorManager;
	protected Sensor mAccel;	
	protected StompDetector detector;
	
	//buttons
	protected Button incThresholdButton;
	protected Button decThresholdButton;
	protected Button incDelayButton;
	protected Button decDelayButton;
	protected Button okButton;
	
	//Text View and layouts
	protected TextView thresholdTV;
	protected TextView delayTV;
	protected LinearLayout thresholdLayout;
	protected LinearLayout delayLayout;
	protected LinearLayout buttonLayout;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set up chart bean
        setContentView(R.layout.stomp_calib_layout);  
        bean = new TimeSeriesChartBean(this,"Accelerometer Monitor", "Time","Accel Reading", new String[] {"Accelerometer","Upper Sensitivity Threshold","Lower Sensitivity Threshold"}, new int[] {Color.BLUE,Color.RED, Color.RED});
        bean.getMRenderer().setYAxisMin(5);
        bean.getMRenderer().setYAxisMax(20);        
	    
	    //load components
        incThresholdButton = (Button) findViewById(R.id.incThresholdButton);	    
        decThresholdButton = (Button) findViewById(R.id.decThresholdButton);	    
        incDelayButton = (Button) findViewById(R.id.incDelayButton);	    
        decDelayButton = (Button) findViewById(R.id.decDelayButton);	 
        okButton = (Button) findViewById(R.id.okButton);        
        thresholdTV = (TextView) findViewById(R.id.thresholdText);
        delayTV = (TextView) findViewById(R.id.delayText);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        thresholdLayout = (LinearLayout) findViewById(R.id.senitivityButtonLayout);
        delayLayout = (LinearLayout) findViewById(R.id.delayButtonLayout);
        
        //register listeners
        incThresholdButton.setOnClickListener(this);
        decThresholdButton.setOnClickListener(this);
        incDelayButton.setOnClickListener(this);
        decDelayButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

        //UI Stuff
        buttonLayout.setBackgroundColor(Color.BLACK);
        thresholdLayout.setBackgroundColor(Color.BLACK);
        delayLayout.setBackgroundColor(Color.BLACK);
        incThresholdButton.setBackgroundColor(Color.WHITE);
        incThresholdButton.setTextColor(Color.BLACK);        
        decThresholdButton.setBackgroundColor(Color.WHITE);
        decThresholdButton.setTextColor(Color.BLACK);        
        incDelayButton.setBackgroundColor(Color.WHITE);
        incDelayButton.setTextColor(Color.BLACK);
        decDelayButton.setBackgroundColor(Color.WHITE);
        decDelayButton.setTextColor(Color.BLACK);
        okButton.setBackgroundColor(Color.WHITE);
        okButton.setTextColor(Color.BLACK);
        thresholdTV.setBackgroundColor(Color.BLACK);
        thresholdTV.setTextColor(Color.WHITE);        
        delayTV.setBackgroundColor(Color.BLACK);
        delayTV.setTextColor(Color.WHITE);
        decFormat = new DecimalFormat("0.00");
                
        //start sensor stuff
	    detector = new StompDetector(this);
	    detector.addStompListening(this);
	    detector.setStart_wait(0);
	    detector.start();
	    mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	    mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    mSensorManager.registerListener(this, mAccel,SensorManager.SENSOR_DELAY_GAME);
	    
	    //starting params
	    updateParamSettings();
	}
	
	protected void onResume() {
		
		//on resume calls
		super.onResume();		
		if(bean!=null) {
			bean.onResume();
		}
		detector.onResume();
		
	}
	
	public void onStop() {
		
		//on stop calls
		super.onStop();
		detector.onStop();
		
		//save
		StomperParams.getInstance().saveInstance();
	}	


	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
			float accZ = event.values[2];
			bean.addToTimeSeries(0, accZ, false);
			bean.addToTimeSeries(1, 10+detector.getSensitivity(), true);
			bean.addToTimeSeries(2, 10-detector.getSensitivity(), false);
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void trigger_callback(double timestamp) {
		final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
	    tg.startTone(ToneGenerator.TONE_PROP_BEEP);	
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==incThresholdButton.getId()) {
			incThreshold();
		}
		else if(v.getId()==decThresholdButton.getId()) {
			decThreshold();
		}
		else if(v.getId()==incDelayButton.getId()) {
			incDelay();
		}
		else if(v.getId()==decDelayButton.getId()) {
			decDelay();
		}
		else if(v.getId()==okButton.getId()) {
			clickOkay();
		}
	}
	
	public void incThreshold() {
		float thresh = detector.getSensitivity();
		if((thresh+THRESHOLD_INCREMENT) <= MAX_THRESHOLD) {
			thresh = thresh + THRESHOLD_INCREMENT;
			StomperParams.getInstance().setStomperSensitivity(thresh);
			updateParamSettings();
		}
	}
	
	public void decThreshold() {
		float thresh = detector.getSensitivity();
		if((thresh-THRESHOLD_INCREMENT) >= MIN_THRESHOLD) {
			thresh = thresh - THRESHOLD_INCREMENT;
		}
		else {
			thresh = MIN_THRESHOLD;
		}
		StomperParams.getInstance().setStomperSensitivity(thresh);
		updateParamSettings();
	}
	
	public void incDelay() {
		int delay = detector.getUntrigger_delay();
		if(delay+DELAY_INCREMENT <= MAX_DELAY) {
			delay = delay + DELAY_INCREMENT;
			StomperParams.getInstance().setStomperDelay(delay);
			updateParamSettings();			
		}
	}
	
	public void decDelay() {
		int delay = detector.getUntrigger_delay();
		if((delay-DELAY_INCREMENT) >= MIN_DELAY) {
			delay = delay - DELAY_INCREMENT;
		}
		else {
			delay = MIN_DELAY;
		}
		StomperParams.getInstance().setStomperDelay(delay);
		updateParamSettings();
	}
	
	public void clickOkay() {
		
		//finish activity
		finish();
		
		//return to main activity
		Intent i = new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);												
	}
	
	public void updateParamSettings() {
		
		//set stomper params
		detector.setSensitivity(StomperParams.getInstance().getStomperSensitivity());
		detector.setUntrigger_delay(StomperParams.getInstance().getStomperDelay());
		
		//update display
		thresholdTV.setText("Thresh: " + decFormat.format(StomperParams.getInstance().getStomperSensitivity()));					
		delayTV.setText("Delay: " + StomperParams.getInstance().getStomperDelay() + "ms");			
	}
	
}