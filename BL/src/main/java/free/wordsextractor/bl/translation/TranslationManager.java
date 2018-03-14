package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.OnlyWordsDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Translations manager of the extracted text
 */
public class TranslationManager {
    private static final Logger log = LogManager.getLogger(TranslationManager.class);        /** logger */

    private static final String KNOWN_WORDS_FILE_NAME = "known.dict";                        /** the file name of the file containing known words */
    final private Dictionary dict;                                                           /** the dictionary containing words extracted and without known words */

    /**
     * Constructor of manager from the given dictionary of extracted words
     * @param dictionary  The dictionary of extracted words
     */
    public TranslationManager(final Dictionary dictionary) {
        dict = dictionary;
    }

    /**
     * Remove known words from the dictionary of extracted words
     */
    public void removeKnownWords() {
        final File knownWordsFile = new File(KNOWN_WORDS_FILE_NAME);
        if (knownWordsFile.exists()) {
            try {
                final Dictionary knownWordsDict = new OnlyWordsDictionary(knownWordsFile.toPath());
                dict.removeWordsOfDict(knownWordsDict);
            } catch (IOException | WordsExtractorException e) {
                log.error("Can't build a dictionary from the file containing known words " + knownWordsFile.getAbsolutePath());
            }
        }
        else
            log.warn("A file with known words doesn't exist");
    }

    @NotNull
    public Dictionary getExtractedWordsDict() {
        return dict;
    }
}
