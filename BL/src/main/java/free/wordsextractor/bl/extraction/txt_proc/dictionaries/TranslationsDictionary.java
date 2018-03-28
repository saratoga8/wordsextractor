package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.google.common.collect.Lists;
import free.wordsextractor.bl.WordsExtractorException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TranslationsDictionary implements Dictionary {
    private final Hashtable<String, String> dict = new Hashtable<>();

    @Override
    public void addWord(String word) throws WordsExtractorException {
        throw new WordsExtractorException("Only word shouldn't be added. A word should be added with translation");
    }

    @Override
    public void addTranslation(String word, String translation) {
        dict.put(word, translation);
    }

    @Override
    public boolean contains(String word) {
        return dict.contains(word);
    }

    @Override
    public boolean removeWord(String word) {
        return (dict.remove(word) != null);
    }

    @Override
    public String getTranslation(String word) {
        return dict.get(word);
    }

    @Override
    public List<String> getWords() {
        return Lists.newArrayList(dict.keySet());
    }

    @Override
    public List<?> getTranslations() {
        return new ArrayList<>(dict.values());
    }


}
