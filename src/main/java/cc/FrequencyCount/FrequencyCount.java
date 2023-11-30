package cc.FrequencyCount;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class FrequencyCount {
    // Define a pattern to split the file content based on punctuations, spaces and tabs
    public static final Pattern SPLIT_PATTERN = Pattern.compile("[\\s\\p{Punct}]+");
    // TODO split using space and remove all punc FileReader.WORD_WITHOUT_SPACE

    public static List<WordFrequency> getMultipleWordsFrequencyCount(String[] filenames, String[] searchWords) {
        List<WordFrequency> fileWordFrequencies = new ArrayList<>();

        for (String filename : filenames) {
            File file = new File(filename);

            if (file.exists() && file.isFile()) {
                Map<String, Integer> wordMap = createWordFrequencyMap(file, searchWords);
                fileWordFrequencies.add(new WordFrequency(filename, wordMap));
            }
        }

        return fileWordFrequencies;

    }

    public static Map<String, Integer> createWordFrequencyMap(File file, String[] searchWords) {
        Map<String, Integer> wordMap = new HashMap<>();

        for (String searchWord : searchWords) {
            wordMap.put(searchWord, 0);
        }

        try (BufferedReader bufferReader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = bufferReader.readLine()) != null) {
                // Convert the entire line to lower case for accurate frequency count
                String lowerCaseLine = currentLine.toLowerCase();
                String[] words = SPLIT_PATTERN.split(lowerCaseLine);

                for (String word : words) {
                    word = word.trim();
                    if (!word.isEmpty()) {
                        for (String searchWord : searchWords) {
                            if (word.equalsIgnoreCase(searchWord)) {
                                wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file " + file.getName() + ". Make sure that the file path is correct.");
        }

        return wordMap;

    }

}



