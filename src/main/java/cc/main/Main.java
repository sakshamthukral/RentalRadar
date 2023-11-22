package cc.main;
import cc.InvertedIndex.InvertIndexingRunner;
import cc.crawler.*;

import cc.suggestions.spellchecker.SpellCheckerRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import cc.suggestions.Autocomplete.AutoComplete;

import org.apache.commons.io.FileUtils;
import cc.utils.config;
public class Main {
    public static final String chromeDriverPath = config.chromeDriverPath;
    public static final String chromeBrowserPath = config.chromeBrowserPath;
    public static String globCity = "";

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

    public static void saveLastRunTime(String lastRunTimeFilePath, String cityName) {
        createFolderIfNotExists("logs");
        try (PrintWriter writer = new PrintWriter(new FileWriter(lastRunTimeFilePath, true))) {
            long currentTime = System.currentTimeMillis();
            writer.println(cityName + ":" + currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to get the last execution time for a specific city
    public static long getLastRunTime(String lastRunTimeFilePath, String cityName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(lastRunTimeFilePath))) {
            Map<String, Long> cityTimestamps = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String city = parts[0];
                    long timestamp = Long.parseLong(parts[1]);
                    cityTimestamps.put(city, timestamp);
                }
            }
            return cityTimestamps.getOrDefault(cityName, 0L);
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
    private static String runCrawlingAndParsing(Scanner sc, String HTMLFolderPath, String txtFolderPath, String lastRunTimeFilePath, int websiteCode){
        System.out.print("Enter the city where you are looking for rentals: ");
        String city = sc.nextLine();
        while(!config.CITIES.contains(city)) {
            while (city.isBlank() || (!city.matches(config.cityRegex))) {
                System.out.print("Invalid City please renter the city: ");
                city = sc.nextLine();
            }
            String suggestion = "";
            AutoComplete.init();
            SpellCheckerRunner.init();
            if (city.length() < config.minCityLength) {
                suggestion = AutoComplete.runAutoComplete(city);
            }
            if (suggestion.isBlank() || city.length() >= config.minCityLength) { //TODO rethink the logic
                System.out.println("SpellChecker suggestions:");
                suggestion = SpellCheckerRunner.spellCheckAndSelectCity(city);
            }
//            System.out.println(suggestion);
            city = suggestion;

            // selected the city -> displaying and incrementing search frequency
            AutoComplete.searchFrequency(city);
        }


        long lastRunTime = getLastRunTime(lastRunTimeFilePath,city.toLowerCase());
        System.out.println("Last run time for " + city + ": " + (lastRunTime > 0 ? new Date(lastRunTime) : "N/A"));
        System.out.print("Do you want to rerun the program? (y/n): ");
        String rerunChoice = sc.nextLine().toLowerCase();
        if ("y".equals(rerunChoice)) {


        System.out.print("Enter the number of pages(<5 in order to prevent longer running times) you want to scrape: ");
        int numPages = sc.nextInt();

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
//        options.setBinary(chromeBrowserPath); FIXME ->  it works without this
        options.addArguments("--deny-permission-prompts");
        options.addArguments("--window-size=1920x1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String htmlCityFolder = HTMLFolderPath+"/"+city;
        String txtCityFolder = txtFolderPath+"/"+city;
        try{
            FileUtils.deleteDirectory(new File(htmlCityFolder));
            FileUtils.deleteDirectory(new File(txtCityFolder));
        } catch(Exception e){
            System.out.println(e);
            System.out.println("We have faced some issue while crawling. Please try again");
        }

        createFolderIfNotExists(htmlCityFolder);
        createFolderIfNotExists(txtCityFolder);
        if(websiteCode==1){
            crawlLiv.driveCrawling(driver, wait, numPages, city);
            parseLiv.processHtmlFiles(htmlCityFolder, txtCityFolder);
        }else if(websiteCode==2){
            crawlRental.driveCrawling(driver, wait, numPages, city);
            parseRental.processHtmlFiles(htmlCityFolder, txtCityFolder);
        }else if (websiteCode==3) {
            crawlRentSeeker.driveCrawling(driver, wait, numPages, city);
            parseRentSeeker.processHtmlFiles(htmlCityFolder, txtCityFolder);
        }
        saveLastRunTime(lastRunTimeFilePath, city);
        globCity = city;
        }
        return city;
       }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("Enter-1: To run web crawling and parsing!");
            System.out.println("Enter-2: To Exit!");
            int webOption = sc.nextInt();
            if (webOption == 1){
                List<String> paths = getPaths(sc);
                String HTMLFolderPath=paths.get(0);
                String txtFolderPath=paths.get(1);
                String lastRunTimeFilePath=paths.get(2);
                int websiteCode=Integer.parseInt(paths.get(3));

                // word completion -> spell check -> crawling -> parsing
                String cityName = runCrawlingAndParsing(sc, HTMLFolderPath, txtFolderPath, lastRunTimeFilePath, websiteCode);
                System.out.println(txtFolderPath);
                System.out.println(cityName);
                // TODO invertedIndex -> FrequencyCount -> PageRanking

                // Inverted Index
                List<String> folders = List.of(Path.of(txtFolderPath, cityName).toString());
                InvertIndexingRunner.init(folders);
                InvertIndexingRunner.run(folders);

                // FrequencyCount -> PageRanking
            }
            if(webOption == 2){
                break;
            }
        }
    }
}
