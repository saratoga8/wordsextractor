package free.wordsextractor.bl.extraction.txt_proc;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
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
     * Add a word to the dictionary
     * @param word The new word
     */
    @NotNull
    void addWord(String word);

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
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    default void saveIn(String path) throws WordsExtractorException {
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

    /**
     * Remove all the words of the given dictionary from the current one
     * @param dict The dictionary containing words for removing from the current one
     */
    @NotNull
    default void removeWordsOfDict(final Dictionary dict) {
        dict.getWords().forEach(word -> { if(!removeWord(word)) log.error("Can't remove word '" + word + "' from dictionary"); });
    }

    /**
     * Get list of words in the dictionary
     * @return The words list
     */
    @NotNull
    List<String> getWords();
}
