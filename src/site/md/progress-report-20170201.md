# Progress Report 2017.02.01

This report describes the initial release of the Web Crawler exercise.

**Project Structure**

The project source code is organized as follows:

~~~~
.
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       ├── com
│   │       │   ├── admfactory
│   │       │   │   └── javaapps
│   │       │   │       └── GoogleCrawler.java
│   │       │   ├── journaldev
│   │       │   │   └── jsoup
│   │       │   │       └── GoogleSearchJava.java
│   │       │   ├── oracle
│   │       │   │   └── docs
│   │       │   │       └── javase
│   │       │   │           └── tutorial
│   │       │   │               └── URLReader.java
│   │       │   └── stackoverflow
│   │       │       └── questions
│   │       │           └── _10257276
│   │       │               └── GoogleCustomSearch.java
│   │       └── org
│   │           └── purl
│   │               └── beroca
│   │                   ├── crawler
│   │                   │   ├── controller
│   │                   │   │   ├── ControllerMain.java
│   │                   │   │   └── GoogleCrawlerExtended.java
│   │                   │   └── model
│   │                   │       ├── RankedStringSet.java
│   │                   │       ├── SortedMapOfLibraryRank.java
│   │                   │       └── SortedSetOfLibraryRank.java
│   │                   └── sandbox
│   │                       └── rejected
│   │                           └── GoogleCrawlerMapAndSet.java
│   ├── site
│   │   └── md
│   │       └── progress-report-20170201.md
│   └── test
│       └── java
│           └── org
│               └── purl
│                   └── beroca
│                       └── crawler
│                           ├── controller
│                           │   └── GoogleCrawlerExtendedTest.java
│                           └── model
│                               ├── SortedMapOfLibraryRankTest.java
│                               └── SortedSetOfLibraryRankTest.java
└── target
~~~~

The relevant sources to accomplish the goal of the exercise (i.e., they delimit the boundary of the possible application code paths) are located in the package:
* ./org/purl/beroca/crawler/

**Attribution to External Examples**

The other packages on the ./src/main/java/ branch of the project, corresponds to packages available on the Web that were evaluated to approach how to handle the initial connection to Google Search and the parsing of the relevant links on the response page. The code path of the main application of the project does *not* use them but they are included for credit attribution and the technical comparison of the different approaches.

The approach used in each of these packages could be summarize as follows:
* ./admfactory/javaapps/GoogleCrawler.java: 
  * Nice and simple. 
  * Uses only standard Java JDK. 
  * It relies on HTML tags to find the Google Search result links. That has pros and cons. 
  * The GoogleCrawlerExtended.java class that is part of the project application code path is an extension of this source, adding additional URL validation features and more granular exception handling. 
* ./journaldev/jsoup/GoogleSearchJava.java:
* ./oracle/docs/javase/tutorial/URLReader.java: 
* ./stackoverflow/questions/_10257276/GoogleCustomSearch.java: 

The package contains a *controller* and a *model* sub-packages. The structure is motivated on the MVC pattern even though the pattern applies only partially to this Web Crawler application.  

**Controller (Main Application)**

The main application entry point is:
* ./org/purl/beroca/crawler/controller/ControllerMain.java

Upon running the application, 3 input parameters are requested on the stdin:
* The keywords used for the Google Search - required
* The number of results to consider in the search response - optional: default: 10, max: 50
* The LOGGER level to display debugging information during run-time - optional: default: WARNING

The ControllerMain.java relies uses GoogleCrawlerExtended.java to: 
* Validate URL syntax
* Parse Google Search response page to get the result links
* Parse the page of each search result link to get the JavaScript libraries

Following the spirit of the MVC pattern, the *controller* classes, handle the interaction with the user (or client), and process the HTTP requests and responses of the application.

**Model**

The *model* package includes 2 classes to manage and structure the application data:
* SortedSetOfLibraryRank.java
* SortedMapOfLibraryRank.java

The 2 classes provides 2 different implementation approaches to accomplish the goal of the exercise. That is, to rank all the JavaScript libraries found in the pages that were part of the Google Search result.

The class SortedSetOfLibraryRank.java was the first approach to implement the ranking of the JavaScript libraries. It is fairly simple, somewhat brute force and O(n^2), so it would not scale for a very large number of them. It remains in the application solely for illustrative purposes.

The class SortedMapOfLibraryRank.java was the second approach to implement the ranking of the JavaScript libraries. It is more elaborated. It allows more features on the underlying data, and it keeps the libraries ranked upon insertion.

## Draft notes

- **TODO: Do not forget attribution of reused code. !!!**

- URL encode / decode
- org.apache.commons.validator.routines.UrlValidator

- LOOGER facility

- JUnit tests

- 2 implementations of the solution => Interface: RankedStringSet.java 

- Added PMD. A static Java source code analyzer.

- To keep JS-Lib ranked use SortedMap instead of Map

- Use of Google Web Search API (Deprecated).
- Use of Google Custom Search.
- Use of Java Standard Libraries

* Use of wget or curl
  * Google Search rejects programmatic access by default.
  * robots.txt of Google Search
  * Setting the right User Agent

- Encode invalid characters in the string used to build the search URL (e.g. spaces, punctuation, etc.).

- Error example

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

- Error example

```
Request URL:https://twitter.com/angularjs%3Flang%3Dde&amp;sa=U&amp;ved=0ahUKEwiEpPSlt-bRAhXLKMAKHX6FBwEQFghNMAg&amp;usg=AFQjCNG-_mNxVHE0Bi30_wqZZxSNW_WM-g
Request Method:GET
Status Code:404 
```

### Cardinality
Relationships between the 3 main entities relevant in the solution.
- URL, JavaScript library, Ranking value.
- URL, JS-lib: 1-to-many.
- JS-lib, Raking value: many-to-1

The relationship between a ranking value and a JavaScript library is "1-to-many".
That is, a given ranking value (1 occurrence, 2 occurrences, ..., n occurrences), can have *many* JavaScript libraries.

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
- Order of keys or values not needed.
- Order of Libraries not needed here.

`SortedMap<Integer, ArrayList<<String>>`
- Map of (Ranking positions => List of Libraries with that Ranking).
- Order of keys relevant!
- Order of values not needed.

### Improvements
- Complete JUnit test cases - Test Driven Development
  - Additional testing of the parsing methods.
  - Additional testing of the URL validation method.
- Investigate parsing of Google number of result. Expected: N, Actual: (N - x)
- Use of `<h3>` tag versus "/link:/" attribute to find Google Search result links.
- Add a Comparator implementation to handle ranking => order Map by value.
- Re-organize main() method.
- Remove dependency between *model* classes and ControllerMain.java class due to the LOGGER service.
- Consider override toString() method instead of custom "print" methods.


