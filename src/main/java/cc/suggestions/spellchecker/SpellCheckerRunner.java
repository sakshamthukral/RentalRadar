package cc.suggestions.spellchecker;

import cc.suggestions.SuggestionHelper;
import cc.utils.FileReader;
import cc.utils.config;

import java.util.List;
import java.util.Scanner;

public class SpellCheckerRunner {

    private static SpellChecker spellChecker;

    public static void init() {
        spellChecker = new SpellChecker();

        List<String> words = FileReader.readFile(config.cityNamesFilePath, FileReader.TYPE.LINES);
        for (String lineWord : words) {
            String word = lineWord.trim();
            spellChecker.loadWordIntoDictionary(word);
        }
    }

    public static String spellCheckAndSelectCity(String input) {
        System.out.println("Spell Checking . . .");
        System.out.println();
        String inputWord = input.toLowerCase();
        String finalCityName = "";

        if (!spellChecker.isSpelledCorrectly(inputWord)) {
            List<String> suggestions = spellChecker.suggestWords(inputWord);

            if(!suggestions.isEmpty()){
                System.out.println("SpellChecking suggestions:");
                finalCityName = SuggestionHelper.printAndSelectSuggestion(suggestions);
            } else {
                System.out.println("No SpellChecking suggestions found.");
                System.out.println();
            }
        } else {
            System.out.println("The word \"" + input + "\" is spelled correctly.");
            finalCityName = input;
        }

        return finalCityName;
    }


    public static void main(String[] args) {
        SpellCheckerRunner.init();

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Spell Checker!");
        System.out.print("Enter a word to check: ");
        String inputWord = sc.nextLine().toLowerCase();

        String finalSuggestion = SpellCheckerRunner.spellCheckAndSelectCity(inputWord);
        System.out.println("Final Suggestion : " + finalSuggestion);
    }
}
