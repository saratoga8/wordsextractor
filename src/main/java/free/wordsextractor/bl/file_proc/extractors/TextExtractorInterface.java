package free.wordsextractor.bl.file_proc.extractors;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordExtractorException;

import java.nio.file.Path;

/**
 * Interface of a text extractor
 */
public interface TextExtractorInterface {

    String CHAR_SET = "UTF-8";

    @NotNull
    String extractTxtFrom(Path path) throws WordExtractorException;
}
