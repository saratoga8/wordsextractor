package free.wordsextractor.bl.txt_proc;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * WordsStatisticsDictionary of word statistics
 */
public class WordsStatisticsDictionary implements Dictionary {
    private static final Logger log = LogManager.getLogger(WordsStatisticsDictionary.class);        /** logger */

    private final Hashtable<String, Integer> wordsStat;                                             /** words statistics */

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
    public String toString() {
        final List<String> wordsSet = new ArrayList<>(wordsStat.keySet());
        wordsSet.sort(String::compareToIgnoreCase);

        final StringBuilder strBuilder = new StringBuilder();
        wordsSet.stream().forEach(word -> strBuilder.append(word + " " + wordsStat.get(word)));

        return strBuilder.toString();
    }

    /**
     * Add word to the dictionary
     * @param word The new word
     */
    @NotNull
    public void addWord(String word) {
        log.debug("Add word '" + word + "' to dictionary");
        if (StringUtils.isBlank(word)) {
            log.error("Can't add NULL or EMPTY word to dictionary");
            return;
        }
        String strippedWrd = stripAllExceptChars(word);
        if (strippedWrd.isEmpty())
            return;

        final Integer num = wordsStat.containsKey(strippedWrd) ? wordsStat.get(strippedWrd) + 1: 1;
        wordsStat.put(strippedWrd, num);
    }

    /**
     * Remove from beginning and end of the word numbers and punctuation chars
     * @param word The word for stripping
     * @return Stripped word
     */
    @NotNull
    private String stripAllExceptChars(String word) {
        String PUNCTUATIONS_CHARS_EDGES_REG = "^\\W+|\\W+$", NUM_CHARS_EDGES_REG = "^\\d+|\\d+$";
        return word.replaceAll(PUNCTUATIONS_CHARS_EDGES_REG, "").replaceAll(NUM_CHARS_EDGES_REG, "");
    }

    /**
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    public void save(String path) throws WordsExtractorException {
        log.debug ("Saving dictionary to the file " + path);

        if (!wordsStat.isEmpty())
            Dictionary.super.save(path);
        else
            throw new WordsExtractorException("There are no words in the dictionary");
    }

    /**
     * Check a given word is in the dictionary
     * @param word The checked word
     * @return true - The word is in the dictionary
     */
    @NotNull
    public boolean contains(String word) {
        return wordsStat.containsKey(word);
    }
}
