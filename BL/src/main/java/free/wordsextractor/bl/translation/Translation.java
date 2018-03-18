package free.wordsextractor.bl.translation;

public abstract class Translation {
    enum Langs {ENG, RUS}

    protected Langs fromLang;
    protected Langs toLang;

    public Langs getFromLang() { return fromLang; }
    public Langs getToLang()   { return toLang;   }

    public Translation(Langs fromLang, Langs toLang) {
        this.fromLang = fromLang;
        this.toLang = toLang;
    }

    public abstract String translate(String word);
}
