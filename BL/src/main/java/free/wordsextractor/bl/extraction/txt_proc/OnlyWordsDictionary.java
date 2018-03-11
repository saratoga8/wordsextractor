package free.wordsextractor.bl.extraction.txt_proc;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class OnlyWordsDictionary implements Dictionary {
    final private static Logger log = LogManager.getLogger(OnlyWordsDictionary.class);        /** logger */
    final private List<String> words = new LinkedList<>();

    public OnlyWordsDictionary(Path path) {

    }

    @Override
    public void addWord(String word) {
        if (StringUtils.isBlank(word)) {
            if (words.add(word))
                words.sort(String::compareToIgnoreCase);
        }
        else
            log.error("The given word is NULL or EMPTY and can't be added to the dictionary");
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }
}
