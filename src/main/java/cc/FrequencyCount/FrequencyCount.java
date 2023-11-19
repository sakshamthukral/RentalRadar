package cc.FrequencyCount;

import cc.InvertedIndex.Config;
import cc.InvertedIndex.FileReader;

import java.util.*;
import java.util.regex.Pattern;

public class FrequencyCount {
    public static final double SIMILARITY_THRESHOLD = 0.5;
    public static final int MAX_SIMILAR_WORDS_COUNT = 10;

    private static void countFrequency(List<String> fileContent, String targetWord) {
        // Define a pattern to split the file content based on punctuations, spaces and tabs
        Pattern splitPattern = Pattern.compile("[\\s\\p{Punct}]+");

        List<SimilarWord> similarWords = new ArrayList<>();

        int count = 0;

        for (String line : fileContent) {
            String[] words = splitPattern.split(line);

            for (String word : words) {
                getUpdatedSimilarWordsList(similarWords, word.toLowerCase(), targetWord);
                if (word.equalsIgnoreCase(targetWord)) {
                    count++;
                }
            }
        }

        System.out.println("Frequency Count for " + targetWord + " : " + count);
        System.out.println("==========" );
        System.out.println("Frequency Count for similar words" );

        if (similarWords.isEmpty()) {
            System.out.println("No similar words found");
            return;
        }

        similarWords.sort((sw1, sw2) -> Double.compare(sw2.similarity, sw1.similarity));

        int similarWordsCount = 0;

        for (SimilarWord sw : similarWords) {
            if (similarWordsCount >= MAX_SIMILAR_WORDS_COUNT) {
                break;
            }
            System.out.println(sw.word + " : " + sw.count);
            similarWordsCount++;
        }

        return;
    }

    private static void getUpdatedSimilarWordsList (List<SimilarWord> similarWords, String currentWord, String targetWOrd) {
        // Check if the word already exists in the list
        boolean wordExists = false;

        if (currentWord.equalsIgnoreCase(targetWOrd)) {
            return;
        }

        double similarity = EditDistance.getSimilarity(targetWOrd, currentWord);

        if (similarity < SIMILARITY_THRESHOLD) {
            return;
        }

        for (SimilarWord sw : similarWords) {
            if (sw.word.equals(currentWord)) {
                // If the word exists, increment the count
                sw.count++;
                wordExists = true;
                break; // No need to continue iterating
            }
        }

        // If the word doesn't exist, add it to the list
        if (!wordExists) {
            similarWords.add(new SimilarWord(currentWord, 1, similarity));
        }

    }

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> stopWOrdsList = Arrays.asList("is", "the", "and", "in", "to", "of", "a", "for", "with", "was", "i", "as", "at", "it", "its", "on");

        try {
            while (true) {
                System.out.print("\nEnter the filename: ");
                String fileName = scanner.nextLine();

//                System.out.println("Select one of the following options:\n\t1. Get count for a specific word.\n\t2. Get count of all the words.\n\t3. Get count of the k most frequent words.\n\t0. Exit");
//                System.out.print("Enter option number: ");
//                int selectedOption = scanner.nextInt();
//
//                // Consume the newline character
//                scanner.nextLine();
//
//                if (selectedOption == 0) {
//                    System.out.println("Exiting");
//                    break;
//                }


                List<String> fileContent = FileReader.readFile(Config.PARENT_DIR, Config.COMMON_PATH + fileName);

                System.out.print("Enter the word to count its frequency: ");
                String targetWord = scanner.nextLine();

                countFrequency(fileContent, targetWord);
                System.out.println("===========================\n");

//                if (selectedOption == 1) {
//                    System.out.print("Enter the word to count its frequency: ");
//                    String targetWord = scanner.nextLine();
//
//                    countFrequency(fileContent, targetWord);
//                } else if ( selectedOption == 2 | selectedOption == 3) {
//
//                    if (selectedOption == 2) {
//                        PriorityQueue<WordFrequency> allWordFrequencies = getMinHeap(filePath, -1, stopWOrdsList);
//
//                        System.out.println("Printing Frequency Count of all the words in " + fileName);
//
//                        for (WordFrequency wf : allWordFrequencies) {
//                            System.out.println("\t" + wf.word + " : " + wf.frequency);
//                        }
//                        System.out.println("===========================\n");
//                    } else {
//                        System.out.print("Enter the number of top frequent words: ");
//                        int topKWords = scanner.nextInt();
//
//                        // Consume the newline character
//                        scanner.nextLine();
//
//                        PriorityQueue<WordFrequency> topKFrequentWords =  getMinHeap(filePath, topKWords, stopWOrdsList);
//                        while (!topKFrequentWords.isEmpty()) {
//                            WordFrequency wordFrequency = topKFrequentWords.poll();
//                            System.out.println("\t" + wordFrequency.word + ": " + wordFrequency.frequency);
//                        }
//                        System.out.println("===========================\n");
//                    }
//                }
//
//                else {
//                    System.out.println("Please enter a valid option");
//                }
            }


        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // Ensure the scanner is closed even if an exception occurs
            scanner.close();
        }
    }
}


class SimilarWord {
    public String word;
    public int count;
    public double similarity;

    public SimilarWord(String word, int count, double similarity) {
        this.word = word;
        this.count = count;
        this.similarity = similarity;
    }

}
