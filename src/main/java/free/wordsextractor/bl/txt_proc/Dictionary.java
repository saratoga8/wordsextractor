package free.wordsextractor.bl.txt_proc;

import java.util.Hashtable;

/**
 * Dictionary of word statistics
 */
public class Dictionary {
    private final Hashtable<String, Integer> wordsStat;

    public Dictionary() {
        this.wordsStat = new Hashtable<String, Integer>();
    }

    public void save(String path) {

    }
}
