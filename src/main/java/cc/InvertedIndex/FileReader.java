package cc.InvertedIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    // read File using Java io
    public static List<String> readFile(String parentPath, String fName) {
        // try with resources -> autoCloseable
        List<String> words = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(parentPath, fName)); Scanner s = new Scanner(fis)) {
            while (s.hasNext()){
                words.add(s.next().replaceAll("[^a-zA-Z0-9]+",""));
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
}

