package free.wordsextractor.bl.file_proc.extractors;

import free.wordsextractor.bl.WordExtractorException;

import java.nio.file.Path;

/**
 * Manage text extractions from files
 */
public class ExtractionManager {
    /**
     * Extract text from the file with the given path
     * @param path The path of the file
     * @return Extracted text from the given file
     * @throws WordExtractorException
     */
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
