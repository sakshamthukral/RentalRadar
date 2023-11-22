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

        // selection among the listed options
        if (suggestions.size() > 1) {
            System.out.print("Enter the number for the suggestion you want to select: ");
            Scanner sc = new Scanner(System.in);
            try {
                selectedOption = sc.nextInt();
                sc.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
                sc.nextLine(); // Consume the invalid input
            }
            if (selectedOption > 0 && selectedOption <= suggestions.size()) {
                suggestion = suggestions.get(selectedOption - 1);
                System.out.println("City Name \"" + suggestion + "\" is selected");
            } else {
                System.out.println("Invalid selection. Try Again!");
            }
        } else if (suggestions.size() == 1) { // list has only one element
            suggestion = suggestions.get(selectedOption);
            System.out.println("City Name " + suggestion + " is selected");
        } else {
            System.out.println("No suggestions found.");
        }
        return suggestion;
    }
}
