package free.wordsextractor.bl.file_proc.extractors;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordExtractorException;

import java.nio.file.Path;

/**
 * Interface of a text extractor
 */
public interface TextExtractorInterface {

    String CHAR_SET = "UTF-8";

    /**
     * Extract text from file with a given path
     * @param path The file's path
     * @return Extracted text
     * @throws WordExtractorException
     */
    @NotNull
    String extractTxtFrom(Path path) throws WordExtractorException;
}
