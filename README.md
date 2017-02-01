# Web Crawler Exercise
**View on GitHub**: <https://github.com/beroca/hello-web-crawler.git>

The aim of this test assignment is to write a program in Java which counts top Javascript libraries used in web pages found on Google.

## Prerequisites
Please create an easily compilable project (e.g. maven/gradle/Intellij/Eclipse project).
Please do not spend more than 3 hours on the task. We expect incomplete solutions - the task is designed that way.

The command-line program should do the following:

1. Read a string (search term) from standard input
1. Get a Google result page for the search term
1. Extract main result links from the page
1. Download the respective pages and extract the names of Javascript libraries used in them
1. Print top 5 most used libraries to standard output

### Bonus steps
- write tests or think about the approach for testing your code
- think about / implement Java concurrency utilities to speed up certain tasks
- think about / implement de-duplication algorithms for the same Javascript libraries with different names

### Notes
- use whatever approach you think is the best and most efficient, you don’t need to create elaborate or complex parsing algorithms
- you can skip a step if it's too hard (and mock data for the next step)
- if something is not clear or can be done in multiple ways, describe why you chose your approach
- use a minimum of 3rd party libraries if possible, preferred is just JDK , however you can mention libraries with which a certain task would be more efficient and/or easier to write

# Progress Reports
This section lists relevant progress reports of the project.  
* [2017.02.01](./src/site/md/progress-report-20170201.md): Initial version.  
