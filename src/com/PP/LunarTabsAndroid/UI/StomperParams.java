package com.PP.LunarTabsAndroid.UI;

import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.StompDetector.StompDetector;

public class StomperParams implements java.io.Serializable {
	
	//params
	protected volatile float stomperSensitivity;
	protected volatile int stomperDelay;
	
	//singleton
	protected StomperParams() {
		stomperSensitivity = StompDetector.DEFAULT_SENSITIVITY;
		stomperDelay = StompDetector.UNTRIGGER_DELAY_DEFAULT;
	}
	protected static StomperParams instance;
	public static StomperParams getInstance() {
		if(instance==null) {
			instance = FileOpAPI.readModel(FileOpAPI.STOMPER_MODEL_FILE);
			if(instance==null) {
				instance = new StomperParams();
			}
		}
		return instance;
	}
	/**
	 * @return the stomperSensitivity
	 */
	public float getStomperSensitivity() {
		return stomperSensitivity;
	}
	/**
	 * @param stomperSensitivity the stomperSensitivity to set
	 */
	public void setStomperSensitivity(float stomperSensitivity) {
		this.stomperSensitivity = stomperSensitivity;
	}
	/**
	 * @return the stomperDelay
	 */
	public int getStomperDelay() {
		return stomperDelay;
	}
	/**
	 * @param stomperDelay the stomperDelay to set
	 */
	public void setStomperDelay(int stomperDelay) {
		this.stomperDelay = stomperDelay;
	}	
	
	public void saveInstance() {
		FileOpAPI.writeModel(instance, FileOpAPI.STOMPER_MODEL_FILE);
	}	
}
