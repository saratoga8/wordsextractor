package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Dictionary interface
 */
public interface Dictionary {
    Logger log = LogManager.getLogger(Dictionary.class);        /* logger */

    /**
     * Add only a word to the dictionary without translation
     * @param word The new word
     */
    @NotNull
    void addWord(String word) throws WordsExtractorException;

    /**
     * Add translation to the dictionary
     * @param word The word
     * @param translation The word's translation
     */
    @NotNull
    void addTranslation(String word, String translation);

    /**
     * Check a given word is in the dictionary
     * @param word The checked word
     * @return true - The word is in the dictionary
     */
    @NotNull
    boolean contains(String word);

    /**
     * Remove the given word from dictionary
     * @param word The word for removing
     * @return true - The word has been removed successfully. false - The word can't be removed
     */
    @NotNull
    boolean removeWord(String word);

    /**
     * Get translation of the given word
     * @param word The word
     * @return The word's translation
     */
    @NotNull
    String getTranslation(String word);

    /**
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    default void saveIn(String path) throws WordsExtractorException {
        final File file = new File(path);
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

    /**
     * Remove all the words of the given dictionary from the current one
     * @param dict The dictionary containing words for removing from the current one
     */
    @NotNull
    default void removeWordsOfDict(final Dictionary dict) {
        if(dict != null)
            dict.getWords().forEach(word -> { if(!removeWord(word)) log.warn("Can't remove word '" + word + "' from dictionary"); });
        else
            log.error("The given dictionary is NULL");
    }

    /**
     * Get list of words in the dictionary
     * @return The words list
     */
    @NotNull
    List<String> getWords();

    /**
     * Get list of translations
     * @return The translations
     */
    @NotNull
    List<?> getTranslations();

    /**
     * Get sorted list of translations(sorted by translated words)
     * @return The sorted list
     */
    @NotNull
    List<?> getSortedTranslations();

    /**
     * Get list of words they aren't translated
     * @return The list of words
     */
    List<String> getNotTranslatedWords();

    @FunctionalInterface
    interface OperationOnWord<T> {
        T apply(String word);
    }

    static <T> T secureOperationOnWord(String word, OperationOnWord<T> operationOnWord, T defaultVal) {
        if(!StringUtils.isBlank(word))
            return operationOnWord.apply(word);
        else
            log.error("The given word is NULL or EMPTY");
        return defaultVal;
    }

    static <T> void secureOperationOnWord(String word, OperationOnWord<T> operationOnWord) {
        if(!StringUtils.isBlank(word))
            operationOnWord.apply(word);
        else
            log.error("The given word is NULL or EMPTY");
    }
}
