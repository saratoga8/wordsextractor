package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.OnlyWordsDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Translations manager of the extracted text
 */
public class TranslationManager {
    private static final Logger log = LogManager.getLogger(TranslationManager.class);        /** logger */

    public static final String KNOWN_WORDS_FILE_NAME = "knowns.dict";                        /** the file name of the file containing known words */
    final private Dictionary dict;                                                          /** the dictionary containing words extracted and without known words */

    /**
     * Constructor of manager from the given dictionary of extracted words
     * @param dictionary  The dictionary of extracted words
     */
    @NotNull
    public TranslationManager(final Dictionary dictionary) {
        dict = dictionary;
    }

    /**
     * Remove known words from the dictionary of extracted words
     */
    public void removeKnownWords(final Path knownWordsFilePath) {
        File file = knownWordsFilePath.toFile();
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                try {
                    final Dictionary knownWordsDict = new OnlyWordsDictionary(knownWordsFilePath);
                    dict.removeWordsOfDict(knownWordsDict);
                } catch (IOException | WordsExtractorException e) {
                    log.error("Can't build a dictionary from the file containing known words " + knownWordsFilePath);
                }
            }
            else
                log.error("The given path isn't file or it is but isn't readable");
        }
        else
            log.warn("A file with known words doesn't exist");
    }

    @NotNull
    public Dictionary getExtractedWordsDict() {
        return dict;
    }
}
