package free.wordsextractor.bl.extraction.txt_proc;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import free.wordsextractor.bl.extraction.file_proc.extractors.TikaTextExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Manage text extractions from files
 */
public class ExtractionManager {
    private final Logger log = LogManager.getLogger(ExtractionManager.class);     /* log item */
    final private List<TextExtractorInterface> extractors;                        /* text extractors */

    /**
     * Constructor
     * @throws WordsExtractorException
     */
    public ExtractionManager() throws WordsExtractorException {
        extractors = Collections.singletonList(new TikaTextExtractor());
        if(extractors.isEmpty())
            throw new WordsExtractorException("There are no text extractors in use");
    }

    /**
     * Extract text from the file with the given path
     * @param path The path of the file
     * @return Extracted text from the given file
     * @throws WordsExtractorException
     */
    public String extractTxtFrom(final Path path) throws WordsExtractorException {
        for (final TextExtractorInterface txtExtractor: extractors) {
            String txt = txtExtractor.extractTxtFrom(path);
            if (!txt.isEmpty())
                return txt;
        }
        throw new WordsExtractorException("There is no text extractor for the given file " + path);
    }
}
