package org.purl.beroca.crawler.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.purl.beroca.crawler.model.SortedSetOfLibraryRank;

public class SortedSetOfLibraryRankTest {
	
	private SortedSetOfLibraryRank sortedMap;
	
	private Map<String, ArrayList<String>> testMap;
	private Map<String, Integer> testRank;
	private SortedSet<Integer> testSet = null;

	@Before
	public void setUp() throws Exception {
		
		sortedMap = new SortedSetOfLibraryRank();
		
		this.testMap = new HashMap<String, ArrayList<String>>();
		this.testMap.put( "url1", new ArrayList<String>(Arrays.asList("lib1", "lib2", "lib3")) );
		this.testMap.put( "url2", new ArrayList<String>(Arrays.asList("lib1", "lib4")) );
		this.testMap.put( "url3", new ArrayList<String>(Arrays.asList("lib1", "lib2", "lib5", "lib6")) );
		
		this.testRank = new HashMap<String, Integer>();
		this.testRank.put( "lib1", 3);
		this.testRank.put( "lib2", 2);
		this.testRank.put( "lib3", 1);
		this.testRank.put( "lib4", 1);
		this.testRank.put( "lib5", 1);
		this.testRank.put( "lib6", 1);
		
		// this.testSet = null;
	}

	@Test
	public void testGetRank() {
		
		sortedMap.updateRankOfString("url1", testMap.get("url1"));
		assertEquals("getRank", 1, sortedMap.getRank("lib1"));
		
		sortedMap.updateRankOfString("url2", testMap.get("url2"));
		assertEquals("getRank", 2, sortedMap.getRank("lib1"));

		sortedMap.updateRankOfString("url3", testMap.get("url3"));
		assertEquals("getRank", 3, sortedMap.getRank("lib1"));
		
		assertEquals("getRank", 2, sortedMap.getRank("lib2"));
		assertEquals("getRank", 1, sortedMap.getRank("lib3"));
		assertEquals("getRank", 1, sortedMap.getRank("lib4"));
		assertEquals("getRank", 1, sortedMap.getRank("lib5"));
		assertEquals("getRank", 1, sortedMap.getRank("lib6"));
	}
	
	@Test
	public void testUpdateRankOfString() {

		sortedMap.updateRankOfString("url1", testMap.get("url1"));
		assertEquals("updateRankOfString", new Integer(1), sortedMap.getMapOfLibToRank().get("lib1"));
		assertEquals("updateRankOfString", new Integer(1), sortedMap.getMapOfLibToRank().get("lib2"));
		assertEquals("updateRankOfString", new Integer(1), sortedMap.getMapOfLibToRank().get("lib3"));

		sortedMap.updateRankOfString("url2", testMap.get("url2"));
		assertEquals("updateRankOfString", new Integer(2), sortedMap.getMapOfLibToRank().get("lib1"));
		assertEquals("updateRankOfString", new Integer(1), sortedMap.getMapOfLibToRank().get("lib4"));
		
		sortedMap.updateRankOfString("url3", testMap.get("url3"));
		assertEquals("updateRankOfString", new Integer(3), sortedMap.getMapOfLibToRank().get("lib1"));
		assertEquals("updateRankOfString", new Integer(2), sortedMap.getMapOfLibToRank().get("lib2"));
		assertEquals("updateRankOfString", new Integer(1), sortedMap.getMapOfLibToRank().get("lib5"));
		assertEquals("updateRankOfString", new Integer(1), sortedMap.getMapOfLibToRank().get("lib6"));
	}
	
	@Test
	public void testSortRankOfStringSet() {
		
		sortedMap.updateRankOfString("url1", testMap.get("url1"));
		sortedMap.updateRankOfString("url2", testMap.get("url2"));
		sortedMap.updateRankOfString("url3", testMap.get("url3"));
		sortedMap.sortRankOfStringSet();
		
		assertEquals("sortRankOfStringSet", true, sortedMap.getSetOfRankSorted().contains(3));
		assertEquals("sortRankOfStringSet", true, sortedMap.getSetOfRankSorted().contains(2));
		assertEquals("sortRankOfStringSet", true, sortedMap.getSetOfRankSorted().contains(1));
		
		testSet = new TreeSet<Integer>(testRank.values());
		assertEquals("sortRankOfStringSet", testSet.toString(), sortedMap.getSetOfRankSorted().toString());
	}	
}
