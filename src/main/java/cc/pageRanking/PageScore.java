package cc.pageRanking;

public class PageScore {
    public String document;
    public Integer score;

    private PageScore(String document, Integer score) {
        this.document = document;
        this.score = score;
    }

    public static PageScore of(String document, Integer score) {
        return new PageScore(document, score);
    }
}
