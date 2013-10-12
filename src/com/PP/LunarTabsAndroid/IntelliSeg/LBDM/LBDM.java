package com.PP.LunarTabsAndroid.IntelliSeg.LBDM;

import java.util.ArrayList;
import java.util.List;

public class LBDM {
	
	/**
	 * Normalizes boundary strengths
	 * @param s_k
	 * @return
	 */
	public List<Double> normalizeBoundaryStrengths(List<Double> s_k) {
		double sum=0.0;
		for(double s : s_k) {
			sum = sum  + s;
		}		
		List<Double> rtn = new ArrayList<Double>();
		for(double s : s_k) {
			if(sum!=0) {
				rtn.add(s / sum);
			}
			else {
				rtn.add(s);
			}
		}
		return rtn;
	}
	
	/**
	 * Strength of Boundary formula
	 * @param r_im1_i
	 * @param r_i_ip1
	 * @param x_i
	 * @return s_i
	 */ 
	public double stengthOfBoundary(double r_im1_i, double r_i_ip1, double x_i)
	{
		return x_i * (r_im1_i + r_i_ip1);
	}
	
	/**
	 * Degree of change formula
	 * @param x_i 
	 * @param x_ip1
	 * @return r_i,ip1
	 */
	public double degreeOfChange(double x_i, double x_ip1) 
	{
		if(x_i==0 && x_ip1==0) {
			return 0.0;
		}
		else {
			return Math.abs(x_i - x_ip1) / (x_i + x_ip1);
		}
	}

}
