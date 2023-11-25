package cc.main;
import cc.InvertedIndex.InvertIndexingRunner;
import cc.crawler.*;

import cc.suggestions.spellchecker.SpellCheckerRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import cc.suggestions.Autocomplete.AutoComplete;
import cc.utils.helper;
import org.apache.commons.io.FileUtils;
import cc.utils.config;
public class Main {
    public static String globCity = "";
    public static int globWebsiteCode;
    public static String HTMLFolderPath;
    public static String txtFolderPath;
    public static String lastRunTimeFilePath;


    public static void changeWebsitePaths(String htmlCityFolder, String txtCityFolder) throws IOException {
        FileUtils.deleteDirectory(new File(htmlCityFolder));
        FileUtils.deleteDirectory(new File(txtCityFolder));
        getPaths(globWebsiteCode);
    }
    public static void saveLastRunTime(String lastRunTimeFilePath, String cityName) {
        helper.createFolderIfNotExists("logs");
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

    private static void getPaths(int websiteCode){
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
    }
    public static String runCrawlingAndParsing(Scanner sc) throws IOException {
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

        Object[] driverAndWait = helper.createChromeDriverWithWait();
        WebDriver driver = (WebDriver) driverAndWait[0];
        WebDriverWait wait = (WebDriverWait) driverAndWait[1];

        boolean gotLeads=false;
        int count=0;
        while (!gotLeads || count<3){
            count+=1;
            String htmlCityFolder = HTMLFolderPath+"/"+city;
            String txtCityFolder = txtFolderPath+"/"+city;
            try{
                FileUtils.deleteDirectory(new File(htmlCityFolder));
                FileUtils.deleteDirectory(new File(txtCityFolder));
            } catch(Exception e){
                System.out.println(e);
                System.out.println("We have faced some issue while crawling. Please try again");
            }
            helper.createFolderIfNotExists(htmlCityFolder);
            helper.createFolderIfNotExists(txtCityFolder);
            if(globWebsiteCode==1){
                gotLeads = crawlLiv.driveCrawling(driver, wait, numPages, city);
                if(gotLeads){
                    parseLiv.processHtmlFiles(htmlCityFolder, txtCityFolder);
                }
                else{
                    System.out.println("No Leads found for "+city+" in liv.rent, therefore initiated checking rentals.ca");
                    globWebsiteCode=2;
                    changeWebsitePaths(htmlCityFolder,txtCityFolder);
                }
            }else if(globWebsiteCode==2){
                gotLeads = crawlRental.driveCrawling(driver, wait, numPages, city);
                if(gotLeads){
                    parseRental.processHtmlFiles(htmlCityFolder, txtCityFolder);
                } else {
                    System.out.println("No Leads found for "+city+" in rentals.ca, therefore initiated checking rentSeeker.ca");
                    globWebsiteCode=3;
                    changeWebsitePaths(htmlCityFolder,txtCityFolder);
                }
            }else if (globWebsiteCode==3) {
                gotLeads = crawlRentSeeker.driveCrawling(driver, wait, numPages, city);
                if(gotLeads){
                    parseRental.processHtmlFiles(htmlCityFolder, txtCityFolder);
                } else {
                    System.out.println("No Leads found for "+city+" in rentals.ca, therefore initiated checking rentSeeker.ca");
                    globWebsiteCode=1;
                    changeWebsitePaths(htmlCityFolder,txtCityFolder);
                }
            }
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
                System.out.println("Enter 1 : To checkout rental leads from liv.rent");
                System.out.println("Enter 2 : To checkout rental leads from rentals.ca");
                System.out.println("Enter 3 : To checkout rental leads from rentseeker.ca");
                globWebsiteCode = sc.nextInt();
                sc.nextLine();
                getPaths(globWebsiteCode);

                // word completion -> spell check -> crawling -> parsing
                String cityName = null;
                try {
                    cityName = runCrawlingAndParsing(sc);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(txtFolderPath);
                System.out.println(cityName);

                // invertedIndex -> FrequencyCount -> PageRanking
                List<String> folders = List.of(Path.of(txtFolderPath, cityName).toString());
                InvertIndexingRunner.init(folders);
                InvertIndexingRunner.run(folders);
            }
            if(webOption == 2){
                break;
            }
        }
//        getPaths(2);
//        System.out.println(HTMLFolderPath);
//        System.out.println(txtFolderPath);
//        System.out.println(lastRunTimeFilePath);

    }
}
