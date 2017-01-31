package org.purl.beroca.sandbox.rejected;

//http://www.admfactory.com/how-to-parse-google-search-result-in-java/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleCrawlerMapAndSet {
	
	// Obtain a suitable logger.
    private final static Logger LOGGER = Logger.getLogger(GoogleCrawlerMapAndSet.class.getName());

	/**
	 * Method to convert the {@link InputStream} to {@link String}
	 * 
	 * @param is
	 *            the {@link InputStream} object
	 * @return the {@link String} object returned
	 */
	public static String getString(InputStream is) {
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		} finally {
			/** finally block to close the {@link BufferedReader} */
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * The method will return the search page result in a {@link String} object
	 * 
	 * @param path
	 *            the google search query
	 * @return the content as {@link String} object
	 * @throws Exception
	 *  	{@lnk SocketTimeoutException} can be thrown when reading from the returned input stream if the read 
	 *  	timeout expires before data is available for read.
	 *  	{@link IOException} - if an I/O error occurs while creating the input stream.
	 *  	{@link UnknownServiceException} - if the protocol does not support input.
	 */
	public static String getSearchContent(String path) throws Exception {
		
		final String agent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
		final URL url = new URL(path);
		final URLConnection connection = url.openConnection();
		/**
		 * User-Agent is mandatory otherwise Google will return HTTP response
		 * code: 403
		 */
		connection.setRequestProperty("User-Agent", agent);
		try {
			final InputStream stream = connection.getInputStream();
			return getString(stream);
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Parse all links
	 * 
	 * @param html
	 *            the page
	 * @return the list with all URLSs
	 * @throws Exception
	 */
	public static List<String> parseLinks(final String html) throws Exception {
		
		List<String> result = new ArrayList<String>();
		final String pattern1 = "<h3 class=\"r\"><a href=\"/url?q=";
		final String pattern2 = "\">";
		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m = p.matcher(html);

		int linkCount = 0;
		while (m.find()) {
			String domainName = m.group(0).trim();
			
			LOGGER.log(Level.FINER, "URL[" + (linkCount+1) + "]: " + domainName);
			/** remove the unwanted text */
			domainName = domainName.substring(domainName.indexOf("/url?q=") + 7);
			domainName = domainName.substring(0, domainName.indexOf("&amp;"));
			LOGGER.log(Level.FINE, "URL[" + (linkCount+1) + "]: " + domainName);

			result.add(domainName);
			linkCount++;
		}
		return result;
	}

	/**
	 * Parse all Javascript libraries
	 * 
	 * @param html
	 *            the page
	 * @param jsLibs
	 *            the global map with all the JS Libs and the count
	 * @return 
	 *            the number as {@link Integer} of JS Libs found
	 * @throws Exception
	 */
	public static int parseJSLibs(final String html, Map<String, Integer> jsLibs) throws Exception {
		int countLibs = 0;
		
		// Example pattern:
		// <script src="https://cdn.rawgit.com/pamelafox/a8b77c43f56da1753348/raw/slideshow.js"></script>
		//
		final String pattern1 = "<script src=\"";
		final String pattern2 = "\"";
		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m = p.matcher(html);

		while (m.find()) {
			String domainName = m.group(0).trim();
			
			LOGGER.log(Level.FINER, "Lib: " + domainName);

			/** remove the unwanted text */
			domainName = domainName.substring("<script src=\"".length());
			domainName = domainName.substring(0, domainName.length()-1);
			
			LOGGER.log(Level.FINE, "Lib: " + domainName);

			int rank = 0;
			if (jsLibs.containsKey(domainName)) {
				rank = jsLibs.get(domainName);
			}
			else {
				countLibs++;
			}
			jsLibs.put(domainName, ++rank);
			LOGGER.log(Level.INFO, "Rank[" + rank + "]: Lib: " + domainName);
		}
		return countLibs;
	}

	
	public static List<String> parseLibraries(final String resultLinkPage) throws Exception {

		List<String> listOfLibs = new ArrayList<String>();

		// Example pattern:
		// <script src="https://cdn.rawgit.com/pamelafox/a8b77c43f56da1753348/raw/slideshow.js"></script>
		//
		final String pattern1 = "<script src=\"";
		final String pattern2 = "\"";
		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m = p.matcher(resultLinkPage);

		int linkCount = 0;
		while (m.find()) {
			String domainName = m.group(0).trim();
			
			LOGGER.log(Level.FINER, "Lib[" + (linkCount+1) + "]: " + domainName);
			/** remove the unwanted text */
			domainName = domainName.substring("<script src=\"".length());
			domainName = domainName.substring(0, domainName.length()-1);
			LOGGER.log(Level.FINE, "Lib[" + (linkCount+1) + "]: " + domainName);

			listOfLibs.add(domainName);
			linkCount++;
		}
		return listOfLibs;
	}

	
	public static int updateRankOfLibs(Map<String, Integer> mapOfLibToRank, List<String> libs) {

		int libCount = 0;
		for( String lib : libs ) {
			int rank = 0;
			if (mapOfLibToRank.containsKey(lib)) {
				rank = mapOfLibToRank.get(lib);
			}
			else
			{
				libCount++;
			}
			mapOfLibToRank.put(lib, ++rank);
			LOGGER.log(Level.INFO, "Rank[" + rank + "]: Lib: " + lib);
		}
		return libCount; 
	}

	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Google Search Parser Tutorial - SortedMap + SortedSet");

		LOGGER.setLevel(Level.ALL);
		
		int countLibs = 0;
		Map<String, Integer> mapOfLibToRank = new HashMap<String, Integer>();
		SortedSet<Integer> setOfRankSorted;

		final String searchTerm = "bootstrap";
		final String searchHits = "20";
		final String query = "https://www.google.com/search?q=" + searchTerm + "&num=" + searchHits;
		LOGGER.log(Level.INFO, "Search Query: " + query);

		final String resultPage = getSearchContent(query);
		final List<String> resultLinks = parseLinks(resultPage);

		System.out.println("Results: " + resultLinks.size() + " URLs");

//		for (int i = 0; i < links.size(); i++) {
//			LOGGER.log(Level.INFO, "URL[" + (i+1) + "]: " + links.get(i));
//			final String resultPageLink = getSearchContent(links.get(i));
//			countLibs += parseJSLibs(resultPageLink, jsLibs);
//			System.out.println();
//		}
		
		int linkCount = 0;
		for (String link : resultLinks) {
			LOGGER.log(Level.INFO, "URL[" + (++linkCount) + "]: " + link);
			final String resultLinkPage = getSearchContent(link);
			final List<String> resultLinkPageLibs = parseLibraries(resultLinkPage);
			System.out.println();

			countLibs += updateRankOfLibs( mapOfLibToRank, resultLinkPageLibs );
			System.out.println();
		}
		
		setOfRankSorted = new TreeSet<Integer>(mapOfLibToRank.values());
		
		final int max = setOfRankSorted.last();
		final int min = setOfRankSorted.first();
		
		LOGGER.log(Level.INFO, "Max: " + max);
		LOGGER.log(Level.INFO, "Min: " + min);
		LOGGER.log(Level.INFO, "Ranked Libs: " + countLibs);
		{
			int libCount = 0;
			for (int rank = max; rank >= min; rank--) {
				for (Map.Entry<String, Integer> entry : mapOfLibToRank.entrySet()) {
					if (entry.getValue() == rank) {
						System.out.println(
								"Rank[" + entry.getValue() + "]: Lib[" + (++libCount) + "]: " + entry.getKey());
					}
				}
			}
		}
		
		LOGGER.log(Level.INFO, "Raw Libs: " + countLibs);
		// Java 7
		{ 
			int libCount = 0;
			for (Map.Entry<String, Integer> entry : mapOfLibToRank.entrySet()) {
				System.out.println("Rank[" + entry.getValue() + "]: Lib[" + (++libCount) + "]: " + entry.getKey());
			}
			// Java 8 only !!
			// jsLibRank.forEach( (k,v)->System.out.println("JS Lib: " + k + " Count: " + v) );
		}
	}
}