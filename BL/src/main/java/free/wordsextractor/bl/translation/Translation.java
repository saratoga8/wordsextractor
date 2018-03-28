package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;

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
    public Dictionary translate(final List<String> words) {
        final TranslationsDictionary dictionary = new TranslationsDictionary();
        words.parallelStream().forEach(word -> dictionary.addTranslation(word,translate(word)) );
        return dictionary;
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
