package com.PP.LunarTabsAndroid.UI;

import java.io.File;

import android.os.Environment;

import com.PP.LunarTabsAndroid.APIs.FileOpAPI;
import com.PP.StompDetector.StompDetector;

public class SerializedParams implements java.io.Serializable {
		
	//params for stomper
	protected volatile float stomperSensitivity;
	protected volatile int stomperDelay;
	
	//home directory
	protected volatile String homeDir;
	
	//singleton
	protected SerializedParams() {
		stomperSensitivity = StompDetector.DEFAULT_SENSITIVITY;
		stomperDelay = StompDetector.UNTRIGGER_DELAY_DEFAULT;
		homeDir = Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	protected static SerializedParams instance;
	public static SerializedParams getInstance() {
		if(instance==null) {
			instance = FileOpAPI.readModel(FileOpAPI.STOMPER_MODEL_FILE);
			if(instance==null) {
				instance = new SerializedParams();
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
	/**
	 * @return the homeDir
	 */
	public String getHomeDir() {
		return homeDir;
	}
	/**
	 * @param homeDir the homeDir to set
	 */
	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}
	
	public void saveInstance() {
		FileOpAPI.writeModel(instance, FileOpAPI.STOMPER_MODEL_FILE);
	}	
}
