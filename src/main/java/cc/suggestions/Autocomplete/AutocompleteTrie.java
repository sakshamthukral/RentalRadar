package cc.suggestions.Autocomplete;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AutocompleteTrie {
    private final TNode root;
    // When the program starts (in the init method of CityAutoComplete), 
    // It initializes the trie and loads the search frequencies from the file.
    public AutocompleteTrie() {
        root = new TNode(' ');
    }
     // The insert method inserts a word into the trie. 
    public void insert(String word) {
        TNode current = root;
        for (char c1 : word.toLowerCase().toCharArray()) {
            if (c1 == ' ') {
                continue; // Skip spaces
            }
            int index = c1 - 'a';
            if (index < 0) {
                index += 26;
            }
            if (current.chldrn[index] == null) {
                current.chldrn[index] = new TNode(c1);
            }
            current = current.chldrn[index];
        }
        current.EndWrd = true;
        current.schFrq++; // increase the counter of  search frequency
    }
    // To display the search frequencies
    public void showSrchFrq(String word) {
        TNode node = findNode(word.toLowerCase());
        if (node != null) {
            System.out.println("| City Name \t| Search Frequency |");
            System.out.printf("| %s \t\t|\t %s \t\t|\n",word, node.schFrq);
        } else {
            System.out.println("No Search Frequency for this city \""+word+"\"");
        }
    }
    // When search frequencies are displayed using showSrchFrq or automatically incremented using increaseSrchFrq, the values in the trie are updated. 
    // Subsequently, these changes can be saved back to the file using saveSrchFrqToFile.
    public void increaseSrchFrq(String word) {
        TNode node = findNode(word.toLowerCase());
        if (node != null) {
            node.schFrq++;
        } else {
            System.out.println("No Search Frequency for this city \""+word+"\"");
        }
    }
    // Below method returns a list of autocomplete suggestions for a given prefix.
    public List<String> autoComplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        TNode node = findNode(prefix.toLowerCase());
        getWords(node, new StringBuilder(prefix.toLowerCase()), suggestions);
        return suggestions;
    }
    // Below findNode method traverses the trie based on the characters in the   given prefix.
    // After this it returns the node corresponding to the last character in the prefix.
    public TNode findNode(String prefix) {
        TNode current = root;
        for (char c1 : prefix.toCharArray()) {
            int index = c1 - 'a';
            if (current.chldrn[index] == null) {
                return null;
            }
            current = current.chldrn[index];
        }
        return current;
    }
    // The getWords recursively explore the trie, appending characters to currentWord and 
    // when it reaches the end of a word, it adds the current word to the list of suggestions.
    // It also uses backtracking to collect suggestions.
    public void getWords(TNode node, StringBuilder currentWord, List<String> suggestions) {
        if (node == null) {
            return;
        }

        if (node.EndWrd) {
            suggestions.add(currentWord.toString());
        }

        for (char c1 = 'a'; c1 <= 'z'; c1++) {
            int index = c1 - 'a';
            currentWord.append(c1);
            getWords(node.chldrn[index], currentWord, suggestions);
            currentWord.deleteCharAt(currentWord.length() - 1);
        // Backtracking: After exploring a child node and its descendants, the line currentWord.deleteCharAt(currentWord.length() - 1); is used to backtrack. 
        // It removes the last character appended to currentWord.
        // This is necessary because, in the recursive exploration, you want to try all possible paths from the current node. 
        // Once you've explored a particular branch (e.g., words starting with "apb"), you need to go back to the previous state (e.g., "ap") 
        // to explore other possibilities (e.g., words starting with "apa").
        // Deleting the last character allows the algorithm to try the next character in the loop during the traversal.
        }
    }
        //  The saveSrchFrqToFile method writes the current search frequencies to the searchFrequencies.txt file. 
        // This includes the search frequencies for each city stored in the trie.
        public void saveSrchFrqToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            saveSrchFrequencies(root, new StringBuilder(), writer);
        } catch (IOException e) {
            System.out.printf("File write error : %s\n", fileName);
        }
    }
     // We use this method to save the search frequencies and it is done using the same way we did for getWords
    private void saveSrchFrequencies(TNode node, StringBuilder currentWord, PrintWriter writer) {
        if (node == null) {
            return;
        }
        if (node.EndWrd) {
            writer.println(currentWord.toString() + " " + node.schFrq);
        }

        for (char c1 = 'a'; c1 <= 'z'; c1++) {
            int index = c1 - 'a';
            currentWord.append(c1);
            saveSrchFrequencies(node.chldrn[index], currentWord, writer);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }


    // The loadSrchFrqFrmFile method reads the search frequencies from the searchFrequencies.txt file and 
    // updates the corresponding nodes in the trie.
    public void loadSrchFrqFrmFile(String fileName) {
        try (BufferedReader read_1 = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = read_1.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String word = parts[0];
                    int frequency = Integer.parseInt(parts[1]);
                    TNode node = findNode(word.toLowerCase());
                    if (node != null) {
                        node.schFrq = frequency;
                    }
                }
            }
        } catch (IOException e) {
            System.out.printf("File read error : %s\n", fileName);
        }
    }
}
