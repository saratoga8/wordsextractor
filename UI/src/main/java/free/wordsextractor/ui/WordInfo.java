package free.wordsextractor.ui;

public class WordInfo {
    final private String word;
    final private String stats;
    final private String translation;

    public WordInfo(String word, String translation, String stats) {
        this.word = word;
        this.stats = stats;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getStats() {
        return stats;
    }

    public String getTranslation() {
        return translation;
    }
}
