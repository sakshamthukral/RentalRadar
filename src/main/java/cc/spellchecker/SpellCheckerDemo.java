package cc.spellchecker;

import cc.InvertedIndex.Config;
import cc.InvertedIndex.FileReader;

import java.util.List;
import java.util.Scanner;


public class SpellCheckerDemo {

    public static final String FILENAME = "RentalLists.txt";
    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker();

        List<String> words = FileReader.readFile(Config.PARENT_DIR, Config.COMMON_PATH + FILENAME);
        for (String lineWord : words) {
            String word = lineWord.trim();
            spellChecker.loadWordIntoDictionary(word);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Spell Checker!");
        System.out.print("Enter a word to check: ");
        String inputWord = sc.nextLine();

        if (!spellChecker.isSpelledCorrectly(inputWord)) {
            List<String> suggestions = spellChecker.suggestWords(inputWord);

            if (!suggestions.isEmpty()) {
                System.out.println("Did you mean: " + suggestions.get(0));
            } else {
                System.out.println("No suggestions found.");
            }

        } else {
            System.out.println("The word is spelled correctly.");
        }
    }
}

