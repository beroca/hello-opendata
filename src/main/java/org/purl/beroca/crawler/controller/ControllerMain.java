/**
 * 
 */
package org.purl.beroca.crawler.controller;

import java.net.URLEncoder;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.purl.beroca.crawler.model.RankedStringSet;
import org.purl.beroca.crawler.model.SortedMapOfLibraryRank;
import org.purl.beroca.crawler.model.SortedSetOfLibraryRank;

/**
 * @author beroca
 *
 */
public class ControllerMain {

	private final static Logger LOGGER = Logger.getLogger(ControllerMain.class.getName());
	private final static Level logLevel = Level.ALL;

	private static GoogleCrawlerExtended urlParser = new GoogleCrawlerExtended();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("# Google Search Parser - SortedMap + SortedSet");

		LOGGER.setLevel(ControllerMain.logLevel);
		for (Handler h : LOGGER.getParent().getHandlers()) {
			if (h instanceof ConsoleHandler) {
				h.setLevel(ControllerMain.logLevel);
			}
		}

		final String searchHits = "3";
		final String searchTerm = URLEncoder.encode("bootstrap", "UTF-8");
		final String query = "https://www.google.com/search?q=" + searchTerm + "&num=" + searchHits;

		System.out.println("Search Query: " + query);
		final String resultPage = urlParser.getSearchContent(query);
		final List<String> resultLinks = urlParser.parseLinks(resultPage);
		System.out.println("Results: " + resultLinks.size() + " URLs");

		RankedStringSet sortedMap = new SortedMapOfLibraryRank();
		System.out.println( "\n# Implementation: " + sortedMap.getClass().getName() );
		run(resultLinks, sortedMap);

		RankedStringSet sortedSet = new SortedSetOfLibraryRank();
		System.out.println( "\n# Implementation: " + sortedSet.getClass().getName() );
		run(resultLinks, sortedSet);
	}

	public static void run(final List<String> resultLinks, RankedStringSet implementaion) throws Exception {

		// Implementation dependent
		int countLibs = 0;
		int linkCount = 0;
		for (String link : resultLinks) {
			LOGGER.log(Level.INFO, "URL[" + (++linkCount) + "]: " + link);
			final String resultLinkPage = urlParser.getSearchContent(link);
			final List<String> resultLinkPageLibs = urlParser.parseLibraries(resultLinkPage);
			countLibs += implementaion.updateRankOfString(link, resultLinkPageLibs);
			System.out.println();
		}

		implementaion.sortRankOfStringSet();
		implementaion.printRankOfLibs();
		LOGGER.log(Level.INFO, "Raw Libs: " + countLibs);
	}
}
