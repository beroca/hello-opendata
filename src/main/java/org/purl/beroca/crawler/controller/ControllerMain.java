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

	private static Logger LOGGER = Logger.getLogger(ControllerMain.class.getName());
	private final static Level logLevel = Level.WARNING;								// default value
	
	private static String searchHits = "5";											// default value
	private static String searchTerm = "bootstrap";									// default value

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

		final String query = "https://www.google.com/search?q=" + 
				URLEncoder.encode(searchTerm, "UTF-8") + 
				"&num=" + searchHits;
		System.out.println("# Search Query: " + query);

		final String resultPage = urlParser.getSearchContent(query);
		final List<String> resultLinks = urlParser.parseLinks(resultPage);
		System.out.println("# URLs found: " + resultLinks.size());

		// Implementation 1
		RankedStringSet sortedMap = new SortedMapOfLibraryRank();
		System.out.println( "\n# Implementation: " + sortedMap.getClass().getName() );
		run(resultLinks, sortedMap);

		// Implementation 2
		RankedStringSet sortedSet = new SortedSetOfLibraryRank();
		System.out.println( "\n# Implementation: " + sortedSet.getClass().getName() );
		run(resultLinks, sortedSet);
	}

	public static void run(final List<String> resultLinks, RankedStringSet implementaion) throws Exception {

		// Implementation dependent
		int countLibs = 0;
		for (String link : resultLinks) {
			final String resultLinkPage = urlParser.getSearchContent(link);
			final List<String> resultLinkPageLibs = urlParser.parseLibraries(resultLinkPage);
			countLibs += implementaion.updateRankOfString(link, resultLinkPageLibs);
			System.out.println();
		}
		System.out.println("# Libs found: " + countLibs + " in " + resultLinks.size() + " URLs");

		implementaion.sortRankOfStringSet();
		implementaion.printRankOfLibs();
	}
}
