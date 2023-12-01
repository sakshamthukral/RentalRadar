package cc.crawler;

import cc.utils.config;
import cc.utils.helper;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class crawlLiv {
    public static final String WEBSITE="https://liv.rent/";
//    public static final String HTMLFolderPath="crawled_liv.rent";
//    private static void createFolder(){
//        File folder = new File(HTMLFolderPath);
//        if (!folder.exists()) {
//            boolean folderCreated = folder.mkdirs();
//            if (folderCreated) {
//                System.out.println("Folder created: " + HTMLFolderPath);
//            } else {
//                System.err.println("Failed to create folder: " + HTMLFolderPath);
//            }
//        }
//    }
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
        helper.createFolderIfNotExists(config.descriptionLiv+"/"+inputKeyword);

        driver.findElement(By.cssSelector("button.sc-9b749994-8.lhKUEI")).click(); // Clicking on Allowing cookies when pop-up comes
        switch (inputKeyword) {
            case "Toronto", "toronto" ->  driver.get("https://liv.rent/rental-listings/city/toronto");
            case "Windsor", "windsor" -> driver.get("https://liv.rent/rental-listings/city/windsor-on");
            case "Winnipeg", "winnipeg" -> driver.get("https://liv.rent/rental-listings/city/winnipeg");
        }


        int page=1;
        while(page<=numPages){
            System.out.println("##############################################################################");
            System.out.println("Page Number: "+page);
            threadWait(6000);
            String currentPageUrl = driver.getCurrentUrl();
            System.out.println(currentPageUrl);

            List<String> links = new ArrayList<>();
            List<String> leadUrlList;
            try {
                leadUrlList = driver
                        .findElements(By.cssSelector("a[href*='/rental-listings/detail/']"))
                        .stream()
                        .map(webElement -> webElement.getAttribute("href"))
                        .filter(url -> url.contains(inputKeyword))
                        .collect(Collectors.toList());

                if(leadUrlList == null || leadUrlList.isEmpty()){
                    System.out.println("Additional page not found!!");
                    gotLeads=false;
                    break;
                }
            } catch (TimeoutException e) {
                System.out.println("Additional page not found!!");
                gotLeads=false;
                break;
            }

            for(String link: leadUrlList){
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
                WebElement descriptionSection = null;
                try {
                    WebElement descriptionParent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#Overview +* +* +*")));
                    List<WebElement> pS = descriptionParent.findElements(By.cssSelector("p"));

                    if(!pS.isEmpty()){
                        if(pS.size() > 1 && pS.get(1).isDisplayed()){
                            pS.get(1).click();
                            System.out.println("Simulating Click on 'Read More'");
                        }

                        descriptionSection = pS.get(0);
                    }
                } catch (NoSuchElementException | TimeoutException e) {
                    System.out.println("Read More button not found. Skipping...");
                } catch (Exception e) {
                    System.out.println("Read More button not found. Skipping...");
                }
                threadWait(5000);
                String descriptionText = "";
                String overviewText = driver.findElement(By.cssSelector("#Overview")).getText();

//                WebElement description = driver.findElement(By.cssSelector("p.sc-d2c88f17-0.iybCNV"));
                if(descriptionSection != null)
                    descriptionText = descriptionSection.getText();

                descriptionText = descriptionText+"\n<<<"+link+">>>";

                String descriptionFile = config.descriptionLiv+"/"+inputKeyword +"/page_"+page+"_listing_" + i + ".txt";
                try (FileWriter fileWriter = new FileWriter(descriptionFile)) {
                    fileWriter.write(overviewText); // adding overview text
                    fileWriter.write("\n");
                    fileWriter.write(descriptionText);
                    System.out.println("Description of " + link + " saved to " + descriptionFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String fileName = config.HTMLFolderPathLiv+"/"+inputKeyword +"/page_"+page+"_listing_" + i + ".html";
                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    fileWriter.write(htmlContent);
                    System.out.println("HTML content of " + link + " saved to " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                threadWait(5000);
                i++;
            }
            page++;
            driver.get(currentPageUrl);
            threadWait(10000);

            try{
                WebElement nextPage = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button#next-page")));
                nextPage.click();
            } catch (TimeoutException e) {
                System.err.println("Next page button not found within the specified timeout: " + e.getMessage());
                break;  // Break the loop if the button is not found
            } catch(NoSuchElementException e){
                System.err.println("Next page not found: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.err.println("An unexpected exception occurred: " + e.getMessage());
                e.printStackTrace();  // Print the full stack trace for debugging
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
//        System.out.print("Enter the city where you are looking for rentals: ");
//        String city = sc.nextLine();
//        List<List<String>> rentalLeads = new ArrayList<>();
//
//        System.setProperty("webdriver.chrome.driver","E:/Semester-1/ACC/RealEstateCrawler/chromedriver-win64/chromedriver-win64/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary("E:/Semester-1/ACC/RealEstateCrawler/chrome-win64/chrome-win64/chrome.exe");
//        options.addArguments("--deny-permission-prompts");
//        options.addArguments("--headless");
//        WebDriver driver = new ChromeDriver(options);
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//
//        driveCrawling(driver,wait, numPages, city);

    }

}
