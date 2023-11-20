package cc.Autocomplete;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
    public static void main(String[] args) {
        String[] cities = {"Toronto", "Montreal", "Calgary", "Ottawa", "Edmonton", "Winnipeg",
                "Vancouver", "Brampton", "Hamilton", "Surrey", "Quebec", "Halifax", "Laval",
                "London", "Markham", "Vaughan", "Gatineau", "Saskatoon", "Kitchener", "Longueuil",
                "Burnaby", "Windsor", "Regina", "Oakville", "Richmond", "Richmond", "Burlington",
                "Oshawa", "Sherbrooke", "Sudbury", "Abbotsford", "Coquitlam", "Barrie", "Saguenay",
                "Kelowna", "Guelph", "Whitby", "Cambridge", "Catharines", "Milton", "Langley", "Kingston",
                "Ajax", "Waterloo", "Terrebonne", "Saanich", "Delta", "Brantford", "Clarington", "Nanaimo",
                "Strathcona", "Pickering", "Lethbridge", "Kamloops", "Richelieu", "Niagara", "Cape Breton",
                "Chilliwack", "Victoria", "Brossard", "Newmarket", "Repentigny", "Peterborough",
                "Moncton", "Drummondville", "Caledon", "Airdrie", "Sarnia", "Granby", "Fredericton",
                "Aurora", "Mirabel", "Blainville", "Welland", "Belleville"};
        Trie trie = new Trie();
        // Add all major cities to the trie.
        for (String city : cities) {
            trie.insert(city);
        }

        trie.loadSearchFrequenciesFromFile("D:/Project/searchFrequencies2.txt");/////////////////////////////////

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter a city name (or type 'exit' to quit): ");
                String prefix = scanner.nextLine();
                if (prefix.equals("exit")) {
                    trie.saveSearchFrequenciesToFile("D:/Project/searchFrequencies2.txt");////////////////////////
                    break;
                }
                // Check if the input contains only alphabetic characters
                if (!prefix.matches("^[a-zA-Z]*$")) {
                    System.out.println("Invalid input. Please enter a valid alphabetic prefix.");
                    continue;
                }
                List<String> suggestions = trie.autoComplete(prefix);
                System.out.println("Autocomplete suggestions:");
                for (int i = 0; i < suggestions.size(); i++) {
                    String suggestion = suggestions.get(i);
                    System.out.println((i + 1) + ". " + suggestion);
                }
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

