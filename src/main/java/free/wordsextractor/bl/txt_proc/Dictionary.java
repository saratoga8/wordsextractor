package free.wordsextractor.bl.txt_proc;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.file_proc.extractors.TextExtractorInterface;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
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
        this.wordsStat = new Hashtable<>();
    }

    /**
     * Convert the dictionary to a SORTED string
     * @return The string representation of the dictionary
     */
    @NotNull
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

        if (!wordsStat.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName(TextExtractorInterface.CHAR_SET), StandardOpenOption.CREATE)) {
                    writer.write(toString());
                    writer.flush();
                }
                catch (IOException e) {
                    throw new WordsExtractorException("Can't write dictionary to file: " + e);
                }
            }
            else
                throw new WordsExtractorException("The path " + path + " doesn't exist");
        }
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
