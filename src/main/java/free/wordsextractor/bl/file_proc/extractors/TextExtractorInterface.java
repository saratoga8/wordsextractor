package free.wordsextractor.bl.file_proc.extractors;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Interface of a text extractor
 */
public interface TextExtractorInterface {
    List<TextExtractorInterface> EXTRACTORS = Arrays.asList(new TicaTextExtractor());


    String extractTxtFrom(Path path);
}
