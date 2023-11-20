package cc.main;
import cc.crawler.*;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cc.Autocomplete.Trie;

import java.io.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import cc.utils.config;
public class Main {
    public static final String chromeDriverPath = config.chromeDriverPath;
    public static final String chromeBrowserPath = config.chromeBrowserPath;

    public static void createFolderIfNotExists(String folderPath){
        File folder = new File(folderPath);
        if(!folder.exists()){
            boolean folderCreated = folder.mkdirs();
            if(folderCreated){
                System.out.println("Folder created: "+ folderPath);
            }else{
                System.err.println("Failed to create folder: "+folderPath);
                System.exit(1);
            }
        }
    }

    // Function to save the last execution time
    public static void saveLastRunTime(String lastRunTimeFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(lastRunTimeFilePath))) {
            long currentTime = System.currentTimeMillis();
            writer.println(currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to get the last execution time
    public static long getLastRunTime(String lastRunTimeFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(lastRunTimeFilePath))) {
            String line = reader.readLine();
            return line != null ? Long.parseLong(line) : 0;
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private static List<String> getPaths(Scanner sc){
        System.out.println("Enter 1 : To checkout rental leads from liv.rent");
        System.out.println("Enter 2 : To checkout rental leads from rentals.ca");
        System.out.println("Enter 3 : To checkout rental leads from rentseeker.ca");
        int websiteCode = sc.nextInt();
        sc.nextLine();
        String HTMLFolderPath="";
        String txtFolderPath="";
        String lastRunTimeFilePath="";
        switch (websiteCode) {
            case 1 ->{
                HTMLFolderPath=config.HTMLFolderPathLiv;
                txtFolderPath=config.txtFolderPathLiv;
                lastRunTimeFilePath=config.lastRunTimeFilePathLiv;
            }
            case 2 ->{
                HTMLFolderPath=config.HTMLFolderPathRental;
                txtFolderPath=config.txtFolderPathRental;
                lastRunTimeFilePath=config.lastRunTimeFilePathRental;
            }
            case 3 ->{
                HTMLFolderPath=config.HTMLFolderPathRentSeeker;
                txtFolderPath=config.txtFolderPathRentSeeker;
                lastRunTimeFilePath=config.lastRunTimeFilePathRentSeeker;
            }
        }
        return Arrays.asList(HTMLFolderPath, txtFolderPath, lastRunTimeFilePath, String.valueOf(websiteCode));
    }
    private static void runCrawlingAndParsing(Scanner sc, String HTMLFolderPath, String txtFolderPath, String lastRunTimeFilePath, int websiteCode){
        try{
            FileUtils.deleteDirectory(new File(HTMLFolderPath));
            FileUtils.deleteDirectory(new File(txtFolderPath));
        } catch(Exception e){
            System.out.println(e);
        }
                String[] cities = { "Toronto", "Montreal", "Calgary", "Ottawa", "Edmonton", "Winnipeg",
                "Vancouver", "Brampton", "Hamilton", "Surrey", "Quebec", "Halifax", "Laval",
                "London", "Markham", "Vaughan", "Gatineau", "Saskatoon", "Kitchener", "Longueuil",
                "Burnaby", "Windsor", "Regina", "Oakville", "Richmond", "Richmond", "Burlington",
                "Oshawa", "Sherbrooke", "Sudbury", "Abbotsford", "Coquitlam", "Barrie", "Saguenay",
                "Kelowna", "Guelph", "Whitby", "Cambridge", "Catharines", "Milton", "Langley", "Kingston",
                "Ajax", "Waterloo", "Terrebonne", "Saanich", "Delta", "Brantford", "Clarington", "Nanaimo",
                "Strathcona", "Pickering", "Lethbridge", "Kamloops", "Richelieu", "Niagara", "Cape Breton",
                "Chilliwack", "Victoria", "Brossard", "Newmarket", "Repentigny", "Peterborough",
                "Moncton", "Drummondville", "Caledon", "Airdrie", "Sarnia", "Granby", "Fredericton",
                "Aurora", "Mirabel", "Blainville", "Welland", "Belleville" };
        Trie trie = new Trie();
        // Added all major cities to the trie.

        for (String city : cities) {
            trie.insert(city);
        }
        trie.loadSearchFrequenciesFromFile("D:\\ACC\\compute-champions\\src\\main\\java\\cc\\SearchFrequency\\searchFrequencies.txt");

        System.out.print("Enter the city where you are looking for rentals: ");
        String city = sc.nextLine();
                    // Check if the input contains only alphabetic characters
                if (!city.matches("^[a-zA-Z]+$")) {
                    System.out.println("Invalid input.1 Please enter a valid city name.");
                }else{
                List<String> suggestions = trie.autoComplete(city);
                System.out.println("Autocomplete suggestions:");
                String suggestion="";
                for (int i = 0; i < suggestions.size(); i++) {
                    suggestion = suggestions.get(i);
                    System.out.println((i + 1) + ". " + suggestion);
                }
                 int selectedOption=0;
                if (suggestions.size()>1) {
                    System.out.print("Enter the number for the suggestion you want to select: ");
                   
                    try {
                        selectedOption = sc.nextInt();
                        sc.nextLine(); // Consume the newline character1
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Try again!");
                        sc.nextLine(); // Consume the invalid input
    
                    }

                            if (selectedOption > 0 && selectedOption <= suggestions.size()) {
                                suggestion = suggestions.get(selectedOption - 1);
                                System.out.println("City Name " + suggestion + " is selected");
                                trie.displaySearchFrequency(suggestion); // Display search frequency for the selected city
                                trie.incrementSearchFrequency(suggestion); // Increment the search frequency for the selected city
                            } else {
                                System.out.println("Invalid selection. Try Again!");
                            }
                        } else 
                        {
                        	for (int i = 0; i < suggestions.size(); i++) 
       			    	   {
       					        suggestion = suggestions.get(i);
       					        System.out.println("City Name "+ suggestion+ " is selected");
       					     trie.displaySearchFrequency(suggestion);
       					     trie.incrementSearchFrequency(suggestion);
       					   }
                        }
                    


        System.out.print("Enter the number of pages(<5 in order to prevent longer running times) you want to scrape: ");
        int numPages = sc.nextInt();

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(chromeBrowserPath);
        options.addArguments("--deny-permission-prompts");
        options.addArguments("--window-size=1920x1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        createFolderIfNotExists(HTMLFolderPath);
        createFolderIfNotExists(txtFolderPath);
        if(websiteCode==1){
            crawlLiv.driveCrawling(driver, wait, numPages, suggestion);
            parseLiv.processHtmlFiles(HTMLFolderPath, txtFolderPath);
        }else if(websiteCode==2){
            crawlRental.driveCrawling(driver, wait, numPages, suggestion);
            parseRental.processHtmlFiles(HTMLFolderPath, txtFolderPath);
        } else if (websiteCode==3) {
            crawlRentSeeker.driveCrawling(driver, wait, numPages, suggestion);
            parseRentSeeker.processHtmlFiles(HTMLFolderPath, txtFolderPath);
        }
        saveLastRunTime(lastRunTimeFilePath);
       }// else of  if (!city.matches("^[a-zA-Z]+$")) closes here
       trie.saveSearchFrequenciesToFile("D:\\ACC\\compute-champions\\src\\main\\java\\cc\\SearchFrequency\\searchFrequencies.txt");
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("Enter-1: To run web crawling and parsing!");
            System.out.println("Enter-2: To run Inverted Indexing!");
            System.out.println("Enter-3: To Exit!");
            int webOption = sc.nextInt();
            if (webOption == 1){
                List<String> paths = getPaths(sc);
                String HTMLFolderPath=paths.get(0);
                String txtFolderPath=paths.get(1);
                String lastRunTimeFilePath=paths.get(2);
                int websiteCode=Integer.parseInt(paths.get(3));

                long lastRunTime = getLastRunTime(lastRunTimeFilePath);
                System.out.println("Last Execution Time: " + (lastRunTime > 0 ? new Date(lastRunTime) : "N/A"));
                System.out.print("Do you want to rerun the program? (y/n): ");
                String rerunChoice = sc.nextLine().toLowerCase();
                if ("y".equals(rerunChoice)) {
                    runCrawlingAndParsing(sc, HTMLFolderPath, txtFolderPath, lastRunTimeFilePath, websiteCode);
                } else{
                    // Following code is for inputting a text file and getting the filtered text after removal of all stopwords
//                    try {
//                        List<String> stopwords = readStopwords(config.stopwordsFilePath);
//                        filterText(inputTextFilePath, outputFilePath, stopwords);
//                        System.out.println("Stopwords removed successfully!");
//                    } catch (IOException e) {
//                        System.out.println("An error occurred: " + e.getMessage());
//                    }
                    System.out.println("Running the rest of the program!!");
                }
            } else if (webOption == 2) {
                System.out.println("Initiated inverted indexing");
            } else if (webOption == 3){
                break;
            } else {
                System.out.println("Not a valid option!");
            }
        }
        
    }
}
