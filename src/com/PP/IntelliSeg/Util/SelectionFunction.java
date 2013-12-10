package com.PP.IntelliSeg.Util;

import java.util.Set;

public class SelectionFunction {
	
	//default parameters
	public static final double a_DEFAULT = 1.0;
	public static final double b_DEFAULT = 2.0;
	public static final double c_DEFAULT = 3.0;
	
	//parameters in use
	protected static double a = a_DEFAULT;
	protected static double b = b_DEFAULT;
	protected static double c = c_DEFAULT;
		
	public static double score(String gram, Set<Integer> startSet, String str_rep)
	{		
		//compute parts
		double L = (double) gram.length();
		double F = (double) startSet.size();
		double T = F*L;
		boolean[] used = new boolean[str_rep.length()];
		for(int start : startSet) {
			for(int x=start; x < (start+L); x++) {
				used[x] = true;
			}
		}
		double U=0.0;
		for(boolean b : used) {
			if(b) {
				U++;
			}
		}
		double DOL = (T-U) / U;
				
		//return
		return (Math.pow(L, a) * Math.pow(F, b)) / (Math.pow(10.0, c*DOL));
	}

	/**
	 * @return the a
	 */
	public static double getA() {
		return a;
	}

	/**
	 * @param a the a to set
	 */
	public static void setA(double a_new) {
		a = a_new;
	}

	/**
	 * @return the b
	 */
	public static double getB() {
		return b;
	}

	/**
	 * @param b the b to set
	 */
	public static void setB(double b_new) {
		b = b_new;
	}

	/**
	 * @return the c
	 */
	public static double getC() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public static void setC(double c_new) {
		c = c_new;
	}	
}
