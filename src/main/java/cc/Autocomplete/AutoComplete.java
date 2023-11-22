package cc.Autocomplete;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import cc.utils.config;

class TrieNode {
    char value;
    boolean isEndOfWord;
    int searchFrequency; // Add a field for search frequency
    TrieNode[] children = new TrieNode[26];

    TrieNode(char value) {
        this.value = value;
    }
}

public class AutoComplete {
    private static Trie trie = new Trie();
    public AutoComplete(){
        String[] cities = config.cities;
        for (String city : cities) {
            trie.insert(city);
        }
        trie.loadSearchFrequenciesFromFile(config.searchFrequencyFilePath);
    }

    public static String runAutoComplete(String city) {
        Scanner sc = new Scanner(System.in);
        List<String> suggestions = trie.autoComplete(city);
//        System.out.println("Autocomplete suggestions:");
        String suggestion = "";
        for (int i = 0; i < suggestions.size(); i++) {
            suggestion = suggestions.get(i);
            System.out.println((i + 1) + ". " + suggestion);
        }
        int selectedOption = 0;
        if (suggestions.size() > 1) {
            System.out.print("Enter the number for the suggestion you want to select: ");
            try {
                selectedOption = sc.nextInt();
                sc.nextLine(); // Consume the newline character1
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
                sc.nextLine(); // Consume the invalid input
            }
            if (selectedOption > 0 && selectedOption <= suggestions.size()) {
                suggestion = suggestions.get(selectedOption - 1);
                System.out.println("City Name " + suggestion + " is selected");
                trie.displaySearchFrequency(suggestion); // Display search frequency for the selected city
                trie.incrementSearchFrequency(suggestion); // Increment the search frequency for the selected city
            } else {
                System.out.println("Invalid selection. Try Again!");
            }
        } else {
            for (int i = 0; i < suggestions.size(); i++) {
                suggestion = suggestions.get(i);
                System.out.println("City Name " + suggestion + " is selected");
                trie.displaySearchFrequency(suggestion);
                trie.incrementSearchFrequency(suggestion);
            }
        }
        trie.saveSearchFrequenciesToFile("E:\\Semester-1\\ACC\\final_project\\compute_champions\\src\\main\\java\\cc\\SearchFrequency\\searchFrequencies.txt");

        return suggestion;
    }
    public static void main(String[] args) {
        String[] cities = config.cities;

        Trie trie = new Trie();
        // Add all major cities to the trie.
        for (String city : cities) {
            trie.insert(city);
        }

        trie.loadSearchFrequenciesFromFile(config.searchFrequencyFilePath);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter a city name (or type 'exit' to quit): ");
                String prefix = scanner.nextLine();
                if (prefix.equals("exit")) {
                    trie.saveSearchFrequenciesToFile(config.searchFrequencyFilePath);
                    break;
                }
                // Check if the input contains only alphabetic characters
                if (!prefix.matches("^[a-zA-Z]*$")) {
                    System.out.println("Invalid input. Please enter a valid alphabetic prefix.");
                    continue;
                }
                List<String> suggestions = trie.autoComplete(prefix);

                for (int i = 0; i < suggestions.size(); i++) {
                    String suggestion = suggestions.get(i);
                    System.out.println((i + 1) + ". " + suggestion);
                }
                if (!(suggestions.size()==0)) {
                    System.out.println("Autocomplete suggestions:");
                    if (suggestions.size() > 1) {
                        System.out.print("Enter the number for the suggestion you want to select: ");
                        int selectedOption;
                        try {
                            selectedOption = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Try again!");
                            scanner.nextLine(); // Consume the invalid input
                            continue;
                        }
                        if (selectedOption > 0 && selectedOption <= suggestions.size()) {
                            String selectedCity = suggestions.get(selectedOption - 1);
                            System.out.println("City Name " + selectedCity + " is selected");
                            trie.displaySearchFrequency(selectedCity); // Display search frequency for the selected city
                            trie.incrementSearchFrequency(selectedCity); // Increment the search frequency for the selected city
                        } else {
                            System.out.println("Invalid selection. Try Again!");
                        }
                    } else {
                        for (int i = 0; i < suggestions.size(); i++) {
                            String suggestion = suggestions.get(i);
                            System.out.println("City Name " + suggestion + " is selected");
                            trie.displaySearchFrequency(suggestion);
                            trie.incrementSearchFrequency(suggestion);
                        }
                    }
                }
            }
        }
    }
}

