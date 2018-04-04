package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * WordsStatisticsDictionary of word statistics
 */
public class WordsStatisticsDictionary implements Dictionary {
    private static final Logger log = LogManager.getLogger(WordsStatisticsDictionary.class);        /** logger */

    private final Hashtable<String, Integer> wordsStat;                                             /** words statistics */

    final static public String FILE_NAME = "stats.dict";

    /**
     * Constructor
     */
    public WordsStatisticsDictionary() {
        this.wordsStat = new Hashtable<>();
    }

    /**
     * Convert the dictionary to a SORTED string
     * @return The string representation of the dictionary
     */
    @NotNull
    synchronized public String toString() {
        final List<String> words = new ArrayList<>(wordsStat.keySet());
        words.sort(String::compareToIgnoreCase);

        final StringBuilder strBuilder = new StringBuilder();
        words.forEach(word -> strBuilder.append(word).append(" ").append(wordsStat.get(word)));

        return strBuilder.toString();
    }

    /**
     * Add word to the dictionary
     * @param word The new word
     */
    @NotNull
    synchronized public void addWord(String word) {
        final OperationOnWord<Void> operation = wrd -> {
            String strippedWrd = stripAllExceptChars(wrd);
            if (!strippedWrd.isEmpty()) {
                final Integer num = wordsStat.containsKey(strippedWrd) ? wordsStat.get(strippedWrd) + 1 : 1;
                wordsStat.put(strippedWrd, num);
            }
            return null;
        };
        Dictionary.secureOperationOnWord(word, operation);
    }

    @Override
    public void addTranslation(String word, String translation) {
        log.error("Should be used addWord() function!");
    }

    /**
     * Remove from beginning and end of the word numbers and punctuation chars
     * @param word The word for stripping
     * @return Stripped word
     */
    @NotNull
    static private String stripAllExceptChars(String word) {
        String PUNCTUATIONS_CHARS_EDGES_REG = "^\\W+|\\W+$", NUM_CHARS_EDGES_REG = "^\\d+|\\d+$";
        return word.replaceAll(PUNCTUATIONS_CHARS_EDGES_REG, "").replaceAll(NUM_CHARS_EDGES_REG, "");
    }

    /**
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    public void saveIn(String path) throws WordsExtractorException {
        log.debug ("Saving dictionary to the file " + path);

        if (!wordsStat.isEmpty())
            Dictionary.super.saveIn(path);
        else
            throw new WordsExtractorException("There are no words in the dictionary");
    }

    @NotNull
    /**
     * Get words of the dictionary
     * @return The list of words
     */
    @Override
    synchronized public List<String> getWords() {
        return new ArrayList<>(wordsStat.keySet());
    }

    @Override
    synchronized public List<Integer> getTranslations() {
        return new ArrayList<>(wordsStat.values());
    }

    @Override
    synchronized public List<?> getSortedTranslations() {
        final List<Integer> translations = new ArrayList<>(wordsStat.values());
        translations.sort(Integer::compareTo);
        return translations;
    }

    @Override
    public List<String> getNotTranslatedWords() {
        log.error("There are no translations in a statistics dictionary");
        return new LinkedList<>();
    }

    /**
     * Check a given word is in the dictionary
     * @param word The checked word
     * @return true - The word is in the dictionary
     */
    @NotNull
    synchronized public boolean contains(String word) {
        return Dictionary.secureOperationOnWord(word, wordsStat::containsKey, Boolean.FALSE);
    }

    @NotNull
    /**
     * Remove the given word from the dictionary
     * @param word The word for removing
     * @return
     */
    @Override
    synchronized public boolean removeWord(String word) {
        return Dictionary.secureOperationOnWord(word, wrd -> wordsStat.remove(wrd) != null, Boolean.FALSE);
    }

    @Override
    public String getTranslation(String word) {
        log.error("There are no translations in a statistics dictionary");
        return "";
    }
}
