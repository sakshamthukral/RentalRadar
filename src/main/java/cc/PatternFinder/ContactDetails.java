package cc.PatternFinder;

import org.bouncycastle.util.StringList;

import java.util.Set;

public class ContactDetails {
    public String filename;
    public String listingUrl;
    public Set<String> emails;
    public Set<String> phoneNumbers;

    public ContactDetails (String filename, String listingUrl, Set<String> emails, Set<String> phoneNumbers) {
        this.filename = filename;
        this.listingUrl = listingUrl;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
    }

}
