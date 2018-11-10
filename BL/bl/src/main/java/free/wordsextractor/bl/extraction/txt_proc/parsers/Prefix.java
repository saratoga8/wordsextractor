package free.wordsextractor.bl.extraction.txt_proc.parsers;

import free.wordsextractor.bl.WordsExtractorException;

public class Prefix extends WordPart {
    public static String NAME = "prefix";

    public Prefix(String str) throws WordsExtractorException {
        super(str, NAME);
    }

    @Override
    public boolean in(String word) {
        return word.startsWith(str);
    }
}
