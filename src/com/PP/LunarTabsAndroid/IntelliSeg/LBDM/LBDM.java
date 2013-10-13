package com.PP.LunarTabsAndroid.IntelliSeg.LBDM;

import java.util.ArrayList;
import java.util.List;

import com.PP.LunarTabsAndroid.IntelliSeg.Abstract.Segment;

public class LBDM {
	
	/**
	 * Segment method
	 * @param P List of Parametric Profiles
	 * @param weights How Profiles should be weighted
	 * @return
	 */
	public static List<Segment> segment(List<List<Double>> P, double[] weights) {
		
		//compute strengths of boundaries for each parametric profile and normalize.
		List<List<Double>> S = new ArrayList<List<Double>>();
		for(List<Double> P_k : P) {
			List<Double> r = computeDegreesOfChange(P_k);
			List<Double> S_k = computeStrengthsOfBoundaries(P_k,r);
			S_k = normalizeBoundaryStrengths(S_k);
		}
		
		//compute weighted boundary
		List<Double> weightedBoundary = weightBoundaries(S,weights);
		
		//segment according to local peaks
		List<Segment> rtn = localPeakSeg(weightedBoundary);

		//return
		return rtn;
	}
	
	/**
	 * Segment sequences by local peaks
	 * @param seq The time series
	 * @return
	 */
	protected static List<Segment> localPeakSeg(List<Double> seq) {
		
		//init rtn
		List<Segment> rtn = new ArrayList<Segment>();
		
		//loop
		int lastStart = 0;		
		for(int x=1; x < (seq.size()-1); x++) {
			double lastVal = seq.get(x-1);
			double cVal = seq.get(x);
			double nextVal = seq.get(x+1);
			if(cVal > lastVal && cVal > nextVal) {
				int end = x;
				Segment seg = new Segment(lastStart,end);
				rtn.add(seg);
				lastStart = (x+1);
			}
		}
		
		//handle last segment
		if(lastStart < (seq.size()-1)) {
			Segment seg = new Segment(lastStart,seq.size()-1);
			rtn.add(seg);
		}
		
		//return
		return rtn;
	}
		
	/**
	 * Computes Weighted Boundary
	 * @param S List of Boundary Sequences S_k
	 * @param weights List of how to weight each boundary
	 * @return
	 */
	protected static List<Double> weightBoundaries(List<List<Double>> S, double[] weights) {
		
		//init rtn
		List<Double> rtn = new ArrayList<Double>();
				
		//easy exist -- nothing to do
		if(S.size()==0 || S.get(0).size()==0) {
			return rtn;
		}
		
		//compute weighted boundary
		int seqLength = S.get(0).size();
		for(int x=0; x < seqLength; x++) {
			double newBoundary=0.0;
			for(int y=0; y < S.size(); y++) {				
				newBoundary = newBoundary + weights[y] * S.get(y).get(x);
			}
			rtn.add(newBoundary);
		}
		
		//return 
		return rtn;
	}
	
	/**
	 * Normalizes boundary strengths
	 * @param s_k
	 * @return
	 */
	protected static List<Double> normalizeBoundaryStrengths(List<Double> s_k) {
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
	 * Computes Strengths of boundaries function
	 * @param P_k Parametric Profile
	 * @param r Degrees of Change
	 * @return Strengths of Boundaries
	 */
	protected static List<Double> computeStrengthsOfBoundaries(List<Double> P_k, List<Double> r) {
		List<Double> rtn = new ArrayList<Double>();
		for(int x=0; x < r.size(); x++) {
			double r_im1_i = 0.0;
			if(x!=0) {
				r_im1_i = r.get(x-1);
			}
			double r_i_ip1 = r.get(x);
			double val = P_k.get(x) * (r_im1_i + r_i_ip1);
			rtn.add(val);
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
	protected static double stengthOfBoundary(double r_im1_i, double r_i_ip1, double x_i)
	{
		return x_i * (r_im1_i + r_i_ip1);
	}
	
	/**
	 * Compute degree of changes for parametric profile
	 * @param P_k Parameteric Profile
	 * @return r_i,i+1 degree of change function
	 */
	protected static List<Double> computeDegreesOfChange(List<Double> P_k) {
		List<Double> rtn = new ArrayList<Double>();
		for(int x=1; x < P_k.size(); x++) {
			double r = 0.0; 
			double x_ip1 = P_k.get(x);
			double x_i = P_k.get(x-1);
			r = degreeOfChange(x_i,x_ip1);
			rtn.add(r);
		}
		rtn.add(P_k.get(P_k.size()-1)); //last element
		return rtn;
	}
	
	/**
	 * Degree of change formula
	 * @param x_i 
	 * @param x_ip1
	 * @return r_i,ip1
	 */
	protected static double degreeOfChange(double x_i, double x_ip1) 
	{
		if(x_i==0 && x_ip1==0) {
			return 0.0;
		}
		else {
			return Math.abs(x_i - x_ip1) / (x_i + x_ip1);
		}
	}
}
