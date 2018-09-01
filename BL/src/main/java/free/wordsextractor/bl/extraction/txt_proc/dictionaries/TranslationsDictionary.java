package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import com.google.common.collect.Lists;
import free.wordsextractor.bl.WordsExtractorException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Dictionary of words and translations
 */
public class TranslationsDictionary implements Dictionary, Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(TranslationsDictionary.class);                /* logger */

    private final Hashtable<String, String> dict = new Hashtable<>();
    /**
     * dictionary of words and translations
     */
    private final List<String> notTranslatedWords = Collections.synchronizedList(new LinkedList<>());    /** not translated words */

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWord(String word) throws WordsExtractorException {
        throw new WordsExtractorException("Only word shouldn't be added. A word should be added with translation");
    }

    /**
     * Add translation of a word
     *
     * @param word        The word
     * @param translation The word's translation
     */
    @Override
    synchronized public void addTranslation(String word, String translation) {
        if (!StringUtils.isBlank(word)) {
            if (translation != null) {
                if (!translation.isEmpty())
                    dict.put(word, translation);
                else
                    notTranslatedWords.add(word);
            } else
                log.error("The given translation of '" + word + "' is NULL");
        } else
            log.error("The given word is NULL or EMPTY");
    }

    /**
     * Check a given word is in the dictionary
     *
     * @param word The checked word
     * @return true - The word is in the dictionary
     */
    @Override
    synchronized public boolean contains(String word) {
        OperationOnWord<Boolean> operation = wd -> dict.containsKey(wd) || notTranslatedWords.contains(wd);
        return Dictionary.secureOperationOnWord(word, operation, Boolean.FALSE);
    }

    /**
     * Remove the given word from the dictionary
     *
     * @param word The word for removing
     * @return true - The word removed successfully
     */
    @Override
    synchronized public boolean removeWord(String word) {
        OperationOnWord<Boolean> operation = wd -> (dict.containsKey(word)) ? (dict.remove(word) != null) : notTranslatedWords.remove(word);
        return Dictionary.secureOperationOnWord(word, operation, Boolean.FALSE);
    }

    /**
     * Get translation of the word
     *
     * @param word The word
     * @return Word's translation
     */
    @Override
    synchronized public String getTranslation(String word) {
        return Dictionary.secureOperationOnWord(word, (wrd -> dict.getOrDefault(wrd, "")), "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAsBinIn(String path) throws IOException {
        saveAsBinIn(path, this);
    }

    /**
     * Get all the words of the dictionary
     *
     * @return The list of dictionary's words
     */
    @Override
    synchronized public List<String> getWords() {
        final List<String> unitedList = Lists.newArrayList(dict.keySet());
        unitedList.addAll(notTranslatedWords);
        return unitedList;
    }

    /**
     * Get list of translations
     *
     * @return The translations
     */
    @Override
    synchronized public List<?> getTranslations() {
        return new ArrayList<>(dict.values());
    }

    /**
     * Get sorted list of translations(sorted by translated words)
     *
     * @return The sorted list
     */
    @Override
    synchronized public List<?> getSortedTranslations() {
        final List<String> translatedWords = Lists.newArrayList(dict.keySet());
        translatedWords.sort(String.CASE_INSENSITIVE_ORDER);

        final List<String> sortedTranslations = new LinkedList<>();
        translatedWords.forEach(word -> sortedTranslations.add(getTranslation(word)));
        return sortedTranslations;
    }

    /**
     * Get list of words they aren't translated
     *
     * @return The list of words
     */
    @NotNull
    @Override
    public List<String> getNotTranslatedWords() {
        return notTranslatedWords;
    }
}
