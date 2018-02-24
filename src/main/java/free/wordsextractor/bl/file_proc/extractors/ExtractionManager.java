package free.wordsextractor.bl.file_proc.extractors;

import free.wordsextractor.bl.WordExtractorException;

import java.nio.file.Path;

/**
 * Created by saratoga on 10/02/18.
 */
public class ExtractionManager {

    public static String extractTxtFrom(final Path path) throws WordExtractorException {
        String txt = "";
        for (final TextExtractorInterface txtExtractor: TextExtractorInterface.EXTRACTORS) {
            txt = txtExtractor.extractTxtFrom(path);
            if (!txt.isEmpty())
                return txt;
        }
        throw new WordExtractorException("There is no text extractor for the given file " + path);
    }
}
