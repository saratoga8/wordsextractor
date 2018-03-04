package free.wordsextractor.bl.txt_proc;

import com.drew.lang.annotations.NotNull;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * Dictionary of word statistics
 */
public class Dictionary {
    private static final Logger log = LogManager.getLogger(Dictionary.class);                       /** logger */

    private final Hashtable<String, Integer> wordsStat;                                             /** words statistics */

    /**
     * Constructor
     */
    public Dictionary() {
        this.wordsStat = new Hashtable<String, Integer>();
    }

    /**
     * Convert the dictionary to a string
     * @return The string representation of the dictionary
     */
    public String toString() {
        final List<String> wordsSet = new ArrayList<>(wordsStat.keySet());
        Collections.sort(wordsSet, (String str1, String str2) -> str1.compareToIgnoreCase(str2));

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
    private String stripAllExceptChars(String word) {
        return word.replaceAll("^\\W+|\\W+$", "").replaceAll("^\\d+|\\d+$", "");
    }

    public void save(String path) {
    }

    public boolean contains(String word) {
        return wordsStat.containsKey(word);
    }
}
