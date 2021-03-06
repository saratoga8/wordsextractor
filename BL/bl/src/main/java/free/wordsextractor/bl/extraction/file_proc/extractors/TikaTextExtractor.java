package free.wordsextractor.bl.extraction.file_proc.extractors;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Text extractor using Tika
 */
public class TikaTextExtractor implements TextExtractorInterface {
    private final Logger log = LogManager.getLogger(TikaTextExtractor.class);     /* log item */

    private final BodyContentHandler handler;                                     /* Tika's body content handler */
    private final TikaConfig tikaConf;                                            /* Tika's configuration */

    private final String TIKA_CONF_FILE = "tika-conf.xml";                        /* Tika's configuration file name */

    /**
     * Constructor
     */
    public TikaTextExtractor() throws WordsExtractorException {
        handler = new BodyContentHandler(-1);
        try {
            tikaConf = new TikaConfig(getClass().getClassLoader().getResource(TIKA_CONF_FILE));
        } catch (IOException | SAXException | TikaException e) {
            throw new WordsExtractorException("Can't initialize Tika configuration file " + TIKA_CONF_FILE + ": " + e);
        }
    }

    /**
     * Extract text from a file
     * @param path The path of a file
     * @return extracted text
     */
    @NotNull
    public String extractTxtFrom(final Path path) throws WordsExtractorException {
        if(path != null) {
            try (final InputStream stream = new FileInputStream(new File(path.toUri()))) {
                new AutoDetectParser(tikaConf).parse(stream, handler, new Metadata());
                return handler.toString();
            } catch (IOException | TikaException | SAXException e) {
                throw new WordsExtractorException("Can't extract text from " + path + ": " + e);
            }
        }
        throw new WordsExtractorException("The given path is NULL");
    }
}
