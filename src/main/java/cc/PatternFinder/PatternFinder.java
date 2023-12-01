package cc.PatternFinder;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFinder {
    public static final List<String> EMAIL_REGEX = List.of("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    public static final List<String> PHONE_REGEX = List.of("\\d{3}-\\d{7}", "\\(\\d{3}\\)\\s\\d{3}-\\d{4}", "\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}", "\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}");
    public static final List<String> CURRENCY_REGEX = List.of("\\$[0-9,]+(?:\\.[0-9]{2})?");
    public static final List<String> URL_REGEX = List.of("https?://\\S+");
    public static final List<String> DATE_REGEX = List.of("\\d{1,2}/\\d{1,2}/\\d{2,4}", "\\d{1,2}-\\d{1,2}-\\d{2,4}", "\\d{4}/\\d{1,2}/\\d{1,2}", "\\d{4}-\\d{1,2}-\\d{1,2}", "\\d{4}-\\d{2}-\\d{2}", "\\d{1,2}-(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}");


    private static List<MatchResult> findInFiles(List<String> filenames, List<String> regexPattern) {

        List<MatchResult> matchResults = new ArrayList<>();

        for (String filename : filenames) {
            MatchResult currentFileResult = findInFile(filename, regexPattern);
            matchResults.add(currentFileResult);
        }

        return matchResults;
    }

    private static MatchResult findInFile(String filename, List<String> regexPattern) {
        File file = new File(filename);
        Set<String> results = new HashSet<>();
        String listingUrl = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (String regex : regexPattern) {
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line);

                    while (matcher.find()) {
                        results.add(matcher.group());
                    }
                }
                listingUrl = findListingUrl(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file " + file.getName() + ". Make sure that the file path is correct.");
        }

        listingUrl = listingUrl.replaceAll("<", "").replaceAll(">", "");
        return new MatchResult(filename, listingUrl, results);
    }

    private static String findListingUrl(String line) {
        String url = "";

        String regex = "<<<(https?://\\S+)>>>";

        Pattern pattern = Pattern.compile(regex);
        Matcher urlMatcher = pattern.matcher(line);

        while (urlMatcher.find()) {
            url = (urlMatcher.group());
        }

        return url;
    }

    public static String findListingUrlInFile(String filename) {
        File file = new File(filename);
        String listingUrl = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                listingUrl = findListingUrl(line);
            }
        } catch (IOException e) {
            System.out.println("File read error");
        }

        listingUrl = listingUrl.replaceAll("<", "").replaceAll(">", "");

        return listingUrl;
    }

    private static void displayPatterns(List<MatchResult> matchResults) {

        boolean noMatchesFound = true;

        for (MatchResult response : matchResults) {
            if (!response.results.isEmpty()) {
                noMatchesFound = false;
                System.out.println(response.filename + ":");

                if (response.listingUrl.isEmpty()) {
                    System.out.println("\t( Listing URL: " + " <Not found> )");
                } else {
                    System.out.println("\t( Listing URL: " + response.listingUrl + " )");
                }

                int serialNumber = 1;
                for (String result : response.results) {
                    System.out.println("\t" + serialNumber + ". " + result);
                    serialNumber++;
                }
            }
        }

        if (noMatchesFound) {
            System.out.println("No matches found");
        }
    }

    private static void findCurrencyAmounts(List<String> filenames) {
        List<MatchResult> results = findInFiles(filenames, CURRENCY_REGEX);

        List<ListingPrice> listingPrices = new ArrayList<>();

        for (MatchResult result : results) {
            if (!result.results.isEmpty()) {
                ListingPrice currentListing = new ListingPrice(result.filename, result.listingUrl);
                findRequiredPrice(result.results, currentListing);
                listingPrices.add(currentListing);
            }
        }

        displayListingPrices(listingPrices);
    }

    public static void run(List<String> filenames) {
        System.out.println("\nFind patterns in the ranked pages:");
        boolean sessionFlag = true;
        int menuUserInput;
        while (sessionFlag) {
            menuUserInput = menuTakeUserInput();

            List<MatchResult> matchResults = new ArrayList<>();

            System.out.println();

            switch (menuUserInput) {
                case 1 -> {
                    System.out.println("Finding Listing Prices...");
                    findCurrencyAmounts(filenames);
                }
                case 2 -> {
                    System.out.println("Finding Phone numbers...");
                    matchResults = findInFiles(filenames, PHONE_REGEX);
                }
                case 3 -> {
                    System.out.println("Finding Emails...");
                    matchResults = findInFiles(filenames, EMAIL_REGEX);
                }
                case 4 -> {
                    System.out.println("Finding URLs...");
                    matchResults = findInFiles(filenames, URL_REGEX);
                }
                case 5 -> {
                    System.out.println("Finding Dates...");
                    matchResults = findInFiles(filenames, DATE_REGEX);
                }
                default -> sessionFlag = false;
            }

            if (sessionFlag)
                displayPatterns(matchResults);
        }

    }

    private static int menuTakeUserInput() {
        System.out.println();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 : To find Listing Prices");
        System.out.println("Enter 2 : To find Phone Numbers");
        System.out.println("Enter 3 : To find Emails");
        System.out.println("Enter 4 : To find URLs");
        System.out.println("Enter 5 : To find Dates");
        System.out.println("Enter 6 : To Return to previous menu");

        String input = sc.next();

        while (!input.matches("[1-6]")) {
            System.out.println("Please enter a valid input");
            input = sc.next();
        }

        return Integer.parseInt(input);
    }

    private static void displayListingPrices(List<ListingPrice> listingPrices) {
        if (listingPrices.isEmpty()) {
            System.out.println("No matches found");
            return;
        }

        System.out.println();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 : To sort listings in Ascending order");
        System.out.println("Enter 2 : To sort listings in Descending order");

        String input = sc.next();

        while (!input.matches("[1-2]")) {
            System.out.println("Please enter a valid input");
            input = sc.next();
        }

        if (input.equals("1")) {
            // Sorting in ascending order based on price
            listingPrices.sort(Comparator.comparingInt(lp -> lp.price));
        } else {
            // Sorting in descending order based on price
            listingPrices.sort(Comparator.comparingInt((ListingPrice lp) -> lp.price).reversed());
        }

        for (ListingPrice response : listingPrices) {
            System.out.println(response.filename + " | PRICE: $" + response.price + " | " + response.listingUrl);
        }

    }

    private static void findRequiredPrice(Set<String> amounts, ListingPrice currentListing) {
        // Parse currency amounts and store them in a TreeSet for sorting
        TreeSet<Integer> numericalAmounts = new TreeSet<>((price1, price2) -> Integer.compare(price2, price1));

        for (String amount : amounts) {
            int numericalValue = parseCurrencyAmount(amount);
            numericalAmounts.add(numericalValue);
        }

        // Retrieve the first year rent (index 1) from the sorted set
        int requiredPrice = 0;

        // Check the size of the set
        if (numericalAmounts.size() == 1) {
            // If only one item is present, return that value
            currentListing.price = numericalAmounts.first();
            return;
        }

        int index = 0;
        for (int value : numericalAmounts) {
            if (index == 1) {
                requiredPrice = value;
                break;
            }
            index++;
        }
        currentListing.price = requiredPrice;
    }

    private static int parseCurrencyAmount(String amount) {
        // Use regex to remove "$" sign and commas, and convert to an integer
        String cleanedAmount = amount.replaceAll("[^\\d]", "");
        return Integer.parseInt(cleanedAmount);
    }

    public static void main(String[] args) {
//        List<String> filenames = List.of("DB/description_rental.ca/toronto/page_3_listing_1.txt", "DB/description_rental.ca/toronto/page_1_listing_17.txt", "DB/description_rental.ca/toronto/page_1_listing_16.txt", "DB/description_rental.ca/toronto/page_3_listing_2.txt");
        List<String> filenames = List.of("DB/crawled_rental.ca/toronto/page_1_listing_1.html");

        run(filenames);
    }
}
