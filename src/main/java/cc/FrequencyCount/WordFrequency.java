package cc.FrequencyCount;

import java.util.Map;

public class WordFrequency {
    public String filename;
    public Map<String, Integer> wordsCount;

    public WordFrequency(String filename, Map<String, Integer> wordsCount) {
        this.filename = filename;
        this.wordsCount = wordsCount;
    }
}
