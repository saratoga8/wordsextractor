package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;

import java.util.Hashtable;
import java.util.List;

/**
 * Translation from one language to another one
 */
public abstract class Translation {
    protected Langs fromLang;                       /** translate from language */
    protected Langs toLang;                         /** translate to language */

    @NotNull
    /**
     * Get language translation from
     * @return Language enum
     */
    public Langs getFromLang() { return fromLang; }

    @NotNull
    /**
     * Get language translation to
     * @return Language enum
     */
    public Langs getToLang()   { return toLang;   }

    @NotNull
    /**
     * Constructor
     * @param fromLang Language translation from
     * @param toLang Language translation to
     */
    public Translation(final Langs fromLang, final Langs toLang) {
        this.fromLang = fromLang;
        this.toLang = toLang;
    }

    @NotNull
    /**
     * Translate the given word
     * @param word The word
     * @return Translation string
     */
    public abstract String translate(String word);

    @NotNull
    /**
     * Translate the given words
     * @param words The given list of words
     * @return Hash table of the translations
     */
    public Hashtable<String, String> translate(final List<String> words) throws WordsExtractorException {
        if(!words.isEmpty()) {
            final Hashtable<String, String> translations = new Hashtable<>();
            words.parallelStream().forEach(word -> translations.put(word,translate(word)) );
            return translations;
        }
        else
            throw new WordsExtractorException("The given list of words for translation is empty");
    }

    /**
     * Languages and their codes
     */
    public enum Langs {
        ENG("en"), RUS("ru");
        final private String val;

        Langs(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }
}
