package cc.pageRanking;

import cc.FrequencyCount.WordFrequency;
import cc.PatternFinder.PatternFinder;

import java.util.List;
import java.util.Map;

public class PageRanking {

    public static PageScore[] rank(String query, List<WordFrequency> wordFrequencyList) {
        System.out.println("Page Ranking . . .");
        System.out.printf("Based on Query \"%s\"\n", query);
        PageScore[] pageScores = new PageScore[wordFrequencyList.size()];

        // Calculate scores for each document based on the specific words
        for (int i = 0; i < wordFrequencyList.size(); i++) {
            int score = 0;
            Map<String, Integer> wordCount = wordFrequencyList.get(i).wordsCount;
            for (String word : wordCount.keySet()) {
                score += wordCount.getOrDefault(word, 0);
            }
            pageScores[i] = PageScore.of(wordFrequencyList.get(i).filename, score);
        }

        // Apply QuickSort
        quickSort(pageScores, 0, pageScores.length - 1);

        // Output the sorted documents
        for (PageScore pageScore : pageScores) {
            System.out.printf("%s | SCORE: %d | %s\n", pageScore.document, pageScore.score, PatternFinder.findListingUrlInFile(pageScore.document));
        }

        return pageScores;
    }

    public static void main(String[] args) {
        List<WordFrequency> wordFrequencyList = List.of(
                new WordFrequency("abcdabcd", Map.of("1", 2, "bathroom", 2)),
                new WordFrequency("abcdabcd", Map.of("1", 3, "bathroom", 5)),
                new WordFrequency("abcdabcd", Map.of("1", 3, "bathroom", 5, "bed", 5))
        );

        rank("bathroom", wordFrequencyList);
    }

    private static void quickSort(PageScore[] array, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(array, begin, end);

            quickSort(array, begin, partitionIndex - 1);
            quickSort(array, partitionIndex + 1, end);
        }
    }

    private static int partition(PageScore[] array, int begin, int end) {
        int pivot = array[end].score;
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (array[j].score >= pivot) {
                i++;

                PageScore temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        PageScore swapTemp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = swapTemp;

        return i + 1;
    }
}
