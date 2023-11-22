package cc.utils;

import java.util.Arrays;
import java.util.List;

public class config {
    public static final String HTMLFolderPathLiv="crawled_liv.rent";
    public static final String HTMLFolderPathRental="crawled_rental.ca";
    public static final String HTMLFolderPathRentSeeker = "crawled_rentSeeker.ca";
    public static final String txtFolderPathLiv = "parsed_liv.rent";
    public static final String lastRunTimeFilePathLiv = "lastRunTimeLiv.txt";
    public static final String txtFolderPathRental = "parsed_rental.ca";
    public static final String lastRunTimeFilePathRental = "lastRunTimeRental.txt";
    public static final String txtFolderPathRentSeeker = "parsed_rentSeeker.ca";
    public static final String lastRunTimeFilePathRentSeeker="lastRunTimeRentSeeker.txt";
    public static final String chromeDriverPath = "chromedriver-win64/chromedriver.exe";
    public static final String chromeBrowserPath = "chrome-win64/chrome.exe";
   // public static final String chromeDriverPath = "C:/Users/aniru/Downloads/acc_assignment_3/chromedriver-win64/chromedriver-win64/chromedriver.exe";
   // public static final String chromeBrowserPath = "C:/Program Files/Google/Chrome/Application/chrome.exe";
    public static final String stopwordsFilePath = "stopwords.txt"; // Path to stopwords file

    public static final String[] cities = {"Toronto", "Montreal", "Calgary", "Ottawa", "Edmonton", "Winnipeg",
                "Vancouver", "Brampton", "Hamilton", "Surrey", "Quebec", "Halifax", "Laval",
                "London", "Markham", "Vaughan", "Gatineau", "Saskatoon", "Kitchener", "Longueuil",
                "Burnaby", "Windsor", "Regina", "Oakville", "Richmond", "Burlington",
                "Oshawa", "Sherbrooke", "Sudbury", "Abbotsford", "Coquitlam", "Barrie", "Saguenay",
                "Kelowna", "Guelph", "Whitby", "Cambridge", "Catharines", "Milton", "Langley", "Kingston",
                "Ajax", "Waterloo", "Terrebonne", "Saanich", "Delta", "Brantford", "Clarington", "Nanaimo",
                "Strathcona", "Pickering", "Lethbridge", "Kamloops", "Richelieu", "Niagara", "Cape Breton",
                "Chilliwack", "Victoria", "Brossard", "Newmarket", "Repentigny", "Peterborough",
                "Moncton", "Drummondville", "Caledon", "Airdrie", "Sarnia", "Granby", "Fredericton",
                "Aurora", "Mirabel", "Blainville", "Welland", "Belleville"};
    public static List<String> CITIES = Arrays.asList("toronto", "montreal", "calgary", "ottawa", "edmonton", "winnipeg",
            "vancouver", "brampton", "hamilton", "surrey", "quebec", "halifax", "laval",
            "london", "markham", "vaughan", "gatineau", "saskatoon", "kitchener", "longueuil",
            "burnaby", "windsor", "regina", "oakville", "richmond", "burlington",
            "oshawa", "sherbrooke", "sudbury", "abbotsford", "coquitlam", "barrie", "saguenay",
            "kelowna", "guelph", "whitby", "cambridge", "catharines", "milton", "langley", "kingston",
            "ajax", "waterloo", "terrebonne", "Saanich", "delta", "brantford", "clarington", "nanaimo",
            "strathcona", "pickering", "lethbridge", "kamloops", "richelieu", "niagara", "cape Breton",
            "chilliwack", "victoria", "brossard", "newmarket", "repentigny", "peterborough",
            "moncton", "drummondville", "caledon", "airdrie", "sarnia", "granby", "fredericton",
            "aurora", "mirabel", "blainville", "welland", "belleville");

    public static final String searchFrequencyFilePath = "src/main/java/cc/SearchFrequency/searchFrequencies.txt";
    public static final String cityRegex = "^[a-zA-Z]+$";
    public static final int minCityLength = 4;
}
