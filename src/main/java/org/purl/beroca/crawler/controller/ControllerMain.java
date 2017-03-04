/**
 * 
 */
package org.purl.beroca.crawler.controller;

import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;
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
	private static Level logLevel = Level.WARNING;									// default value
	
	private static String searchHits = "10";											// default value
	private static String searchTerm = "bootstrap";									// default value
	private static GoogleCrawlerExtended urlParser = new GoogleCrawlerExtended();

	private static void getMainOptions() {

		Scanner in = null;
		try {
			in = new Scanner(System.in);

			System.out.println("# Enter search keyword(s): ");
			searchTerm = in.nextLine().trim();

			System.out.println("# Enter number of search results [1, 50]: ");
			int intHits = in.nextInt();
			searchHits = ( intHits > 0 && intHits <= 50 ? String.valueOf(intHits) : "1" );
			
			System.out.println("# Enter LOGGER Level: (default: 3 => WARNING");
			System.out.println("[ 1 => ALL, 2 => INFO, 3 => WARNING ]:");
			int intLogLevel = in.nextInt();
			switch (intLogLevel) {
			case 1:
				ControllerMain.logLevel = Level.ALL;
				break;
			case 2:
				ControllerMain.logLevel = Level.INFO;
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid level: Using default => WARNING");
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				in.close();
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("# Google Search Parser - SortedMap + SortedSet");

		// getMainOptions();
		
		// Configure LOGGER messages
		LOGGER.setLevel(ControllerMain.logLevel);
		for (Handler h : LOGGER.getParent().getHandlers()) {
			if (h instanceof ConsoleHandler) {
				h.setLevel(ControllerMain.logLevel);
			}
		}
		
		// Send query request to Google Search and process response
		// https://www.cia.gov/library/publications/the-world-factbook/geos/af.html
		final String query = "https://www.cia.gov/library/publications/the-world-factbook/geos/af.html" + 
				URLEncoder.encode("", "UTF-8");
		// System.out.println("# Search Query: " + query);

		final String resultPage = urlParser.getSearchContent(query);
		final List<String> resultLinks = urlParser.parseLinks(resultPage);
		System.out.println("# URLs found: " + resultLinks.size());

		// Implementation 1
		RankedStringSet sortedMap = new SortedMapOfLibraryRank();
		System.out.println( "\n# Implementation: " + sortedMap.getClass().getSimpleName() );
		runImplemention(resultLinks, sortedMap);

		// Implementation 2
		RankedStringSet sortedSet = new SortedSetOfLibraryRank();
		System.out.println( "\n# Implementation: " + sortedSet.getClass().getSimpleName() );
		runImplemention(resultLinks, sortedSet);
	}

	private static void runImplemention(final List<String> resultLinks, RankedStringSet implementaion)
			throws Exception {

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
