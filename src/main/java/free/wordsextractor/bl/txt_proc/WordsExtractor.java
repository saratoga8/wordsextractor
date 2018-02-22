package free.wordsextractor.bl.txt_proc;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by saratoga on 10/02/18.
 */
public class WordsExtractor {
    final private List paths;

    public WordsExtractor(final List<Path> paths) {
        this.paths = paths;
    }


    public Dictionary createDictionary() {
        final Dictionary dict = new Dictionary();
        addWordsToDict(dict);
        return dict;
    }

    private void addWordsToDict(Dictionary dict) {

    }
}
