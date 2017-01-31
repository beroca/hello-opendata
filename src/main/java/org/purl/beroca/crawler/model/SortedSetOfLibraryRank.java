/**
 * 
 */
package org.purl.beroca.crawler.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.purl.beroca.crawler.controller.ControllerMain;

/**
 * @author beroca
 *
 */
public class SortedSetOfLibraryRank implements RankedStringSet {

	private final static Logger LOGGER = Logger.getLogger(ControllerMain.class.getName());

	private Map<String, Integer> mapOfLibToRank;
	private SortedSet<Integer> setOfRankSorted;

	/**
	 * 
	 */
	public SortedSetOfLibraryRank() {

		this.mapOfLibToRank = new HashMap<String, Integer>();
		this.setOfRankSorted = null;
	}

	/**
	 * @return the mapOfLibToRank
	 */
	public Map<String, Integer> getMapOfLibToRank() {
		return mapOfLibToRank;
	}

	public int getRank(final String lib) {

		int rank = 0;

		if (mapOfLibToRank.containsKey(lib)) {

			rank = mapOfLibToRank.get(lib);
		}
		return rank;
	}

	public void printRankOfLibs() {

		final int totalLibs = mapOfLibToRank.size();
		System.out.println("Number of Libs found: " + totalLibs);

		if (totalLibs <= 0) {
			return;
		}

		final int max = setOfRankSorted.last();
		final int min = setOfRankSorted.first();

		LOGGER.log(Level.INFO, "Max: " + max);
		LOGGER.log(Level.INFO, "Min: " + min);

		for (int libCount = 0, rank = max; rank >= min; rank--) {
			for (Map.Entry<String, Integer> entry : mapOfLibToRank.entrySet()) {
				if (entry.getValue() == rank) {
					System.out.println("Rank[" + entry.getValue() + "]: Lib[" + (++libCount) + "]: " + entry.getKey());
				}
			}
		}

		if (LOGGER.getLevel().intValue() <= Level.INFO.intValue()) {
			// Print libs as they are stored
			System.out.println("Raw Libs found (not ranked)");

			// Java 7
			int libCount = 0;
			for (Map.Entry<String, Integer> entry : mapOfLibToRank.entrySet()) {
				System.out.println("Rank[" + entry.getValue() + "]: Lib[" + (++libCount) + "]: " + entry.getKey());
			}

			// Java 8 only !!
			// jsLibRank.forEach( (k,v)->System.out.println("JS Lib: " + k + "
			// Count: " + v) );
		}
	}

	/**
	 * @return the setOfRankSorted
	 */
	public SortedSet<Integer> getSetOfRankSorted() {
		return setOfRankSorted;
	}

	public void sortRankOfStringSet() {

		setOfRankSorted = new TreeSet<Integer>(mapOfLibToRank.values());
	}

	public int updateRankOfString(final String link, final List<String> libs) {

		int libCount = 0;
		for (String lib : libs) {
			int rank = 0;
			if (mapOfLibToRank.containsKey(lib)) {
				rank = mapOfLibToRank.get(lib);
			} else {
				libCount++;
			}
			mapOfLibToRank.put(lib, ++rank);
			LOGGER.log(Level.INFO, "Rank[" + rank + "]: Lib: " + lib);
		}
		return libCount;
	}
}
