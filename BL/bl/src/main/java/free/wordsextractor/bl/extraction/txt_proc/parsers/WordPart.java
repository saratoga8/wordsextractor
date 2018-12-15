package free.wordsextractor.bl.extraction.txt_proc.parsers;

import free.wordsextractor.bl.WordsExtractorException;
import org.apache.http.util.TextUtils;

public abstract class WordPart {
    final protected String str;
    final protected String name;

    protected WordPart(String str, String name) throws WordsExtractorException {
        if (str == null)
            throw new WordsExtractorException(name + " can't be NULL");
        if (TextUtils.isEmpty(name))
            throw new WordsExtractorException("A name of a word part can't be NULL or EMPTY");
        this.str = str;
        this.name = name;
    }

    abstract boolean isIn(String word);

    public String toString() {
        return str;
    }
}
