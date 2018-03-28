package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TranslationsDictionary implements Dictionary {
    private final Hashtable<String, String> words = new Hashtable<>();

    @Override
    public void addWord(String txt) {
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }

    @Override
    public boolean removeWord(String word) {
        log.error("Removing from a dictionary of translated words hasn't implemented");
        return false;
    }

    @Override
    public List<String> getWords() {
        return new ArrayList<>(words.keySet());
    }
}
