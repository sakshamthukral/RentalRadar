package cc.PatternFinder;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFinder {
    public static List<String> emailPattern = List.of("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    public static List<String> phoneNoPattern = List.of( "\\d{3}-\\d{7}", "\\(\\d{3}\\)\\s\\d{3}-\\d{4}", "\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}", "\\+1\\s\\(\\d{3}\\)\\s\\d{3}-\\d{4}");
    public static List<String> currencyAmountPattern = List.of("\\$[0-9,]+(?:\\.[0-9]{2})?");
    public static List<String> urlPattern = List.of("https?://\\S+");
    public static List<String> datePattern = List.of("\\d{1,2}/\\d{1,2}/\\d{2,4}", "\\d{1,2}-\\d{1,2}-\\d{2,4}", "\\d{4}/\\d{1,2}/\\d{1,2}", "\\d{4}-\\d{1,2}-\\d{1,2}", "\\d{4}-\\d{2}-\\d{2}", "\\d{1,2}-(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4}");


    private static Set<String> findInFiles (List<String> filenames, List<String> regexPattern) {

        Set<String> results = new HashSet<>();

        for (String filename : filenames) {
            Set<String> currentFileResult = findInFile(filename, regexPattern);
            results.addAll(currentFileResult);
        }

        if (results.isEmpty()) {
            System.out.println("No results found.");
        }

        return results;
    }

    private static Set<String> findInFile (String filename, List<String> regexPattern) {
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
            e.printStackTrace();
        }

        listingUrl = listingUrl.replaceAll("<", "").replaceAll(">", "");
        printPatternResults(filename, results, listingUrl);

        return results;
    }

    private static String findListingUrl (String line) {
        String url = "";

        String regex = "<(https?://\\S+)>";

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

    private static void printPatternResults (String filename, Set<String> results, String listingUrl) {
        if (results.isEmpty()) {
            return;
        }
        System.out.printf("%s | URL: %s\n", filename, listingUrl);

        int serialNumber = 1;
        for (String result : results) {
            System.out.println("\t" + serialNumber + ". " + result);
            serialNumber++;
        }
    }

    public static void run (List<String> filenames) {
        System.out.println("\nFind patterns in the ranked pages:");
        boolean sessionFlag = true;
        int menuUserInput;
        while (sessionFlag) {
            menuUserInput = menuTakeUserInput();

            switch (menuUserInput) {
                case 1 -> {
                    System.out.println("Finding Emails...");
                    findInFiles(filenames, emailPattern);
                }
                case 2 -> {
                    System.out.println("Finding Phone numbers...");
                    findInFiles(filenames, phoneNoPattern);
                }
                case 3 -> {
                    System.out.println("Finding Currency Amounts...");
                    findInFiles(filenames, currencyAmountPattern);
                }
                case 4 -> {
                    System.out.println("Finding URLs...");
                    findInFiles(filenames, urlPattern);
                }
                case 5 -> {
                    System.out.println("Finding Dates...");
                    findInFiles(filenames, datePattern);
                }
                default -> sessionFlag = false;
            }
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

    public static void main(String[] args) {

        List<String> filenames = List.of("DB/parsed_liv.rent/windsor/page_1_listing_4.txt", "DB/parsed_liv.rent/windsor/page_1_listing_3.txt", "DB/parsed_liv.rent/windsor/page_1_listing_1.txt", "DB/parsed_liv.rent/windsor/page_1_listing_2.txt");

        run(filenames);
    }
}
