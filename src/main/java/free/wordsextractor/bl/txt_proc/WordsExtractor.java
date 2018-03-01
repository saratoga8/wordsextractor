package free.wordsextractor.bl.txt_proc;

import free.wordsextractor.bl.WordExtractorException;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Words extractor
 */
public class WordsExtractor {
    final private List<Path> paths;

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

    private List<String> extractWordsFromFile(final Path path) {
        List<String> words = new LinkedList();
        return words;
    }

    private void addWordsToDict(final Dictionary dict) {
        paths.parallelStream().forEach( path -> extractWordsFromFile(path)
                                                                          .stream().forEach(word -> dict.addWord(word)) );
    }
}
