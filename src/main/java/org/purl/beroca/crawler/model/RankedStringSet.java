package org.purl.beroca.crawler.model;

import java.util.List;

public interface RankedStringSet {

	int getRank(final String str);

	void sortRankOfStringSet();

	void printRankOfLibs();
		
	int updateRankOfString(final String url, final List<String> libs);

}