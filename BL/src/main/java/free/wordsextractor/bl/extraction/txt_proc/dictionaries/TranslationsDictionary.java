package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import com.google.common.collect.Lists;
import free.wordsextractor.bl.WordsExtractorException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class TranslationsDictionary implements Dictionary {
    private final Hashtable<String, String> dict = new Hashtable<>();
    private final List<String> notTranslatedWords = new LinkedList<>();

    @Override
    public void addWord(String word) throws WordsExtractorException {
        throw new WordsExtractorException("Only word shouldn't be added. A word should be added with translation");
    }

    @NotNull
    @Override
    public void addTranslation(String word, String translation) {
        if (!translation.isEmpty())
            dict.put(word, translation);
        else
            notTranslatedWords.add(word);
    }

    @Override
    public boolean contains(String word) {
        return dict.contains(word) || notTranslatedWords.contains(word);
    }

    @Override
    public boolean removeWord(String word) {
        return (dict.contains(word)) ? (dict.remove(word) != null): notTranslatedWords.remove(word);
    }

    @Override
    public String getTranslation(String word) {
        return dict.get(word);
    }

    @Override
    public List<String> getWords() {
        final List<String> unitedList = Lists.newArrayList(dict.keySet());
        unitedList.addAll(notTranslatedWords);
        return unitedList;
    }

    @Override
    public List<?> getTranslations() {
        return new ArrayList<>(dict.values());
    }

    @Override
    public List<?> getSortedTranslations() {
        final List<String> translatedWords = Lists.newArrayList(dict.keySet());
        translatedWords.sort(String.CASE_INSENSITIVE_ORDER);

        final List<String> sortedTranslations = new LinkedList<>();
        translatedWords.forEach(word -> sortedTranslations.add(getTranslation(word)));
        return sortedTranslations;
    }

    @NotNull
    @Override
    public List<String> getNotTranslatedWords() {
        return notTranslatedWords;
    }
}
