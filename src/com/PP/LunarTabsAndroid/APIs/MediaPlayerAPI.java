package com.PP.LunarTabsAndroid.APIs;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import com.PP.LunarTabsAndroid.UI.DataModel;


import android.media.MediaPlayer;

public class MediaPlayerAPI implements MediaPlayer.OnCompletionListener {
	
	//singleton
	protected static MediaPlayer mediaPlayer;
	protected MediaPlayerAPI() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
	}
	protected static MediaPlayerAPI instance = null;
	public static MediaPlayerAPI getInstance() {
		if(instance==null) {
			instance = new MediaPlayerAPI();
		}
		return instance;
	}
		
	public void play(String filePath) {
		try {
			
			//load file
			File f = new File(filePath);
			FileInputStream fis = new FileInputStream(f);
			FileDescriptor fd = fis.getFD();
			
			//disable stt
			
			//play
			mediaPlayer.setDataSource(fd);
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		//clear memory of media player
//		mp.release();
		mp.reset();
		
		//delete temporary files
		TuxGuitarUtil.cleanUp(FileOpAPI.SAVE_PATH);
		
		//re-enable stt
		
	}
}