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

    private static List<File> getAllFiles(File folder) {

        List<File> files = new ArrayList<>();

        for (File directory : Objects.requireNonNull(folder.listFiles())) {
            if (directory.isDirectory()) {
                files.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
            }
        }

        // Return the list of all the files in the folder
        return files;
    }

    private static Set<String> findInFiles (String[] filenames, List<String> regexPatterns) {

        Set<String> results = new HashSet<>();

        for (String filename : filenames) {
            Set<String> currentFileResult = findInFile(filename, regexPatterns);
            results.addAll(currentFileResult);
        }

        return results;
    }

    private static Set<String> findInFiles (List<String> filenames, List<String> regexPatterns) {

        Set<String> results = new HashSet<>();

        for (String filename : filenames) {
            Set<String> currentFileResult = findInFile(filename, regexPatterns);
            results.addAll(currentFileResult);
        }

        return results;
    }

    private static Set<String> findInFile (String filename, List<String> regexPatterns) {
        File file = new File(filename);
        Set<String> results = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (String regex : regexPatterns) {
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line);

                    while (matcher.find()) {
                        results.add(matcher.group());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
    public static void main(String[] args) {
        System.out.println("**** PATTERN FINDER ****");

        Set<String> results = findInFile("filteredText.txt" , currencyAmountPattern);

        int serialNumber = 1;
        for (String result : results) {
            System.out.println(serialNumber + ". " + result);
            serialNumber++;
        }

    }
}
