package free.wordsextractor.bl.translation;

public abstract class Translation {
    protected Langs fromLang;
    protected Langs toLang;

    public Langs getFromLang() { return fromLang; }
    public Langs getToLang()   { return toLang;   }

    public Translation(Langs fromLang, Langs toLang) {
        this.fromLang = fromLang;
        this.toLang = toLang;
    }

    public abstract String translate(String word);

    enum Langs {
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
