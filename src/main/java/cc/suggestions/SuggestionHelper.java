package cc.suggestions;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SuggestionHelper {
    public static String printAndSelectSuggestion(List<String> suggestions) {
        String suggestion = "";
        int selectedOption = 0;

        for (int i = 0; i < suggestions.size(); i++) {
            System.out.println((i + 1) + ". " + suggestions.get(i));
        }
        System.out.println();

        // selection among the listed options
        if (suggestions.size() > 1) {
            Scanner sc = new Scanner(System.in);
            int suggOption = 0;

            boolean isSugegstionSelected = false;
            while (!isSugegstionSelected) {
                System.out.print("Enter the number for the suggestion you want to select: ");

                try {
                    suggOption = sc.nextInt();

                    if (suggOption <= 0 || suggOption > suggestions.size()) {
                        System.out.println("Invalid selection. Try Again!");
                    } else {
                        isSugegstionSelected = true;
                        selectedOption = suggOption;
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Try again!");
                    sc.nextLine(); // Consume the invalid input
                }
            }

            suggestion = suggestions.get(selectedOption - 1);
            System.out.println("City Name \"" + suggestion + "\" is selected");
            System.out.println();

        } else if (suggestions.size() == 1) { // list has only one element
            suggestion = suggestions.get(selectedOption);
            System.out.println("City Name \"" + suggestion + "\" is selected");
        } else {
            System.out.println("No suggestions found.");
        }
        System.out.println();
        return suggestion;
    }
}
