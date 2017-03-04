package org.purl.beroca.crawler.controller;

//http://www.admfactory.com/how-to-parse-google-search-result-in-java/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;



public class GoogleCrawlerExtended {

	// Obtain a suitable logger.
	private static Logger LOGGER = Logger.getLogger(ControllerMain.class.getName());

	public static enum PATTERN_TYPE {
		PATTERN_GOOGLE_RESULT, PATTERN_JAVASCRIPT_LIBRARY
	};

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
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			/** finally block to close the {@link BufferedReader} */
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
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
	 *             {@lnk SocketTimeoutException} can be thrown when reading from
	 *             the returned input stream if the read timeout expires before
	 *             data is available for read. {@link IOException} - if an I/O
	 *             error occurs while creating the input stream.
	 *             {@link UnknownServiceException} - if the protocol does not
	 *             support input.
	 */
	public String getSearchContent(final String path) throws Exception {

		// Very DEFENSIVE implementation
		
		String pageContent = ""; // <===== return String =====

		if( urlIsValid(path) ) 
		{
			URL url = null;
			try {
				url = new URL(path);
				
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getClass().getSimpleName() + ": " + e.getMessage()); // , e);
				System.out.println("# Skipping URL: " + path + " => FAILED: new URL(path)");
			}

			URLConnection connection = null;
			try {
				connection = url.openConnection();

				// User-Agent is mandatory otherwise Google will return:
				// HTTP response code: 403
				connection.setRequestProperty(
						"User-Agent", 
						"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
						);
				
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getClass().getSimpleName() + ": " + e.getMessage()); // , e);
				System.out.println("# Skipping URL: " + path + " => FAILED url.openConnection()");
			}

			InputStream stream = null;
			try {
				stream = connection.getInputStream();
				
				pageContent = getString(stream); // <===== INTENDED CODE PATH =====
				
				Pattern pattern1 = Pattern.compile("<span[^>]*>IDPs: </span>[^<]*<span[^\\d]*[\\d,]+");
				Pattern pattern2 = Pattern.compile("[\\d,]+");
				Matcher m = pattern1.matcher(pageContent);
				if ( m.find() ) { 
					
					String result = m.group();
				    m = pattern2.matcher(result);					
				    m.find();
				}
				
				String domainName = m.group(0).trim().replace(",", "");
				int ipds = Integer.parseInt(domainName);
				System.out.println("Endlich: " + domainName);
				
//				System.out.println(pageContent);
				
//				String[] str1 = pageContent.split("IDPs: ");
//				String[] str2 = str1[1].split("</div>");
//				System.out.println(str2[0]);
				
				
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getClass().getSimpleName() + ": " + e.getMessage()); // , e);
				e.printStackTrace();
				System.out.println("# Skipping URL: " + path + " => FAILED connection.getInputStream()");
			}
		}
		else // <===== Invalid URL ===== 
		{
			System.out.println("# Skipping URL: " + path + " => FAILED: urlIsValid()");
		}

		return pageContent;
	}

	/**
	 * Parse all links
	 * 
	 * @param html
	 *            the page
	 * @return the list with all URLSs
	 * @throws Exception
	 */
	public List<String> parseLinks(final String html) throws Exception {

		List<String> result = new ArrayList<String>();

		// Example pattern of Google Search Result
		// <h3 class="r"><a
		// href="/url?q=http://getbootstrap.com/&amp;sa=U&amp;ved=0ahUKEwiW9ZjFzuzRAhVIXhQKHdM9CvsQFggVMAA&amp;usg=AFQjCNHYUu9OcB7laJwyCqUFrt06xL-OfA">
		//
		final String pattern1 = "<h3 class=\"r\"><a href=\"/url?q=";
		final String pattern2 = "\">";
		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m = p.matcher(html);

		int linkCount = 0;
		while (m.find()) {
			String domainName = m.group(0).trim();

			LOGGER.log(Level.FINER, "URL[" + (linkCount + 1) + "]: " + domainName);
			// LOGGER.finer("URL[" + (linkCount+1) + "]: " + domainName);
			/** remove the unwanted text */
			domainName = domainName.substring(domainName.indexOf("/url?q=") + 7);
			domainName = domainName.substring(0, domainName.indexOf("&amp;"));
			LOGGER.log(Level.FINE, "URL[" + (linkCount + 1) + "]: " + domainName);

			result.add(domainName);
			System.out.println("URL[" + (++linkCount) + "]: " + domainName);
		}
		return result;
	}

	/**
	 * Parse all Javascript libraries
	 * 
	 * @param html
	 *            the page
	 * @param url
	 * @param jsLibs
	 *            the global map with all the JS Libs and the count
	 * @return the number as {@link Integer} of JS Libs found
	 * @throws Exception
	 */
	public List<String> parseLibraries(String resultPageOfLink) throws Exception {

		List<String> listOfLibs = new ArrayList<String>();

		// Example pattern:
		// <script
		// src="https://cdn.rawgit.com/pamelafox/a8b77c43f56da1753348/raw/slideshow.js"></script>
		//
		final String pattern1 = "<script src=\"";
		final String pattern2 = "\"";
		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m = p.matcher(resultPageOfLink);

		int libCount = 0;
		while (m.find()) {
			String domainName = m.group(0).trim();

			LOGGER.log(Level.FINER, "Lib[" + libCount + "]: " + domainName);
			/** remove the unwanted text */
			domainName = domainName.substring("<script src=\"".length());
			domainName = domainName.substring(0, domainName.length() - 1);
			LOGGER.log(Level.FINE, "Lib[" + libCount + "]: " + domainName);

			listOfLibs.add(domainName);
			libCount++;
		}
		return listOfLibs;
	}
	
	private boolean urlIsValid(String url) {
		
		LOGGER.log(Level.INFO, "Validate URL: " + url);

		UrlValidator urlValidator = new UrlValidator();
		if (urlValidator.isValid(url)) 
		{
			return true;
		}
		else 
		{
			String urlDecoded = null;
			try {
				urlDecoded = URLDecoder.decode(url, "UTF-8");
			} 
			catch (UnsupportedEncodingException e) 
			{
				LOGGER.log(Level.SEVERE, e.getClass().getSimpleName() + ": " + e.getMessage()); // , e);
				System.out.println("# Skipping URL: " + url + " => FAILED URLDecoder.decode()");
			}
			
			if( urlValidator.isValid(urlDecoded)) {
				return true;
			}
			else
			{
				LOGGER.log(Level.SEVERE, "Skipping URL: " + urlDecoded + " => FAILED UrlValidator.isValid()");
				System.out.println("# Skipping URL: " + urlDecoded + " => FAILED UrlValidator.isValid()");
				return false;
			}
		}
	}
}