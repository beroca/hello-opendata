package org.purl.beroca.crawler.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.purl.beroca.crawler.controller.ControllerMain;

public class SortedMapOfLibraryRank implements RankedStringSet {

	private final static Logger LOGGER = Logger.getLogger(ControllerMain .class.getName());

	private Map<String, ArrayList<String>> mapLibToURL;
	private SortedMap<Integer, ArrayList<String>> mapRankToLib;

	public SortedMapOfLibraryRank() {
		this.mapLibToURL = new HashMap<String, ArrayList<String>>();
		this.mapRankToLib = new TreeMap<Integer, ArrayList<String>>();
	}

	/**
	 * @return the mapLibToURL
	 */
	public Map<String, ArrayList<String>> getMapLibToURL() {
		return mapLibToURL;
	}

	/**
	 * @return the mapRankToLib
	 */
	public SortedMap<Integer, ArrayList<String>> getMapRankToLib() {
		return mapRankToLib;
	}

	/* (non-Javadoc)
	 * @see org.purl.beroca.crawler.RankedStringSet#getRankOfJSLib(java.lang.String)
	 */
	public int getRank(String jsLib) {

		int rank = 0;

		if (mapLibToURL.containsKey(jsLib)) {

			rank = mapLibToURL.get(jsLib).size();
		}
		return rank;
	}

	/* (non-Javadoc)
	 * @see org.purl.beroca.crawler.RankedStringSet#rankLibs()
	 */
	public void sortRankOfStringSet() {

		for (String lib : mapLibToURL.keySet()) {

			final int rank = getRank(lib);

			if (mapRankToLib.containsKey(rank)) {
				mapRankToLib.get(rank).add(lib);

			} else {
				mapRankToLib.put(rank, new ArrayList<String>(Arrays.asList(lib)));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.purl.beroca.crawler.RankedStringSet#printRankOfLibs()
	 */
	public void printRankOfLibs() {

		final int totalLibs = mapRankToLib.size();
		System.out.println("Number of Libs found: " + totalLibs);

		if(totalLibs <= 0) {
			return;
		}
		
		final int max = mapRankToLib.lastKey();
		final int min = mapRankToLib.firstKey();

		LOGGER.log(Level.INFO, "Max Rank: " + max);
		LOGGER.log(Level.INFO, "Min Rank: " + min);
			
		for (int libCount = 0, rank = max; rank >= min; rank--) {

			ArrayList<String> listOfLibs = mapRankToLib.get(rank);
			for (String lib : listOfLibs) {

				System.out.println("Rank[" + rank + "]: Lib[" + (++libCount) + "]: " + lib);
				if (LOGGER.getLevel().intValue() <= Level.INFO.intValue()) {

					ArrayList<String> listOfURLs = mapLibToURL.get(lib);
					for (String url : listOfURLs) {
						System.out.println("- URLs: " + url);
					}
				}
			}
		}

		if ( LOGGER.getLevel().intValue() <= Level.INFO.intValue() )
		{
			System.out.println("Raw Libs (not ranked)");
			// Print libs as they are stored

			int libCount = 0;
			for (Map.Entry<String, ArrayList<String>> entry : mapLibToURL.entrySet()) {
				System.out
						.println("Rank[" + entry.getValue().size() + "]: Lib[" + (++libCount) + "]: " + entry.getKey());
			}
			// Java 8 only !!
			// jsLibRank.forEach( (k,v)->System.out.println("JS Lib: " + k + "
			// Count: " + v) );
		}
	}


	/* (non-Javadoc)
	 * @see org.purl.beroca.crawler.RankedStringSet#updateListOfLibraryURLs(java.lang.String, java.util.List)
	 */
	public int updateRankOfString(final String link, final List<String> listOfLibs) {

		for (String lib : listOfLibs) {
			
			if (mapLibToURL.containsKey(lib)) {
				// Update Lib to URLs mappings
				mapLibToURL.get(lib).add(link);
			} else {
				// Create Lib to URL mapping
				mapLibToURL.put(lib, new ArrayList<String>(Arrays.asList(link)));
			}

			final int rank = getRank(lib);
			LOGGER.log(Level.INFO, "Rank[" + rank + "]: Libs: " + lib + " URLs: " + mapLibToURL.get(lib).toString());
		}
		return 0;
	}
}
