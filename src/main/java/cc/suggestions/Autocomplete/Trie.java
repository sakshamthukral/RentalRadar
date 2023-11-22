package cc.suggestions.Autocomplete;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode(' ');
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toLowerCase().toCharArray()) {
            if (c == ' ') {
                continue; // Skip spaces
            }
            int index = c - 'a';
            if (index < 0) {
                index += 26;
            }
            if (current.children[index] == null) {
                current.children[index] = new TrieNode(c);
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
        current.searchFrequency++; // Increment search frequency
    }

    public void displaySearchFrequency(String word) {
        TrieNode node = findNode(word.toLowerCase());
        if (node != null) {
            System.out.println("City Name: " + word + ", Search Frequency: " + node.searchFrequency);
        } else {
            System.out.println("No Search Frequency for this city \""+word+"\"");
        }
    }
    public void incrementSearchFrequency(String word) {
        TrieNode node = findNode(word.toLowerCase());
        if (node != null) {
            node.searchFrequency++;
        } else {
            System.out.println("No Search Frequency for this city \""+word+"\"");
        }
    }
    public List<String> autoComplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        TrieNode node = findNode(prefix.toLowerCase());
        collectWords(node, new StringBuilder(prefix.toLowerCase()), suggestions);
        return suggestions;
    }

    public TrieNode findNode(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                return null;
            }
            current = current.children[index];
        }
        return current;
    }

    public void collectWords(TrieNode node, StringBuilder currentWord, List<String> suggestions) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord) {
            suggestions.add(currentWord.toString());
        }

        for (char c = 'a'; c <= 'z'; c++) {
            int index = c - 'a';
            currentWord.append(c);
            collectWords(node.children[index], currentWord, suggestions);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }
        public void saveSearchFrequenciesToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            saveSearchFrequencies(root, new StringBuilder(), writer);
        } catch (IOException e) {
            e.printStackTrace(); // TODO replace with Error Handling
        }
    }

    private void saveSearchFrequencies(TrieNode node, StringBuilder currentWord, PrintWriter writer) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord) {
            writer.println(currentWord.toString() + " " + node.searchFrequency);
        }

        for (char c = 'a'; c <= 'z'; c++) {
            int index = c - 'a';
            currentWord.append(c);
            saveSearchFrequencies(node.children[index], currentWord, writer);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }



    public void loadSearchFrequenciesFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String word = parts[0];
                    int frequency = Integer.parseInt(parts[1]);
                    TrieNode node = findNode(word.toLowerCase());
                    if (node != null) {
                        node.searchFrequency = frequency;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
