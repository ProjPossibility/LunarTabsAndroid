package com.root.gast.speech.activation;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class TimeoutMonitor {
	
	//state
	protected int timeoutLim;
	protected LinkedBlockingQueue<Boolean> blockingQ;
	
	//ctr
	public TimeoutMonitor(int timeoutLim) {
		blockingQ = new LinkedBlockingQueue<Boolean>();
		this.timeoutLim = timeoutLim;
	}
	
	//abstract timeout method
	protected abstract void timeout();
	
	protected void blocking_thread() {
		boolean val;
		try {
			
			//figure out first val that gets here
			val = blockingQ.take();
			
			//if timeout value, call timeout function
			if(!val) {
				timeout();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void timeout_thread() {
		try {
			Thread.sleep(timeoutLim);
			blockingQ.add(false); //send failure
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		
		//start blocking thread
		(new Thread() {
			public void run() {
				blocking_thread();
			}
		}).start();
		
		//start timeout thread		
		(new Thread() {
			public void run() {
				timeout_thread();
			}
		}).start();
	}
	
	public void success() {
		blockingQ.add(true); //success flag
	}
}
