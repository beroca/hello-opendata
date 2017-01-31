package org.purl.beroca.crawler.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.purl.beroca.crawler.model.SortedMapOfLibraryRank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SortedMapOfLibraryRankTest {
	
	private SortedMapOfLibraryRank sortedMap;
	private Map<String, ArrayList<String>> testMap;
	private SortedMap<Integer, ArrayList<String>> testRank;

	@Before
	public void setup() {
		this.sortedMap = new SortedMapOfLibraryRank ();
		
		this.testMap = new HashMap<String, ArrayList<String>>();
		this.testMap.put( "url1", new ArrayList<String>(Arrays.asList("lib1", "lib2", "lib3")) );
		this.testMap.put( "url2", new ArrayList<String>(Arrays.asList("lib1", "lib4")) );
		this.testMap.put( "url3", new ArrayList<String>(Arrays.asList("lib1", "lib2", "lib5", "lib6")) );
		
		this.testRank = new TreeMap<Integer, ArrayList<String>>();
		this.testRank.put( 3, new ArrayList<String>(Arrays.asList("lib1")) );
		this.testRank.put( 2, new ArrayList<String>(Arrays.asList("lib2")) );
		this.testRank.put( 1, new ArrayList<String>(Arrays.asList("lib3", "lib4", "lib5", "lib6")) );
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
		assertEquals("updateRankOfString", Arrays.asList("url1"), sortedMap.getMapLibToURL().get("lib1"));
		assertEquals("updateRankOfString", Arrays.asList("url1"), sortedMap.getMapLibToURL().get("lib2"));
		assertEquals("updateRankOfString", Arrays.asList("url1"), sortedMap.getMapLibToURL().get("lib3"));

		sortedMap.updateRankOfString("url2", testMap.get("url2"));
		assertEquals("updateRankOfString", Arrays.asList("url1", "url2"), sortedMap.getMapLibToURL().get("lib1"));
		assertEquals("updateRankOfString", Arrays.asList("url2"), sortedMap.getMapLibToURL().get("lib4"));
		
		sortedMap.updateRankOfString("url3", testMap.get("url3"));
		assertEquals("updateRankOfString", Arrays.asList("url1", "url2", "url3"), sortedMap.getMapLibToURL().get("lib1"));
		assertEquals("updateRankOfString", Arrays.asList("url1", "url3"), sortedMap.getMapLibToURL().get("lib2"));
		assertEquals("updateRankOfString", Arrays.asList("url3"), sortedMap.getMapLibToURL().get("lib5"));
		assertEquals("updateRankOfString", Arrays.asList("url3"), sortedMap.getMapLibToURL().get("lib6"));
	}
	
	@Test
	public void testSortRankOfStringSet() {
		
		sortedMap.updateRankOfString("url1", testMap.get("url1"));
		sortedMap.updateRankOfString("url2", testMap.get("url2"));
		sortedMap.updateRankOfString("url3", testMap.get("url3"));
		sortedMap.sortRankOfStringSet();
		
		assertEquals("sortRankOfStringSet", Arrays.asList("lib1"), sortedMap.getMapRankToLib().get(3));
		assertEquals("sortRankOfStringSet", Arrays.asList("lib2"), sortedMap.getMapRankToLib().get(2));
		assertEquals("sortRankOfStringSet", 
				Arrays.asList("lib3", "lib4", "lib5", "lib6").size(), 
				sortedMap.getMapRankToLib().get(1).size() 
				);
	}
}
