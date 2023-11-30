package cc.PatternFinder;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFinder {
    public static final List<String> EMAIL_REGEX = List.of("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    public static final List<String> PHONE_REGEX = List.of( "\\d{3}-\\d{7}", "\\(\\d{3}\\)\\s\\d{3}-\\d{4}", "\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}", "\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}");
    public static final List<String> CURRENCY_REGEX = List.of("\\$[0-9,]+(?:\\.[0-9]{2})?");
    public static final List<String> URL_REGEX = List.of("https?://\\S+");
    public static final List<String> DATE_REGEX = List.of("\\d{1,2}/\\d{1,2}/\\d{2,4}", "\\d{1,2}-\\d{1,2}-\\d{2,4}", "\\d{4}/\\d{1,2}/\\d{1,2}", "\\d{4}-\\d{1,2}-\\d{1,2}", "\\d{4}-\\d{2}-\\d{2}", "\\d{1,2}-(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}");


    private static List<MatchResult> findInFiles (List<String> filenames, List<String> regexPattern) {

        List<MatchResult> matchResults = new ArrayList<>();

        for (String filename : filenames) {
            MatchResult currentFileResult = findInFile(filename, regexPattern);
            matchResults.add(currentFileResult);
        }

        return matchResults;
    }

    private static MatchResult findInFile (String filename, List<String> regexPattern) {
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
            System.out.println("Error reading the file " + file.getName() + ". Make sure that the file path is correct.");;
        }

        listingUrl = listingUrl.replaceAll("<", "").replaceAll(">", "");
        return new MatchResult(filename, listingUrl, results);
    }

    private static String findListingUrl (String line) {
        String url = "";

        String regex = "<<<(https?://\\S+)>>>";

        Pattern pattern = Pattern.compile(regex);
        Matcher urlMatcher =  pattern.matcher(line);

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

    private static void displayPatterns (List<MatchResult> matchResults) {

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

    public static void run (List<String> filenames) {
        System.out.println("\nFind patterns in the ranked pages:");
        boolean sessionFlag = true;
        int menuUserInput;
        while (sessionFlag) {
            menuUserInput = menuTakeUserInput();

            List<MatchResult> matchResults = new ArrayList<>();

            System.out.println();

            switch (menuUserInput) {
                case 1 -> {
                    System.out.println("Finding Emails...");
                    matchResults = findInFiles(filenames, EMAIL_REGEX);
                }
                case 2 -> {
                    System.out.println("Finding Phone numbers...");
                    matchResults = findInFiles(filenames, PHONE_REGEX);
                }
                case 3 -> {
                    System.out.println("Finding Currency Amounts...");
                    matchResults = findInFiles(filenames, CURRENCY_REGEX);
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
        System.out.println("Enter 1 : To find Emails");
        System.out.println("Enter 2 : To find Phone Numbers");
        System.out.println("Enter 3 : To find Currency Amounts");
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
}
