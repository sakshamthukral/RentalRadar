package cc.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.utils.config;
import cc.utils.helper;

public class crawlRentSeeker {
    public static final String WEBSITE="https://www.rentseeker.ca";
//    public static final String HTMLFolderPath=config.HTMLFolderPathRentSeeker;
    private static void threadWait(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean driveCrawling(WebDriver driver, WebDriverWait wait, int numPages, String inputKeyword){
        boolean gotLeads=true;
        driver.get(WEBSITE);
        driver.manage().window().maximize();
        helper.createFolderIfNotExists(config.HTMLFolderPathRentSeeker+"/"+inputKeyword);
        helper.createFolderIfNotExists(config.descriptionRentSeeker+"/"+inputKeyword);

        switch (inputKeyword) {
            case "Toronto", "toronto" ->
                    driver.get(WEBSITE+"/rentals/apartments/ontario/toronto"); // For searching rentals in Toronto
            case "Windsor", "windsor" -> driver.get(WEBSITE+"/rentals/apartments/ontario/windsor");
        }

        int page=1;
        while(page<=numPages){
            System.out.println("##############################################################################");
            System.out.println("Page Number: "+page);
            threadWait(15000);
            String currentPageUrl = driver.getCurrentUrl();
            System.out.println(currentPageUrl);

            List<String> links = new ArrayList<>();
            List<WebElement> leads;
            try{
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.listing-card div.image-container>a"))); // Waiting for the next page to appear
                leads= driver.findElements(By.cssSelector("div.listing-card div.image-container>a")); // Getting links to all the leads visible on Page-1
            } catch (TimeoutException e){
                System.err.println("Leads not found!!");
                gotLeads=false;
                break;
            }

            for(WebElement lead: leads){
                String link = lead.getAttribute("href");
                Pattern pattern = Pattern.compile(config.linkRegex);
                Matcher matcher = pattern.matcher(link);
                if(matcher.matches()){
                    links.add(link);
                }
            }

            int i=1;
            for(String link: links){
                driver.get(link);
                String htmlContent = driver.getPageSource();
                threadWait(8000);
//                try {
//                    WebElement readMoreBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.truncated-content__read-more.btn-cta.btn-cta--secondary")));
//                    readMoreBtn.click();
//                } catch (NoSuchElementException e) {
//                    // Handle the case when "Read More" button is not found
//                    System.out.println("Read More button not found. Skipping...");
//                } catch (TimeoutException e) {
//                    System.out.println("Read More button not found. Skipping...");
//                } catch (Exception e) {
//                    System.out.println("Read More button not found. Skipping...");
//                }
//                threadWait(5000);
                StringBuilder concatenatedText = new StringBuilder();
                try{
                    WebElement description = driver.findElement(By.cssSelector("section#description"));
                    List<WebElement> childElements = description.findElements(By.xpath(".//*"));
                    for (WebElement child : childElements) {
                        concatenatedText.append(child.getText()).append("\n");
                    }
                } catch (NoSuchElementException e){
                    System.out.println("No Description Available!!");
                } catch (TimeoutException e){
                    System.out.println("No Description Available!!");
                } catch (Exception e) {
                    System.out.println("No Description Available!!");
                }
                concatenatedText.append("\n<<<").append(link).append(">>>");

//                String descriptionText = description.getText();
//
                String descriptionFile = config.descriptionRentSeeker+"/"+inputKeyword +"/page_"+page+"_listing_" + i + ".txt";
                try (FileWriter fileWriter = new FileWriter(descriptionFile)) {
                    fileWriter.write(concatenatedText.toString());
                    System.out.println("Description of " + link + " saved to " + descriptionFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String fileName = config.HTMLFolderPathRentSeeker+"/"+inputKeyword+"/page_"+page+"_listing_" + i + ".html";
                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(htmlContent);
                    System.out.println("HTML content of " + link + " saved to " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
                threadWait(5000);
                i++;
            }
            page++;
            driver.get(currentPageUrl);
            threadWait(5000);

            try{
                WebElement nextPage = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Next")));
                nextPage.click();
            } catch (TimeoutException e) {
                System.err.println("Next page button not found within the specified timeout: " + e.getMessage());
                break;  // Break the loop if the button is not found
            } catch(NoSuchElementException e){
                System.err.println("Next page not found: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.err.println("An unexpected exception occurred: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }

//        System.out.println("Crawling of Rental Leads Complete Successfully!!");
        return gotLeads;
    }
    public static void main(String[] args){
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Enter the number of pages(<5 in order to prevent longer running times) you want to scrape: ");
//        int numPages = sc.nextInt();
//        sc.nextLine();
//        System.out.print("Enter the city where you are looking for rentals: ");
//        String city = sc.nextLine();
////        List<List<String>> rentalLeads = new ArrayList<>();
//
//        System.setProperty("webdriver.chrome.driver","E:/Semester-1/ACC/RealEstateCrawler/chromedriver-win64/chromedriver-win64/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary("E:/Semester-1/ACC/RealEstateCrawler/chrome-win64/chrome-win64/chrome.exe");
//        options.addArguments("--deny-permission-prompts");
////        options.addArguments("--headless");
//        WebDriver driver = new ChromeDriver(options);
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//
//        driveCrawling(driver,wait, numPages, city);

    }
}
