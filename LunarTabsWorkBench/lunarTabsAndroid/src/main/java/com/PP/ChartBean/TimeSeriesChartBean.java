package com.PP.ChartBean;

import android.app.Activity;

public class TimeSeriesChartBean extends ChartBean {
	
	//const
	public static final int DEFAULT_BUFFER_LENGTH = 100;
	
	//vars
	protected int bufferLength;
	protected int loadFactor;
	
	//state
	protected int[] currentT;
	protected int maxT;
	
	/**
	 * Constructor
	 * @param activity
	 * @param title
	 * @param xAxisTitle
	 * @param yAxisTitle
	 * @param seriesTitles
	 * @param seriesColors
	 */
	public TimeSeriesChartBean(Activity activity, String title,
			String xAxisTitle, String yAxisTitle, String[] seriesTitles,
			int[] seriesColors) {
		super(activity, title, xAxisTitle, yAxisTitle, seriesTitles, seriesColors);
		currentT = new int[seriesTitles.length];
		bufferLength = DEFAULT_BUFFER_LENGTH;
	}
		
	public void addToTimeSeries(int series, double val, boolean refocus) {
		addData(series,currentT[series],val,false);
		if(currentT[series] > maxT) {
			maxT = currentT[series];
		}
		clearOldData(series);
		currentT[series]++;		
		if(refocus) {
			refocus();
		}
	}
	
	public void refocus() {
		if(mRenderer!=null) {
			int min = Math.max(0, maxT-bufferLength);
			int max = maxT;
			mRenderer.setXAxisMin(min);
			mRenderer.setXAxisMax(max);
		}
		if(mChart!=null) {
			mChart.repaint();
		}
	}
	
	public void clearOldData(int series) {
		if(mCurrentSeries[series].getItemCount() > 2*bufferLength) {
			mCurrentSeries[series].remove(0);
		}
	}

	/**
	 * @return the bufferLength
	 */
	public int getBufferLength() {
		return bufferLength;
	}

	/**
	 * @param bufferLength the bufferLength to set
	 */
	public void setBufferLength(int bufferLength) {
		this.bufferLength = bufferLength;
	}
}