package cc.suggestions.Autocomplete;

import java.util.List;
import java.util.Scanner;
import cc.suggestions.SuggestionHelper;
import cc.utils.config;

class TNode {
    char val1;
    boolean EndWrd;
    int schFrq; // Add a field for search frequency
    TNode[] chldrn = new TNode[26];

    TNode(char val1) {
        this.val1 = val1;
    }
}

public class CityAutoComplete {
    private static AutocompleteTrie citytrie;

    public static void init() {
        citytrie = new AutocompleteTrie();
        String[] cities = config.cities;
        for (String city : cities) {
            citytrie.insert(city);
        }
        citytrie.loadSrchFrqFrmFile(config.searchFrequencyFilePath);
    }

    public static String runCityAutoComplete(String input) {
        System.out.println("Autocompleting . . .");
        String inptWord = input.toLowerCase();

        List<String> suggestions = citytrie.autoComplete(inptWord);
        System.out.println("Autocomplete suggestions:");
        String finalCityName = SuggestionHelper.printAndSelectSuggestion(suggestions);

        return finalCityName;
    }

    public static void srchFrq(String cityName){
        // TODO move them to a central method
        citytrie.showSrchFrq(cityName); // Shows the search frequency for the selected city
        citytrie.increaseSrchFrq(cityName); // Increments the search frequency of the city selected
        citytrie.saveSrchFrqToFile(config.searchFrequencyFilePath);
    }

    public static void main(String[] args) {
        CityAutoComplete.init();

        try (Scanner scnr = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter a city name (or type 'exit' to quit): ");
                String prefix = scnr.nextLine();
                if (prefix.equals("exit")) {
                    citytrie.saveSrchFrqToFile(config.searchFrequencyFilePath);
                    break;
                }
                // Check if the input contains only alphabetic characters
                if (!prefix.matches("^[a-zA-Z]*$")) {
                    System.out.println("Invalid input. Please enter a valid alphabetic prefix.");
                    continue;
                }
                String finalSuggestion = CityAutoComplete.runCityAutoComplete(prefix);
                System.out.println("Final Suggestion : " + finalSuggestion);

            }
        }
    }
}
