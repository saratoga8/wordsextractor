package free.wordsextractor.bl.translation;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Translation from one language to another one
 */
public abstract class Translation {
    private static final Logger log = LogManager.getLogger(Translation.class);        /* logger */

    protected final Langs fromLang;                       /* translate from language */
    protected final Langs toLang;                         /* translate to language */

    /**
     * Get language translation from
     * @return Language enum
     */
    public Langs getFromLang() { return fromLang; }

    /**
     * Get language translation to
     * @return Language enum
     */
    public Langs getToLang()   { return toLang;   }

    /**
     * Constructor
     * @param fromLang Language translation from
     * @param toLang Language translation to
     */
    public Translation(final Langs fromLang, final Langs toLang) {
        this.fromLang = fromLang;
        this.toLang = toLang;
    }

    /**
     * Translate the given word
     * @param word The word
     * @return Translation string
     */
    public abstract String translate(String word) throws WordsExtractorException;

    /**
     * Translate the given words
     * @param words The given list of words
     * @return Hash table of the translations
     */
    public Dictionary translate(final List<String> words) {
        final TranslationsDictionary dictionary = new TranslationsDictionary();
        words.parallelStream().forEach(word -> {
            try {
                dictionary.addTranslation(word, translate(word));
            } catch (WordsExtractorException e) {
                log.error("Can't translate the word '" + word + "': " + e);
            }
        });
        return dictionary;
    }

    /**
     * Languages and their codes
     */
    public enum Langs {
        ENG("en"), RUS("ru"), TEST("test");
        final private String val;

        Langs(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }

        public static Langs getLang(String langName) throws WordsExtractorException {
            switch (langName.toLowerCase()) {
                case "en":  return ENG;
                case "eng": return ENG;
                case "ru":  return RUS;
                case "rus": return RUS;
            }
            throw new WordsExtractorException("Unknown language: " + langName);
        }
    }
}
