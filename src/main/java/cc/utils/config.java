package cc.utils;

import java.util.Arrays;
import java.util.List;

public class config {
    public static final String HTMLFolderPathLiv="DB/crawled_liv.rent";
    public static final String HTMLFolderPathRental="DB/crawled_rental.ca";
    public static final String HTMLFolderPathRentSeeker = "DB/crawled_rentSeeker.ca";
    public static final String txtFolderPathLiv = "DB/parsed_liv.rent";
    public static final String txtFolderPathRental = "DB/parsed_rental.ca";
    public static final String txtFolderPathRentSeeker = "DB/parsed_rentSeeker.ca";
    public static final String descriptionLiv="DB/description_liv.rent";
    public static final String descriptionRental="DB/description_rental.ca";
    public static final String descriptionRentSeeker="DB/description_rentSeeker.ca";
    public static final String lastRunTimeFilePathLiv = "logs/lastRunTimeLiv.txt";
    public static final String lastRunTimeFilePathRental = "logs/lastRunTimeRental.txt";
    public static final String lastRunTimeFilePathRentSeeker="logs/lastRunTimeRentSeeker.txt";

    // driver is in the /lib folder; unzip
     public static final String chromeDriverPath = "lib/chromedriver.exe"; // Path of chromedriver.exe
//    // public static final String chromeBrowserPath = "chrome-win64/chrome.exe"; // Path of chrome.exe
     public static final String stopwordsFilePath = "stopwords.txt"; // Path to stopwords file -> file is at the root of the project
     public static final String searchFrequencyFilePath = "searchFrequencies.txt"; //give full path of searchFrequencies.txt
     public static final String cityNamesFilePath="CityNames.txt"; // give full path of CityNames.txt

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

    public static final String cityRegex = "^[a-zA-Z]+$";
    public static final String linkRegex = "(https?://[a-zA-Z0-9./-]+)";
    public static final int minCityLength = 15;

}
