package free.wordsextractor.bl.txt_proc;

import free.wordsextractor.bl.WordExtractorException;

import java.nio.file.Path;
import java.util.List;

/**
 * Words extractor
 */
public class WordsExtractor {
    final private List paths;

    public WordsExtractor(final List<Path> paths) throws WordExtractorException {
        if ((paths != null) && !paths.isEmpty())
            this.paths = paths;
        else
            throw new WordExtractorException("The list of files paths is NULL or EMPTY");
    }


    public Dictionary createDictionary() {
        final Dictionary dict = new Dictionary();
        addWordsToDict(dict);
        return dict;
    }

    private void addWordsToDict(Dictionary dict) {

    }
}
