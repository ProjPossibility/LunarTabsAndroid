package com.PP.IntelliSeg.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SelStruct {
	
	//fields
	protected String gram;
	protected Set<Integer> startSet;
	protected double score;
	protected int firstOcc;
	
	/**
	 * Constructor
	 * @param gram
	 * @param startSet
	 * @param score
	 */
	public SelStruct(String gram, Set<Integer> startSet, double score) {
		this.gram = gram;
		this.startSet = startSet;
		this.score = score;
		if(startSet.size() > 0) {
			List<Integer> list = new ArrayList<Integer>(startSet);
			Collections.sort(list);
			this.firstOcc = list.get(0);
		}
	}
	
	/**
	 * Comparator for score order (sort in descending order of score)
	 * @return
	 */
	public static Comparator<SelStruct> getScoreComparator() {
		return new Comparator<SelStruct>() {

			@Override
			public int compare(SelStruct lhs, SelStruct rhs) {
				//descending order
				return Double.compare(rhs.score, lhs.score);				
			}
		};
	}

	/**
	 * Comparator for order of First Occurrence
	 * @return
	 */
	public static Comparator<SelStruct> getFirstOccurrenceComparator() {
		return new Comparator<SelStruct>() {

			@Override
			public int compare(SelStruct lhs, SelStruct rhs) {
				return lhs.firstOcc - rhs.firstOcc;
			}
		};
	}
	
	/**
	 * @return the gram
	 */
	public String getGram() {
		return gram;
	}

	/**
	 * @param gram the gram to set
	 */
	public void setGram(String gram) {
		this.gram = gram;
	}

	/**
	 * @return the startSet
	 */
	public Set<Integer> getStartSet() {
		return startSet;
	}

	/**
	 * @param startSet the startSet to set
	 */
	public void setStartSet(Set<Integer> startSet) {
		this.startSet = startSet;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
}
