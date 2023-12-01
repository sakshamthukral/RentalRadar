package cc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    private static final String WORD_WITH_SPACE = "[^a-zA-Z0-9 ]+";
    public static final String WORD_WITHOUT_SPACE = "[^a-zA-Z0-9]+";

    public static List<String> readFile(String fName, TYPE type) {
        return readFileContent("", fName, type);
    }

    // read File using Java io
    public static List<String> readFile(String parentPath, String fName, TYPE type) {
        return readFileContent(parentPath, fName, type);
    }

    private static List<String> readFileContent(String parentPath, String fName, TYPE type) {
        Path path = Path.of(parentPath, fName);

        // try with resources -> autoCloseable
        List<String> words = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(path.toFile()); Scanner s = new Scanner(fis)) {

            switch (type) {
                case LINES -> {
                    while (s.hasNextLine()) {
                        words.add(s.nextLine().replaceAll(WORD_WITH_SPACE, "").trim().toLowerCase());
                    }
                }
                case WORDS -> {
                    while (s.hasNext()) {
                        String nextWord = s.next().replaceAll(WORD_WITHOUT_SPACE, "").trim().toLowerCase();
                        if(!nextWord.isBlank())
                            words.add(nextWord);
                    }
                }
            }

            return words;
        } catch (IOException e) {
            System.out.printf("No File found for: %s, make sure the file is in the directory \n", fName);
        }

        return Collections.emptyList();
    }

    public static String[] listAllFiles(String parentPath) {
        File directory = new File(parentPath);
        return directory.list();
    }

    public enum TYPE {
        WORDS,
        LINES
    }
}
