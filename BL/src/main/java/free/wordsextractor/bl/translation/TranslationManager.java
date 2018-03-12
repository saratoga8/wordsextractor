package free.wordsextractor.bl.translation;

import free.wordsextractor.bl.extraction.txt_proc.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.OnlyWordsDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class TranslationManager {
    private static final Logger log = LogManager.getLogger(TranslationManager.class);        /** logger */

    private static final String KNOWN_WORDS_FILE_NAME = "known.dict";

    final private Dictionary dict;

    public TranslationManager(final Dictionary dictionary) {
        dict = dictionary;
    }

    public void removeKnownWords() {
        final File knownWordsFile = new File(KNOWN_WORDS_FILE_NAME);
        if (knownWordsFile.exists()) {
            Dictionary knownWordsDict = new OnlyWordsDictionary(knownWordsFile.toPath());
            dict.removeWordsOfDict(knownWordsDict);
        }
    }
}
