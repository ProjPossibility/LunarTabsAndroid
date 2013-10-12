package com.PP.LunarTabsAndroid.IntelliSeg.Crochemore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Crochemore {
	
	//debug flag
	public static boolean DEBUG_MODE = false;
	
	/**
	 * Main function
	 * @param str Input
	 * @return Map of Repetitions
	 */
	public static Map<String,Set<Integer>> seg(String str) {
		
		//repetition map
		Map<String,Set<Integer>> rtn = new HashMap<String,Set<Integer>>();
		
		//create first level start sets
		Map<String, Set<Integer>> cLevel = new HashMap<String,Set<Integer>>();
		for(int x=0; x < str.length(); x++) {
			String s = ""+str.charAt(x);
			Set<Integer> preList = null;
			if(cLevel.containsKey(s)) {
				preList = cLevel.get(s);
			}
			else {
				preList = new HashSet<Integer>();
				cLevel.put(s,preList);
				rtn.put(s, preList);
			}
			preList.add((x)); //1-index everything			
		}
		
		//iter till convergence
		if(DEBUG_MODE) {
			printMap(cLevel);
		}
		while(!isConverged(cLevel)) {
			cLevel = level_iter(cLevel,rtn);
			if(DEBUG_MODE) {
				printMap(cLevel);			
			}
		}
		
		//remove non-repeating parts
		List<String> toRemove = new ArrayList<String>();
		for(String key : rtn.keySet()) {
			if(rtn.get(key).size() < 2) {
				toRemove.add(key);
			}
		}
		for(String key : toRemove) {
			rtn.remove(key);
		}
		
		//return
		return rtn;
		
	}
	
	/**
	 * Whether algorithm converged to singleton repetition.
	 * @param cLevel Current level
	 * @return Whether converged
	 */
	protected static boolean isConverged(Map<String,Set<Integer>> cLevel) {
		
		//see if we have a singleton repetition
		int cnt=0;
		for(String key : cLevel.keySet()) {
			Set<Integer> set = cLevel.get(key);
			if(set.size() > 1)
				cnt = cnt + 1;
			if(cnt > 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Helper function for iterations
	 * @param prevLevel
	 * @param rtn
	 * @return
	 */
	protected static Map<String,Set<Integer>> level_iter(Map<String,Set<Integer>> prevLevel, Map<String,Set<Integer>> rtn) {
		
		//create rtn
		Map<String,Set<Integer>> nextLevel = new HashMap<String,Set<Integer>>();
		
		//loop
		for(String set1Key : prevLevel.keySet()) {
			
			//get start set currently being refined. If singleton, then don't need to refine.
			Set<Integer> set1 = prevLevel.get(set1Key);
			if(set1.size()<2) {
				continue;
			}
			
			//loop over other start sets at this level
			for(String set2Key : prevLevel.keySet()) {
				
				//easy exit (no prefix fit)
				int prevLevelLength = set2Key.length();
				if(prevLevelLength!=1 && set2Key.startsWith(set1Key.substring(set1Key.length()-prevLevelLength)))
				{
					continue;
				}
				
				//get other set
				Set<Integer> set2 = prevLevel.get(set2Key);
								
				//compute split on start sets
				Set<Integer> newSet = splitStartSets(set1,set2);
				
				//Store if not empty.
				if(!newSet.isEmpty()) {
					
					//compute new prefix
					String newPrefix = set1Key.substring(0,set1Key.length()-prevLevelLength+1) + set2Key.substring(set2Key.length()-prevLevelLength,set2Key.length());
					nextLevel.put(newPrefix, newSet);
					rtn.put(newPrefix, newSet);
				}
			}
		}
		
		//return
		return nextLevel;
	}
	
	/**
	 * Splits start set a in relation to start set b
	 * @param a one start set
	 * @param b another start set
	 */
	protected static Set<Integer> splitStartSets(Set<Integer> a, Set<Integer> b) {
		Set<Integer> in = new HashSet<Integer>();
		for(Integer a1 : a) {
			if(b.contains(a1+1)) {
				in.add(a1);
			}
		}
		return in;
	}
	
	public static void printMap(Map<String,Set<Integer>> map) {
		for(String key : map.keySet()) {
			Set<Integer> set = map.get(key);
			System.out.print(key + " =[");
			for(int x : set) {
				System.out.print(x+1 + ",");
			}
			System.out.println("]");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		String str = "ababccabacbabcca";
		printMap(seg(str));
//		Map<String,List<Integer>> map = seg(str);
//		printMap(map);
		/*
		Set<Integer> a = new HashSet<Integer>();
		a.add(1);
		a.add(3);
		a.add(7);
		a.add(9);
		a.add(12);
		a.add(16);
		Set<Integer> b = new HashSet<Integer>();
		b.add(2);
		b.add(4);
		b.add(8);
		b.add(11);
		b.add(13);
		List<Set<Integer>> inout = splitStartSets(a,b);
		Set<Integer> in = inout.get(0);
		System.out.print("{");
		for(Integer i : in) {
			System.out.print(i + " ");
		}
		System.out.print("} ");
		Set<Integer> out = inout.get(1);
		System.out.print("{");
		for(Integer i : out) {
			System.out.print(i + " ");
		}
		System.out.print("} ");
		System.out.flush();
		*/
	}
}
