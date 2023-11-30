package cc.FrequencyCount;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class FrequencyCount {

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

        // Define a pattern to split the file content based on punctuations, spaces and tabs
        Pattern splitPattern = Pattern.compile("[\\s\\p{Punct}]+");
        // TODO split using space and remove all punc FileReader.WORD_WITHOUT_SPACE

        try (BufferedReader bufferReader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = bufferReader.readLine()) != null) {
                // Convert the entire line to lower case for accurate frequency count
                String lowerCaseLine = currentLine.toLowerCase();
                String[] words = splitPattern.split(lowerCaseLine);

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
            throw new RuntimeException(e);
        }

        return wordMap;

    }

    public static void main (String[] args) {
        String[] filenames = {"storage/txt/common/doc1.txt", "storage/txt/common/doc2.txt", "storage/txt/common/doc3.txt"};
        String[] searchWords = {"document", "sample"};

        List<WordFrequency> wordFrequencies = getMultipleWordsFrequencyCount(filenames, searchWords );

        for (WordFrequency wordFrequency : wordFrequencies) {

            System.out.println(wordFrequency.filename + " -> " + wordFrequency.wordsCount);
        }

    }
}
