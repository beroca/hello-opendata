// http://stackoverflow.com/questions/22084867/trouble-searching-google-using-the-search-api

package com.stackoverflow.questions._10257276;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleCustomSearch {

	public static void main(String[] args) throws Exception {

		// 1. Enable Google Custom Search API
		// https://console.developers.google.com/apis/api/customsearch/overview?project=beroca&duration=PT1H
		//
		// 2. Enable/Create Google Custom Search API KEY
		// http://stackoverflow.com/questions/26324921/google-api-keys-for-windows-phone-app
		// https://console.developers.google.com/apis/credentials?project=beroca
		//
		// Relevant: Using REST to Invoke the API
		// https://developers.google.com/custom-search/json-api/v1/using_rest
		//
		// Otherwise:
		// Exception in thread "main" java.io.IOException: 
		// Server returned HTTP response code: 400 for URL: 
		// https://www.googleapis.com/customsearch/v1?key=YOUR KEY&cx=013036536707430787589:_pqjad5hr1a&q=Android&alt=json
		//
		
	    String key="AIzaSyDIb6JnIXNIfAlIyRkrdziargeJX6GEqS4";
	    String qry="australian+open";
	    URL url = new URL(
	            "https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&q="+ qry + "&alt=json");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
	    BufferedReader br = new BufferedReader(new InputStreamReader(
	            (conn.getInputStream())));

	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {

	        if(output.contains("\"link\": \"")){                
	            String link = output.substring(
	            				output.indexOf("\"link\": \"") + ("\"link\": \"").length(), 
	            				output.indexOf("\",")
	            				);
	            System.out.println(link);       //Will print the google search links
	        }     
	    }
	    conn.disconnect();                              
	}
}
