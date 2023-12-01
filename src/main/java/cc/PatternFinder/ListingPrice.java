package cc.PatternFinder;

public class ListingPrice {
    public String filename;
    public String listingUrl;
    public int price;
    public String priceRange;

    public ListingPrice( String filename, String listingUrl) {
        this.filename = filename;
        this.listingUrl = listingUrl;
    }
}
