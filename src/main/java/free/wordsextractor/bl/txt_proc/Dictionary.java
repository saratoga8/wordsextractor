package free.wordsextractor.bl.txt_proc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Dictionary of word statistics
 */
public class Dictionary {
    private final Hashtable<String, Integer> wordsStat;

    public Dictionary() {
        this.wordsStat = new Hashtable<String, Integer>();
    }

    public List<String> getWords() {
        return new ArrayList<>(wordsStat.keySet());
    }

    public void addWord(String word) {
        Integer num = wordsStat.containsKey(word) ? wordsStat.get(word) + 1: 1;
        wordsStat.put(word, num);
    }

    public void save(String path) {
    }
}
