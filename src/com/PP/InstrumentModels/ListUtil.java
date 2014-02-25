package com.PP.InstrumentModels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtil {
	
	/**
	 * Returns number of elements that two sorted lists have in common.
	 * @param a First sorted list
	 * @param b Second sorted list
	 * @return
	 */
	public static int computeMatchScore(List<String> a, List<String> b) {
		int ptr_a = 0;
		int ptr_b = 0;
		int score = 0;
		while(ptr_a < a.size() && ptr_b < b.size()) {
			String elem_a = a.get(ptr_a);
			String elem_b = b.get(ptr_b);
			if(elem_a.equalsIgnoreCase(elem_b)) {
				ptr_a++;
				ptr_b++;
				score++;
			}
			else if(elem_a.compareTo(elem_b) < 0) {
				ptr_a++;
			}
			else if(elem_a.compareTo(elem_b) > 0) {
				ptr_b++;
			}
		}
		return score;
	}
	
	/**
	 * Returns list with only unique elements in list a
	 * @param a input_list
	 * @return output_list with only unique elements
	 */
	public static List<String> unique(List<String> a) {
		Set<String> uniqueSet = new HashSet<String>(a);
		return new ArrayList<String>(uniqueSet);
	}
}
