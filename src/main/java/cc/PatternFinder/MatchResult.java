package cc.PatternFinder;

import java.util.Set;

public class MatchResult {
    public String filename;
    public String listingUrl;
    public Set<String> results;

    public MatchResult (String filename, String listingUrl, Set<String> results) {
        this.filename = filename;
        this.listingUrl = listingUrl;
        this.results = results;
    }

}
