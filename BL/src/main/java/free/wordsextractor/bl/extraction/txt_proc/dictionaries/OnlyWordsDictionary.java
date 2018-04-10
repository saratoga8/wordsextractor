package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.google.common.collect.Sets;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Dictionary containing only words
 */
public class OnlyWordsDictionary implements Dictionary {
    final private static Logger log = LogManager.getLogger(OnlyWordsDictionary.class);        /* logger */
    final private Set<String> words;                                                          /* words of the dictionary */

    /**
     * Constructor of the dictionary from the given file
     * @param path The path of the file containing words for dictionary
     * @throws IOException Can't open or find the file
     */
    public OnlyWordsDictionary(final Path path) throws IOException {
        words = Collections.synchronizedSet(Sets.newHashSet(WordsExtractor.extractWordsFromFile(path)));
    }

    /**
     * Construct an empty dictionary
     */
    public OnlyWordsDictionary() {
        this.words = new HashSet<>();
    }

    /**
     * Add a new word to the dictionary in sorted order
     * @param word The new word
     */
    @Override
    synchronized public void addWord(String word) {
        final OperationOnWord<Void> operation = wrd ->  {
            if (!words.add(wrd))
                log.error("Can't add the give word '" + word + "' to the dictionary");
            return null;
        };
        Dictionary.secureOperationOnWord(word, operation);
    }

    /**
     * Add translation of the word
     * @param word The word
     * @param translation The word's translation
     */
    @Override
    public void addTranslation(String word, String translation) {
        log.error("Should be used addWord() function!");
    }

    /**
     * Check the dictionary contains the given word
     * @param word The checked word
     * @return true - The word is in the dictionary
     */
    @Override
    synchronized public boolean contains(String word) {
        return Dictionary.secureOperationOnWord(word, words::contains, false);
    }

    /**
     * Remove the given word from the dictionary
     * @param word The word for removing
     * @return true - The word removed successfully
     */
    @Override
    synchronized public boolean removeWord(String word) {
        return Dictionary.secureOperationOnWord(word, words::remove, false);
    }

    /**
     * Get translation of the word
     * @param word The word
     * @return Word's translation
     */
    @Override
    public String getTranslation(String word) {
        log.error("There are no translations in a only words dictionary");
        return "";
    }

    /**
     * Get all the words of the dictionary
     * @return The list of dictionary's words
     */
    @Override
    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    /**
     * Get list of translations only
     * @return The list of translations
     */
    @Override
    public List<String> getTranslations() {
        log.error("There are no translations in a dictionary with only words!");
        return new LinkedList<>();
    }

    /**
     * Get list of translations
     * @return The translations
     */
    @Override
    public List<?> getSortedTranslations() {
        log.error("There are no translations in a dictionary with only words!");
        return new LinkedList<>();
    }

    /**
     * Get list of words they aren't translated
     * @return The list of words
     */
    @Override
    public List<String> getNotTranslatedWords() {
        log.error("There are no translations in a dictionary with only words!");
        return new LinkedList<>();
    }
}
