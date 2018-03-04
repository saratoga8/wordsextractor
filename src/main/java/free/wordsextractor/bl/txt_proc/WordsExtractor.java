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
    private static final Logger log = LogManager.getLogger(WordsExtractor.class);

    final private List<Path> paths;

    @NotNull
    public WordsExtractor(final List<Path> paths) throws WordsExtractorException {
        if ((paths != null) && !paths.isEmpty())
            this.paths = paths;
        else
            throw new WordsExtractorException("The list of files paths is NULL or EMPTY");
    }

    @NotNull
    public Dictionary createDictionary() {
        return addWordsToDict(new Dictionary());
    }

    @NotNull
    private List<String> extractWordsFromFile(final Path path) throws WordsExtractorException, IOException {
        log.debug("Extract words from the file " + path.toString());

        LinkedList<String> words = new LinkedList();
        if (!path.toString().isEmpty()) {
            try (Scanner scanner = new Scanner(path, TextExtractorInterface.CHAR_SET)) {
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

    @NotNull
    private Dictionary addWordsToDict(final Dictionary dict) {
        paths.parallelStream().forEach(path -> {
            try {
                extractWordsFromFile(path).stream().forEach(word -> dict.addWord(word));
            } catch (WordsExtractorException | IOException e) {
                log.error("Can't add words from file " + path.toString() + " to a dictionary: " + e);
            }
        });
        return dict;
    }
}
