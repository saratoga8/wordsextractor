package free.wordsextractor.bl.file_proc.extractors;

import java.nio.file.Path;

/**
 * Interface of a text extractor
 */
public interface TextExtractorInterface {

    String CHAR_SET = "UTF-8";


    String extractTxtFrom(Path path);
}
