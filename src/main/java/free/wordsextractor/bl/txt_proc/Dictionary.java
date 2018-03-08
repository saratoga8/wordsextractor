package free.wordsextractor.bl.txt_proc;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.file_proc.extractors.TextExtractorInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Dictionary interface
 */
public interface Dictionary {
    Logger log = LogManager.getLogger(Dictionary.class);        /** logger */

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
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    default void save(String path) throws WordsExtractorException {
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
}
