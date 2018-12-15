package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.translation.Translation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * WordsStatisticsDictionary of word statistics
 */
public class WordsStatisticsDictionary extends Dictionary implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(WordsStatisticsDictionary.class);        /** logger */

    private final Hashtable<String, Integer> wordsStat;                                             /** words statistics */

//    final static public String FILE_NAME = "stats.dict";

    /**
     * {@inheritDoc}
     */
    public WordsStatisticsDictionary(final Translation.Langs lang) throws WordsExtractorException {
        super(lang);
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
            String strippedWrd = Dictionary.stripAllExceptChars(wrd);
            if (!strippedWrd.isEmpty()) {
                final Integer num = wordsStat.containsKey(strippedWrd) ? wordsStat.get(strippedWrd) + 1 : 1;
                wordsStat.put(strippedWrd, num);
            }
            return null;
        };
        Dictionary.secureOperationOnWord(word, operation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTranslation(String word, String translation) {
        log.error("Should be used addWord() function!");
    }


    /**
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    public void saveAsTxtIn(String path) throws WordsExtractorException {
        log.debug ("Saving dictionary to the file " + path);

        if (!wordsStat.isEmpty())
            super.saveAsTxtIn(path);
        else
            throw new WordsExtractorException("There are no words isIn the dictionary");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAsBinIn(String path) throws IOException {
        saveAsBinIn(path, this);
    }

    /**
     * Get words of the dictionary
     * @return The list of words
     */
    @Override
    synchronized public List<String> getWords() {
        return new ArrayList<>(wordsStat.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public List<Integer> getTranslations() {
        return new ArrayList<>(wordsStat.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public List<?> getSortedTranslations() {
        final List<Integer> translations = new ArrayList<>(wordsStat.values());
        translations.sort(Integer::compareTo);
        return translations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getNotTranslatedWords() {
        log.error("There are no translations isIn a statistics dictionary");
        return new LinkedList<>();
    }

    /**
     * Check a given word is isIn the dictionary
     * @param word The checked word
     * @return true - The word is isIn the dictionary
     */
    @NotNull
    synchronized public boolean contains(String word) {
        return Dictionary.secureOperationOnWord(word, wordsStat::containsKey, Boolean.FALSE);
    }

    /**
     * Remove the given word from the dictionary
     * @param word The word for removing
     * @return true The word has been removed successfully
     */
    @Override
    synchronized public boolean removeWord(String word) {
        return Dictionary.secureOperationOnWord(word, wrd -> wordsStat.remove(wrd) != null, Boolean.FALSE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTranslation(String word) {
        final Integer foundNum = wordsStat.get(word);
        if (foundNum == null)
            log.error("There is no word '" + word + "'");
        return (foundNum != null) ? foundNum.toString(): "";
    }
}
