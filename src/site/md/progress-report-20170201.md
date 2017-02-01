# Web Crawler Report

TODO: Do not forget attribution of reused code.

URL encode / decode

JUnit tests

org.apache.commons.validator.routines.UrlValidator

## Challenges and Design Choices

- Added PMD. A static Java source code analyzer.

- To keep JS-Lib ranked use SortedMap instead of Map

- Use of Google Web Search API (Deprecated).
- Use of Google Custom Search.
- Use of Java Standard Libraries

- Use of wget or curl
-- Google Search rejects programmatic access by default.
-- robots.txt of Google Search
-- Setting the right User Agent

- Encode invalid characters in the string used to build the search URL (e.g. spaces, punctuation, etc.).
- Use of `<h3>` tag versus "link:" attribute to find Google Search result links.

-- Error example
```
Jan 29, 2017 4:37:20 AM org.purl.beroca.sandbox.rejected.GoogleCrawlerMapAndSet parseLinks
INFO: [9] https://twitter.com/angularjs%3Flang%3Dde
...
INFO: [9] https://twitter.com/angularjs%3Flang%3Dde
Exception in thread "main" java.io.FileNotFoundException: https://twitter.com/angularjs%3Flang%3Dde
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1872)
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1474)
	at sun.net.www.protocol.https.HttpsURLConnectionImpl.getInputStream(HttpsURLConnectionImpl.java:254)
	at org.purl.beroca.sandbox.rejected.GoogleCrawlerMapAndSet.getSearchContent(GoogleCrawler.java:77)
	at org.purl.beroca.sandbox.rejected.GoogleCrawlerMapAndSet.main(GoogleCrawler.java:187)
```

-- Error example
```
Request URL:https://twitter.com/angularjs%3Flang%3Dde&amp;sa=U&amp;ved=0ahUKEwiEpPSlt-bRAhXLKMAKHX6FBwEQFghNMAg&amp;usg=AFQjCNG-_mNxVHE0Bi30_wqZZxSNW_WM-g
Request Method:GET
Status Code:404 
```
### Cardinality
Relationships between the 3 main entities relevant in the solution.
- URL, Javascript library, Ranking value.
- URL, Js-lib: 1-to-many.
- Js-lib, Raking value: many-to-1

The relationship between a ranking value and a Javascript library is "1-to-many".
That is, a given ranking value (1 occurrance, 2 occurrences, ..., n occurrencies), can have *many* Javascipt libraries.

Keeping the Js-libs always sorted or not.

### Approach 1
Using a Map and a Set.

`HashMap<String, Integer>`
- Map of (Library => Raking position)
- Order of keys, not relevant.
- Order of values, yes.

`SortedSet<Integer>`
- Set of all Ranking positions *in order*.
- Order of elements, relevant.

Iterating through the SortedSet and Iterating through the HashMap:
- For each element in the SortedSet (ranking value), all Js-Libs can be displayed.

It works but it requires O(n^2)

### Approach 2
Using 2 Maps.

`HaspMap<String, ArrayList<<String>>`
- Map of (Libraries => List of URLs where they appear). 
- Order of Libraries not needed.

`SortedMap<Integer, ArrayList<<String>>`
- Map of (Ranking positions => List of Libraries with that Ranking).
- Order of Libraries relevant!

### Improvements
- Test Driven Development
