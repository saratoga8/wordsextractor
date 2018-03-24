package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;

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
    public Translation(Langs fromLang, Langs toLang) {
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
