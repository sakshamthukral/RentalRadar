package cc.PatternFinder;

import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFinder {
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

    private static Set<String> findInFolder (File folder, String regex) {
//        List<String> results = new ArrayList<>();

        Set<String> results = new HashSet<>();

        List<File> files = getAllFiles(folder);

        for (File file : files) {
            if (file.isFile()) {
                Set<String> currentFileResult = findInFile(file, regex);
                results.addAll(currentFileResult);
            }
        }

        return results;
    }

    private static Set<String> findInFile (File file, String regex) {
        Set<String> results = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    results.add(matcher.group());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**** PATTERN FINDER ****");

        String folderPath = "testDirectory";
        File folder = new File(folderPath);
        String pattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

        while(true) {
            System.out.println("\nWhat do you want to find?");
            System.out.println("\n\t1. Emails\n\t2. Phone Numbers\n\t3. Amount\n\t4. URLs\n\t5. Dates\n\t(Enter 0 to exit)");

            System.out.print("\nSelect one option from above: ");
            int selectedOption = scanner.nextInt();

            scanner.nextLine();

            switch (selectedOption) {
                case 0: {
                    break;
                }
                case 1: {
                    Set<String> emails = findInFolder(folder, pattern );
                    System.out.println("\nFollowing emails found\n");
                    int serialNumber = 1;
                    for (String email : emails) {
                        System.out.println(serialNumber + ". " + email);
                        serialNumber++;
                    }

                    System.out.println("\n***** Email Finder End *****\n");
                }
            }

            if (selectedOption == 0) {
                System.out.println("Exiting Pattern Finder");
                break;
            }

        }

    }
}
