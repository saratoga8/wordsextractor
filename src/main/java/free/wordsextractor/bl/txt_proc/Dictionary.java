package free.wordsextractor.bl.txt_proc;

import com.drew.lang.annotations.NotNull;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Dictionary of word statistics
 */
public class Dictionary {
    private static final Logger log = LogManager.getLogger(Dictionary.class);

    private final Hashtable<String, Integer> wordsStat;

    public Dictionary() {
        this.wordsStat = new Hashtable<String, Integer>();
    }

    public List<String> getWords() {
        return new ArrayList<>(wordsStat.keySet());
    }

    @NotNull
    public void addWord(String word) {
        if (StringUtils.isBlank(word)) {
            log.error("Can't add NULL or EMPTY word to dictionary");
            return;
        }
        final Integer num = wordsStat.containsKey(word) ? wordsStat.get(word) + 1: 1;
        wordsStat.put(word, num);
    }

    public void save(String path) {
    }
}
