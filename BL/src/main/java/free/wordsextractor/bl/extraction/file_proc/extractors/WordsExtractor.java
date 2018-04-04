package free.wordsextractor.bl.extraction.file_proc.extractors;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.WordsStatisticsDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Words extractor
 */
public class WordsExtractor {
    private static final Logger log = LogManager.getLogger(WordsExtractor.class);               /* logger */

    @NotNull
    final private List<Path> paths;                                                             /* paths of files containing text */

    /**
     * Constructor
     * Initialize paths of text files containing text. From the files should be extracted words
     * @param paths Paths of text files
     * @throws WordsExtractorException
     */
    @NotNull
    public WordsExtractor(final List<Path> paths) throws WordsExtractorException {
        if ((paths != null) && !paths.isEmpty())
            this.paths = paths;
        else
            throw new WordsExtractorException("The list of files paths is NULL or EMPTY");
    }

    /**
     * Create dictionary from the extracted words
     * @return Created dictionary
     */
    @NotNull
    public Dictionary createWordsStatsDictionary() throws WordsExtractorException {
        return addWordsToDict(new WordsStatisticsDictionary());
    }

    /**
     * Extract words from a file with the given path
     * @param path The file's path
     * @return The list of extracted words
     * @throws WordsExtractorException
     * @throws IOException
     */
    @NotNull
    public static List<String> extractWordsFromFile(final Path path) throws IOException {
        final LinkedList<String> words = new LinkedList<>();
        if(path != null) {
            if (!path.toString().isEmpty()) {
                String word;
                try (final Scanner scanner = new Scanner(path, TextExtractorInterface.CHAR_SET)) {
                    while (scanner.hasNext()) {
                        word = scanner.next();
                        if(!words.add(word))
                            log.error("Can't add the word '" + word + "'");
                    }
                    if (words.isEmpty())
                        log.error("In the file " + path.toString() + " no words has found");
                }
            }
            else
                log.error("The given path is EMPTY");
        }
        else
            log.error("The given path is NULL");
        return words;
    }

    /**
     * Add words from text files to the given dictionary
     * @param dict The dictionary
     * @return Updated dictionary
     */
    @NotNull
    private WordsStatisticsDictionary addWordsToDict(final WordsStatisticsDictionary dict) throws WordsExtractorException {
        if(dict != null) {
            paths.parallelStream().forEach(path -> {
                try {
                    extractWordsFromFile(path).forEach(dict::addWord);
                } catch (IOException e) {
                    log.error("Can't add words from file " + path.toString() + " to a dictionary: " + e);
                }
            });
            return dict;
        }
        throw new WordsExtractorException("The given dictionary is NULL");
    }
}
