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

    public AutocompleteTrie() {
        root = new TNode(' ');
    }

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
        current.schFrq++; // Increment search frequency
    }

    public void showSrchFrq(String word) {
        TNode node = findNode(word.toLowerCase());
        if (node != null) {
            System.out.println("| City Name \t| Search Frequency |");
            System.out.printf("| %s \t\t|\t %s \t\t|\n",word, node.schFrq);
        } else {
            System.out.println("No Search Frequency for this city \""+word+"\"");
        }
    }
    public void increaseSrchFrq(String word) {
        TNode node = findNode(word.toLowerCase());
        if (node != null) {
            node.schFrq++;
        } else {
            System.out.println("No Search Frequency for this city \""+word+"\"");
        }
    }
    public List<String> autoComplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        TNode node = findNode(prefix.toLowerCase());
        getWords(node, new StringBuilder(prefix.toLowerCase()), suggestions);
        return suggestions;
    }

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
        }
    }
        public void saveSrchFrqToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            saveSrchFrequencies(root, new StringBuilder(), writer);
        } catch (IOException e) {
            System.out.printf("File write error : %s\n", fileName);
        }
    }

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
