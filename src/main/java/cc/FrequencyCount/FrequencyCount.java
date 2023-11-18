package cc.FrequencyCount;

import cc.InvertedIndex.Config;
import cc.InvertedIndex.FileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class FrequencyCount {


    private static int countFrequency(List<String> fileContent, String targetWord) {
        // Define a pattern to split the file content based on punctuations, spaces and tabs
        Pattern splitPattern = Pattern.compile("[\\s\\p{Punct}]+");

        int count = 0;

        for (String line : fileContent) {
            String[] words = splitPattern.split(line);

            for (String word : words) {

                if (word.equalsIgnoreCase(targetWord)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static PriorityQueue<WordFrequency> getMinHeap(String filePath, int k, List<String> stopWOrdsList) throws IOException {
        PriorityQueue<WordFrequency> minHeap = new PriorityQueue<>();

        Pattern splitPattern = Pattern.compile("[\\s\\p{Punct}]+");

        try (BufferedReader br = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = splitPattern.split(line);
                for (String word : words) {
                    if (!word.isEmpty() && !stopWOrdsList.contains(word.toLowerCase())) {
                        WordFrequency currentWordFrequency = new WordFrequency(word, 1);

                        // Check if the word is already in the minHeap
                        if (minHeap.contains(currentWordFrequency)) {
                            // Remove the existing entry, increment frequency, and add it back
                            minHeap.remove(currentWordFrequency);
                            currentWordFrequency.frequency++;
                        }

                        minHeap.offer(currentWordFrequency);

                        if (k > 0) {
                            // Maintain the size of the heap to be k
                            if (minHeap.size() > k) {
                                minHeap.poll();
                            }
                        }

                    }
                }
            }
        }

        return minHeap;
    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> stopWOrdsList = Arrays.asList("is", "the", "and", "in", "to", "of", "a", "for", "with", "was", "i", "as", "at", "it", "its", "on");

        try {
            while (true) {
                System.out.print("\nEnter the URL (Currently treated as filename): ");
                String fileName = scanner.nextLine();

                System.out.println("Select one of the following options:\n\t1. Get count for a specific word.\n\t2. Get count of all the words.\n\t3. Get count of the k most frequent words.\n\t0. Exit");
                System.out.print("Enter option number: ");
                int selectedOption = scanner.nextInt();

                // Consume the newline character
                scanner.nextLine();

                if (selectedOption == 0) {
                    System.out.println("Exiting");
                    break;
                }

                String filePath = Config.PARTENT_DIR + Config.COMMON_PATH + fileName;

                List<String> fileContent = FileReader.readFile(Config.PARTENT_DIR, Config.COMMON_PATH + "doc1.txt");

                if (selectedOption == 1) {
                    System.out.print("Enter the word to count its frequency: ");
                    String targetWord = scanner.nextLine();

                    int count = countFrequency(fileContent, targetWord);
                    System.out.println("Frequency Count for " + targetWord + " : " + count);
                    System.out.println("===========================\n");
                } else if ( selectedOption == 2 | selectedOption == 3) {

                    if (selectedOption == 2) {
                        PriorityQueue<WordFrequency> allWordFrequencies = getMinHeap(filePath, -1, stopWOrdsList);

                        System.out.println("Printing Frequency Count of all the words in " + fileName);

                        for (WordFrequency wf : allWordFrequencies) {
                            System.out.println("\t" + wf.word + " : " + wf.frequency);
                        }
                        System.out.println("===========================\n");
                    } else {
                        System.out.print("Enter the number of top frequent words: ");
                        int topKWords = scanner.nextInt();

                        // Consume the newline character
                        scanner.nextLine();

                        PriorityQueue<WordFrequency> topKFrequentWords =  getMinHeap(filePath, topKWords, stopWOrdsList);
                        while (!topKFrequentWords.isEmpty()) {
                            WordFrequency wordFrequency = topKFrequentWords.poll();
                            System.out.println("\t" + wordFrequency.word + ": " + wordFrequency.frequency);
                        }
                        System.out.println("===========================\n");
                    }
                }

                else {
                    System.out.println("Please enter a valid option");
                }


            }


        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Ensure the scanner is closed even if an exception occurs
            scanner.close();
        }
    }
}

class WordFrequency implements Comparable<WordFrequency> {
    String word;
    int frequency;

    public WordFrequency(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(WordFrequency other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}
