package cc.InvertedIndex;

import java.util.Scanner;
import java.util.Set;

import static cc.InvertedIndex.Config.VALID_WORD_REGEX;
import static cc.InvertedIndex.FileReader.listAllFiles;

public class InvertIndexingRunner {
    private static InverseIndexing indexer;
    private static boolean sessionFlag = true;


    public static void init() {
        indexer = new InverseIndexing();

        addStopWordsTask();
        addWordsTask();
    }

    private static void addStopWordsTask() {
        FileReader
                .readFile(Config.PARTENT_DIR, Config.FILE_STOPWORDS)
                .forEach(indexer::addStopWords);
    }

    // may need to work on removing symbols
    private static void addWordsTask() {
        String parentPath = Config.PARTENT_DIR.concat(Config.COMMON_PATH);

        String[] fileNames = listAllFiles(parentPath);

        for (String fileName : fileNames) {
            indexer.addDocument(parentPath, fileName);
        }
    }

    public static void main(String[] args) {
        init();
        int menuUserInput;
        while (sessionFlag) {
            menuUserInput = menuTakeUserInput();

            switch (menuUserInput) {
                case 1 -> searchDocument();
                case 2 -> init();
                default -> sessionFlag = false;
            }
        }
    }

    // take menu item input from user as integer
    private static int menuTakeUserInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 : To search for words in the documents");
        System.out.println("Enter 2 : To Reload the indexing");
        System.out.println("Enter 3 : To Return to main menu");

        String input = sc.next();

        while (!input.matches("[0-9]")) {
            System.out.println("Please enter a valid input");
            input = sc.next();
        }

        return Integer.parseInt(input);
    }

    // Search for documents containing a word
    private static void searchDocument() {
        String query = wordTakeUserInput();
        Set<String> result = indexer.search(query);

        if (result.isEmpty()) {
            System.out.printf("No documents found containing the word \"%s\"\n", query);
            System.out.println("Try again with another word");
        } else {
            System.out.printf("Documents containing the word \"%s\" : ", query);
            System.out.printf("%s \n", String.join(" , ", result));
        }
        System.out.println();
    }

    // Take valid word input from user
    private static String wordTakeUserInput() {
        // Function InvertIndexing
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the word you want to search among the documents : ");
        String word = sc.next();

        while (!word.matches(VALID_WORD_REGEX)) {
            System.out.println("Please enter a valid word!");
            word = sc.next();
        }

        return word;
    }
}
