package cc.FrequencyCount;

import java.util.List;

class FrequentWord {
    public int count;
    public List<SimilarWord> similarWords;

    public FrequentWord(int count, List<SimilarWord> similarWords) {
        this.count = count;
        this.similarWords = similarWords;
    }
}
