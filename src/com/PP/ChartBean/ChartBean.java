package com.PP.ChartBean;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.lunartabsandroid.R;

import android.app.Activity;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

public class ChartBean {
	
	//vars
	protected String title;
	protected String xAxisTitle;
	protected String yAxisTitle;
	protected String[] seriesTitles;
	protected int[] seriesColors;
	protected Activity activity;
	
	//data
    protected GraphicalView mChart;
    protected XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    protected XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    protected XYSeries[] mCurrentSeries;
    private XYSeriesRenderer[] mCurrentRenderers;
    
    /**
     * Constructor
     * @param activity
     * @param title
     * @param xAxisTitle
     * @param yAxisTitle
     */
    public ChartBean(Activity activity, String title, String xAxisTitle, String yAxisTitle, String[] seriesTitles, int[] seriesColors) {
    	this.activity = activity;
    	this.title = title;
    	this.xAxisTitle = xAxisTitle;
    	this.yAxisTitle = yAxisTitle;
    	this.seriesTitles = seriesTitles;
    	this.seriesColors = seriesColors;
    	initChart();
    }
    
    protected void initChart() {
    	mCurrentSeries = new XYSeries[seriesTitles.length];
        mCurrentRenderers = new XYSeriesRenderer[seriesTitles.length];    	
    	for(int x=0; x < seriesTitles.length;x++) {
    		mCurrentSeries[x] = new XYSeries(seriesTitles[x]);
    		mDataset.addSeries(mCurrentSeries[x]);
    		mCurrentRenderers[x] = new XYSeriesRenderer();
    		mCurrentRenderers[x].setColor(seriesColors[x]);
            mRenderer.addSeriesRenderer(mCurrentRenderers[x]);
    	}
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setPanEnabled(false,false);
        mRenderer.setXTitle(xAxisTitle);
        mRenderer.setYTitle(yAxisTitle);
        mRenderer.setAxisTitleTextSize(40);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setYLabelsPadding(30);
        mRenderer.setXLabelsAlign(Align.CENTER);
        mRenderer.setYLabelsAlign(Align.CENTER);   
        mRenderer.setChartTitle(title);
        mRenderer.setChartTitleTextSize(40);
        mRenderer.setFitLegend(true);
        mRenderer.setLegendHeight(50);
        mRenderer.setLegendTextSize(30);
        mRenderer.setMargins(new int[] { 70, 90, 70, 40 });
    }

    public void addData(int seriesIndex, double x, double y, boolean repaint) {
    	mCurrentSeries[seriesIndex].add(x,y);
    	if(mChart!=null && repaint) {
    		mChart.repaint();
    	}
    }
    
    public void addData(int seriesIndex, List<Double> x, List<Double> y, boolean repaint) {
    	for(int z=0; z < x.size() && z < y.size(); z++) {
    		mCurrentSeries[seriesIndex].add(x.get(z),y.get(z));
    	}
    	if(mChart!=null && repaint) {
    		mChart.repaint();
    	}
    }

    public void onResume() {
    	if(activity!=null) {
	        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.chart);
	        if (mChart == null) {
	            mChart = ChartFactory.getLineChartView(activity, mDataset, mRenderer);
	            layout.addView(mChart);
	        } else {
	            mChart.repaint();
	        }
    	}
    }

	/**
	 * @return the mRenderer
	 */
	public XYMultipleSeriesRenderer getMRenderer() {
		return mRenderer;
	}

	/**
	 * @param mRenderer the mRenderer to set
	 */
	public void setMRenderer(XYMultipleSeriesRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}
}