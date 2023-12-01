package cc.crawler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import cc.utils.config;
public class stopwordRemover {
    public static void main(String[] args){
        String inputTextFilePath = "parsed_liv.rent/page_1_listing_1.txt"; // Path to input text file
        String outputFilePath = "filteredText.txt"; // Path to output filtered text file

        try {
            List<String> stopwords = readStopwords(config.stopwordsFilePath);
            filterText(inputTextFilePath, outputFilePath, stopwords);
            System.out.println("Stopwords removed successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    private static List<String> readStopwords(String filePath) throws IOException {
        List<String> stopwords = new ArrayList<>();
        Path path = Paths.get(filePath);

        // Read stopwords from the file
        try (Scanner scanner = new Scanner(Files.newBufferedReader(path))) {
            while (scanner.hasNextLine()) {
                stopwords.add(scanner.nextLine().trim());
            }
        }
        return stopwords;
    }

    private static void filterText(String inputFilePath, String outputFilePath, List<String> stopwords) throws IOException {
        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputFilePath);

        // Read input text from file
        String inputText = Files.readString(inputPath).toLowerCase();

        // Replace stopwords with single space
        for (String word : stopwords) {
            inputText = inputText.replaceAll("\\b" + word.toLowerCase() + "\\b", " ");
        }

        // Write filtered text to the output file
        Files.writeString(outputPath, inputText);
    }

}
