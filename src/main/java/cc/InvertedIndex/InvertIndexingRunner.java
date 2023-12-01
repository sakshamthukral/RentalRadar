package cc.InvertedIndex;

import cc.FrequencyCount.FrequencyCount;
import cc.FrequencyCount.WordFrequency;
import cc.PatternFinder.PatternFinder;
import cc.pageRanking.PageRanking;
import cc.pageRanking.PageScore;
import cc.utils.FileReader;
import cc.utils.config;

import java.nio.file.Path;
import java.util.*;

public class InvertIndexingRunner {
    public static final String VALID_WORD_REGEX = "^[A-Za-z0-9\\-_ ]*$";
    private static InverseIndexing indexer;

    public static void init(List<String> folders) {
        indexer = new InverseIndexing();

        addStopWordsTask();
        addWordsTask(folders);
    }

    private static void addStopWordsTask() {
        FileReader.readFile(config.stopwordsFilePath, FileReader.TYPE.WORDS)
                .forEach(indexer::addStopWords);
    }

    // may need to work on removing symbols
    private static void addWordsTask(List<String> folders) {

        for (String parentPath : folders) {
            if(!parentPath.isBlank()){
                String[] fileNames = FileReader.listAllFiles(parentPath);
                if(fileNames != null && fileNames.length > 0){
                    for (String fileName : fileNames) {
                        indexer.addDocument(parentPath, fileName);
                        System.out.printf("DONE - %s\n", Path.of(parentPath, fileName));
                    }
                } else {
                    System.out.printf("No file found in - %s\n", Path.of(parentPath));
                    System.out.println("Do a crawling for the city first");
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) {
        List<String> folders = List.of(config.descriptionLiv, config.descriptionRental, config.descriptionRentSeeker);
        init(folders);

        run();
    }

    public static void run() {
        boolean sessionFlag = true;
        int menuUserInput;
        while (sessionFlag) {
            menuUserInput = menuTakeUserInput();

            switch (menuUserInput) {
                case 1 -> searchDocument();
                default -> sessionFlag = false;
            }
        }
    }

    // take menu item input from user as integer
    private static int menuTakeUserInput() {
        System.out.println();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 : To search for words in the crawled documents");
        System.out.println("Enter 2 : To Return to main menu");
        // TODO rename to change city prompt?

        String input = sc.next();

        while (!input.matches("[0-9]")) {
            System.out.println("Please enter a valid input");
            input = sc.next();
        }

        int exit = 2;

        try{
            return Integer.parseInt(input);
        }catch (NumberFormatException e){
            System.out.println("Number parsing error; Returning to main menu");
            return exit;
        }
    }

    // Search for documents containing a word
    private static void searchDocument() {
        boolean repeat = true;
        List<WordFrequency> wordFrequencyList = new ArrayList<>();

        while (repeat) {
            String query = queryTakeUserInput();
            Set<String> result = indexer.search(query);

            System.out.println();
            if (result.isEmpty()) {
                System.out.printf("No documents found containing the query \"%s\"\n", query);
                System.out.println("Try again with another search query");
            } else {
                System.out.printf("Documents containing the query \"%s\" : \n", query);

                result.stream().iterator().forEachRemaining(elem -> System.out.printf("%s - %s\n", elem, PatternFinder.findListingUrlInFile(elem)));
                System.out.println();

                // showing frequency count
                System.out.println("Frequency Counting . . .");

                String[] words = query.split(" ");
                String[] documents = result.toArray(new String[]{});
                wordFrequencyList = FrequencyCount.getMultipleWordsFrequencyCount(documents, words);

                for (WordFrequency wf : wordFrequencyList) {
                    System.out.printf("FILE: %s - URL: %s\n", wf.filename, PatternFinder.findListingUrlInFile(wf.filename));
                    System.out.println("-- Word Frequency List --");

                    System.out.printf("| WORD\t| FREQUENCY |\n");
                    //TODO a table like freq count?
                    for (String key : wf.wordsCount.keySet()) {
                        System.out.printf("| %s\t| %d\t|\n",key, wf.wordsCount.get(key) );
                    }
                    System.out.println();
                }

                // PageRanking
                PageScore[] sortedPages = PageRanking.rank(query, wordFrequencyList);
                System.out.println();

                int repeatInput = menuTakeUserInputToForward();

                if(repeatInput == 1)
                    repeat = false;
            }
        }

        List<String> filenames = new ArrayList<>();
        for (WordFrequency wf : wordFrequencyList) {
            filenames.add(wf.filename);
        }

        PatternFinder.run(filenames);
        System.out.println();
    }

    // take menu item input from user as integer
    private static int menuTakeUserInputToForward() {
        Scanner sc = new Scanner(System.in);
        int input = 0;

        boolean isMenuSelected3 = false;
        while (!isMenuSelected3){
            System.out.println("Enter 1 : To find pattern in the files");
            System.out.println("Enter 2 : To search for a different query");

            try {
                input = sc.nextInt();
                sc.nextLine();
                isMenuSelected3 = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
                sc.nextLine(); // Consume the invalid input
            }
        }

        return input;
    }

    // Take valid word input from user
    private static String queryTakeUserInput() {
        System.out.println();
        // Function InvertIndexing
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the word you want to search among the documents : ");
        String query = sc.nextLine().trim();

        while (!query.matches(VALID_WORD_REGEX)) {
            System.out.println();
            System.out.println("Please enter a valid word!");
            query = sc.nextLine();
        }

        return query;
    }
}
