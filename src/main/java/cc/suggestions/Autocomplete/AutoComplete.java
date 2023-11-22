package cc.suggestions.Autocomplete;

import java.util.List;
import java.util.Scanner;

import cc.suggestions.SuggestionHelper;
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
    private static Trie trie;

    public static void init() {
        trie = new Trie();
        String[] cities = config.cities;
        for (String city : cities) {
            trie.insert(city);
        }
        trie.loadSearchFrequenciesFromFile(config.searchFrequencyFilePath);
    }

    public static String runAutoComplete(String input) {
        System.out.println("Autocompleting . . .");
        String inputWord = input.toLowerCase();

        List<String> suggestions = trie.autoComplete(inputWord);
        System.out.println("Autocomplete suggestions:");
        String finalCityName = SuggestionHelper.printAndSelectSuggestion(suggestions);

        return finalCityName;
    }

    public static void searchFrequency(String cityName){
        // TODO move them to a central method
        trie.displaySearchFrequency(cityName); // Display search frequency for the selected city
        trie.incrementSearchFrequency(cityName); // Increment the search frequency for the selected city
        trie.saveSearchFrequenciesToFile(config.searchFrequencyFilePath);
    }

    public static void main(String[] args) {
        AutoComplete.init();

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
                String finalSuggestion = AutoComplete.runAutoComplete(prefix);
                System.out.println("Final Suggestion : " + finalSuggestion);

            }
        }
    }
}
