package cc.suggestions.TopCities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import cc.utils.config;
public class TopSearchedCities {
    
    public static void main(String[] args) {
        try {
            String DocLoc = config.searchFrequencyFilePath;
            Map<String, Integer> cityFrequencies = rdCityFrq(DocLoc);
            String[] topCities = getTCities(cityFrequencies, 3);

            System.out.println("Here are the top 3 most searched cities below:");
            for (String city : topCities) {
                System.out.println(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// This method is responsible for reading the contents of the text file and storing the city names along with their frequencies in a Map<String, Integer>.
// The BufferedReader is used to efficiently read lines from the file and each line is split into two parts using regex.
// The city name and its frequency are then added to the cityFrequencies map.
// The time complexity for this part is O(n), where n is the number of lines in the file, as each line is processed once.
    public static Map<String, Integer> rdCityFrq(String filePath) throws IOException {
        Map<String, Integer> cityFrequencies = new HashMap<>();

        try (BufferedReader rdr = new BufferedReader(new FileReader(filePath))) {
            String row;
            while ((row = rdr.readLine()) != null) {
                String[] prts = row.split("\\s+");
                if (prts.length == 2) {
                    String city = prts[0].toLowerCase();
                    int frq = Integer.parseInt(prts[1]);
                    cityFrequencies.put(city, frq);
                }
            }
        }

        return cityFrequencies;
    }
// This method takes the cityFrequencies map and retrieves the top cities based on their frequencies.
// Retrieve the entries with the highest frequencies. The comparator used for the priority queue ensures that entries are ordered by frequency in descending order.
// The priority queue is populated with all entries from the cityFrequencies map. 
// The retrieved city names are stored in the topCities array.
    public static String[] getTCities(Map<String, Integer> cityFrequencies, int topCount) {
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
                (a, b) -> Integer.compare(b.getValue(), a.getValue())
        );

        maxHeap.addAll(cityFrequencies.entrySet());

        String[] tCities = new String[topCount];
        for (int initial2 = 0; initial2 < topCount; initial2++) {
            Map.Entry<String, Integer> etry = maxHeap.poll();
            if (etry != null) {
                tCities[initial2] = etry.getKey();
            }
        }
        return tCities;
    }
}
