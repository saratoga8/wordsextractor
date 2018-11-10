package free.wordsextractor.bl.extraction.txt_proc.parsers;

import free.wordsextractor.bl.WordsExtractorException;

public class Suffix extends WordPart {
    public static String NAME = "suffix";

    public Suffix(String str) throws WordsExtractorException {
        super(str, NAME);
    }

    @Override
    boolean in(String word) {
        return word.endsWith(str);
    }
}
