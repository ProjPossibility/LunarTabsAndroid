package com.PP.LunarTabsAndroid.StompDetector;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class StompDetector implements SensorEventListener {
	
	//const
	public static final int START_WAIT = 5000;
	public static final int UNTRIGGER_DELAY = 2000;
	
	//user params
	protected float sensitivity = 0.1f;
	
	//state
	protected volatile boolean enabled;
	protected volatile boolean active;
	protected volatile boolean prevTriggered;
	protected float lastVal;
	
	//sensor managers and parent
	protected SensorManager mSensorManager;
	protected Sensor mAccel;	
	protected Activity parent;
	
	public StompDetector(Activity parent) {
		this.parent = parent;
		enabled = false;
		active = false;
		prevTriggered = false;
		lastVal = -1;		
		
		//init sensors
	    mSensorManager = (SensorManager)parent.getSystemService(parent.SENSOR_SERVICE);
	    mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    mSensorManager.registerListener(this, mAccel,SensorManager.SENSOR_DELAY_NORMAL);	    
	    
	}
	
	/**
	 * start function
	 */
	public void start() {
						
		//make inactive for start wait
		enabled = true;		
		deactiveFor(START_WAIT);
		
	}
	
	/**
	 * stop function
	 */
	public void stop() {
		enabled = false;
		active = false;
	}
	
	/**
	 * 
	 * @param wait
	 */
	protected void deactiveFor(final int wait) {
		
		//make triggering mechanism inactive and 
		//make reactive after wait
		Thread t = new Thread() {
			
			@Override
			public void run() {
				try {
					active = false;
					Thread.sleep(wait);
					active = true;
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		};
		t.start();
		
	}
	
	protected boolean trigger(float val) {
				
		//if active, do trigger rule.
		if(active) {
			
			//diff rule
			float diff = Math.abs(lastVal-val);
			boolean triggered = (diff >= sensitivity);
			
			//we've triggered and not previously triggered -- set inactive for delay time
			if(!prevTriggered && triggered) {
				prevTriggered = true;
				deactiveFor(UNTRIGGER_DELAY);
				lastVal = val;
				return true;
			}
			
			//not triggered and previously triggered and not in deactivate loop
			//allow triggering again.
			if(!triggered && prevTriggered) {
				prevTriggered = false;
			}
		}
		
		//update
		lastVal = val;
						
		//return false by default
		return false; 
					
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(enabled) {
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
				float accZ = event.values[2];
				if(trigger(accZ)) {
					trigger_callback();
				}			
			}
		}
	}
	
	protected abstract void trigger_callback();
	
	//unused
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
}
