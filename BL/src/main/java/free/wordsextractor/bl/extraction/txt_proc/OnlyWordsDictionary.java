package free.wordsextractor.bl.extraction.txt_proc;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Dictionary containing only words
 */
public class OnlyWordsDictionary implements Dictionary {
    final private static Logger log = LogManager.getLogger(OnlyWordsDictionary.class);        /* logger */
    final private List<String> words;                                                         /* words of the dictionary */  //TODO should be HashSet

    /**
     * Constructor of the dictionary from the given file
     * @param path The path of the file containing words for dictionary
     * @throws IOException Can't open or find the file
     * @throws WordsExtractorException Can't read the words from the file
     */
    public OnlyWordsDictionary(final Path path) throws IOException, WordsExtractorException {
        words = WordsExtractor.extractWordsFromFile(path);
    }

    /**
     * Add a new word to the dictionary
     * @param word The new word
     */
    @Override
    public void addWord(String word) {
        if (StringUtils.isBlank(word)) {
            if (words.add(word))
                words.sort(String::compareToIgnoreCase);
        }
        else
            log.error("The given word is NULL or EMPTY and can't be added to the dictionary");
    }

    /**
     * Check the dictionary contains the given word
     * @param word The checked word
     * @return true - The word is in the dictionary
     */
    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }

    /**
     * Remove the given word from the dictionary
     * @param word The word for removing
     * @return true - The word removed successfully
     */
    @Override
    public boolean removeWord(String word) {
        if (StringUtils.isBlank(word)) {
            log.error("The given word for removing is NULL or EMPTY");
            return false;
        }
        else
            return words.remove(word);
    }

    /**
     * Get all the words of the dictionary
     * @return The list of dictionary's words
     */
    @Override
    public List<String> getWords() {
        return words;
    }
}
