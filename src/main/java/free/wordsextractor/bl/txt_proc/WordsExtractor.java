package free.wordsextractor.bl.txt_proc;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.file_proc.extractors.TextExtractorInterface;
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
    private static final Logger log = LogManager.getLogger(WordsExtractor.class);               /** logger */

    @NotNull
    final private List<Path> paths;                                                             /** paths of files containing text */

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
    public Dictionary createDictionary() {
        return addWordsToDict(new Dictionary());
    }

    /**
     * Extract words from a file with the given path
     * @param path The file's path
     * @return The list of extracted words
     * @throws WordsExtractorException
     * @throws IOException
     */
    @NotNull
    private List<String> extractWordsFromFile(final Path path) throws WordsExtractorException, IOException {
        log.debug("Extract words from the file " + path.toString());

        final LinkedList words = new LinkedList();
        if (!path.toString().isEmpty()) {
            try (final Scanner scanner = new Scanner(path, TextExtractorInterface.CHAR_SET)) {
                while (scanner.hasNext())
                    words.add(scanner.next());

                if (words.isEmpty())
                    throw new WordsExtractorException("In the file " + path.toString() + " no words has found");
            }
        }
        else
            throw new WordsExtractorException("The given path is EMPTY");
        return words;
    }

    /**
     * Add words from text files to the given dictionary
     * @param dict The dictionary
     * @return Updated dictionary
     */
    @NotNull
    private Dictionary addWordsToDict(final Dictionary dict) {
        paths.parallelStream().forEach(path -> {
            try {
                extractWordsFromFile(path).stream().forEach(dict::addWord);
            } catch (WordsExtractorException | IOException e) {
                log.error("Can't add words from file " + path.toString() + " to a dictionary: " + e);
            }
        });
        return dict;
    }
}
