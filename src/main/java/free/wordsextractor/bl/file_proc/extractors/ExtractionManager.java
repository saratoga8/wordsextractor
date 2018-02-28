package free.wordsextractor.bl.file_proc.extractors;

import free.wordsextractor.bl.WordExtractorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Manage text extractions from files
 */
public class ExtractionManager {
    private final Logger log = LogManager.getLogger(getClass());
    final private List<TextExtractorInterface> extractors;


    public ExtractionManager() throws WordExtractorException {
        extractors = new LinkedList<>();

        try {
            extractors.add(new TikaTextExtractor());
        } catch (WordExtractorException e) {
            log.error("Can't add Tika Text Extractor to the list of extractors");
        }
        if(extractors.isEmpty())
            throw new WordExtractorException("There are no text extractors in use");
    }

    /**
     * Extract text from the file with the given path
     * @param path The path of the file
     * @return Extracted text from the given file
     * @throws WordExtractorException
     */
    public String extractTxtFrom(final Path path) throws WordExtractorException {
        for (final TextExtractorInterface txtExtractor: extractors) {
            String txt = txtExtractor.extractTxtFrom(path);
            if (!txt.isEmpty())
                return txt;
        }
        throw new WordExtractorException("There is no text extractor for the given file " + path);
    }
}
