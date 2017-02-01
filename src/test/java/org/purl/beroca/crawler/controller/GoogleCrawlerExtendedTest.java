package org.purl.beroca.crawler.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GoogleCrawlerExtendedTest {
	
	GoogleCrawlerExtended crawlerActual;

	@Before
	public void setUp() throws Exception {

		crawlerActual = new GoogleCrawlerExtended();
	}

	@Test
	public void testGetString() {
		assertTrue("", true );
	}

	@Test
	public void testGetSearchContent() throws Exception {

		// URL1 => invalid => encode => valid
		final String URL1_INVALID_BEFORE_ENCODING = 
				"https://www.google.com/search?q=bootstrap !\"§$%%&// ;:_Ä'  javascript&num=5"; 
		final String URL1_VALID_AFTER_ENCODING = 
				"https://www.google.com/search?q=bootstrap+%21%22%C2%A7%24%25%25%26%2F%2F+%3B%3A_%C3%84%27++javascript&num=5";
		
		assertEquals("URL1_INVALID", "", crawlerActual.getSearchContent(URL1_INVALID_BEFORE_ENCODING));
		assertNotEquals("URL1_VALID", "", crawlerActual.getSearchContent(URL1_VALID_AFTER_ENCODING));
		
		// URL2 => invalid => decode => valid
		final String URL2_INVALID_BEFORE_DECODING = 
				"https://www.youtube.com/watch%3Fv%3DDe5VlKfmvXg";
		final String URL2_VALID_AFTER_DECODING = 
				"https://www.youtube.com/watch?v=De5VlKfmvXg";
		
		assertEquals("URL2_INVALID", "", crawlerActual.getSearchContent(URL2_INVALID_BEFORE_DECODING));
		assertNotEquals("URL2_VALID", "", crawlerActual.getSearchContent(URL2_VALID_AFTER_DECODING));
		
		// URL3 => valid => throws javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
		final String URL3_SSL_EXCEPTION = 
				"https://sourceforge.net/p/jeebase/svn/6/tree/proj/trunk/jeebase/jeebase-web/src/main/webapp/resources/js/openjs/bootstrap/bootstrap3.3.5/fonts/glyphicons-halflings-regular.woff?force=True";

		// assertNotEquals("URL3_SSL_EXCEPTION", "", crawlerActual.getSearchContent(URL3_SSL_EXCEPTION));
	}

	@Test
	public void testParseLinks() {
		assertTrue("", true );
	}

	@Test
	public void testParseLibraries() {
		assertTrue("", true );
	}

}
