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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Text extractor using Tika
 */
public class TikaTextExtractor implements TextExtractorInterface {
    private final Logger log = LogManager.getLogger(TikaTextExtractor.class);     /* log item */

    private final BodyContentHandler handler;                                     /* Tika's body content handler */
    private final TikaConfig tikaConf;                                            /* Tika's configuration */

    /**
     * Constructor
     */
    public TikaTextExtractor() throws WordsExtractorException {
        handler = new BodyContentHandler();

        Path tikaConfPath;
        String TIKA_CONF_FILE = "tika-conf.xml";
        try {
            final URL url = this.getClass().getClassLoader().getResource(TIKA_CONF_FILE);
            if (url != null) {
                tikaConfPath = Paths.get(url.toURI());
                if (tikaConfPath == null)
                    throw new WordsExtractorException("Can't get path of Tika configuration file " + TIKA_CONF_FILE);
            }
            else
                throw new WordsExtractorException("Can't find Tika's configuration file " + TIKA_CONF_FILE);
        } catch (URISyntaxException e) {
            throw new WordsExtractorException("Can't find Tika configuration file " + TIKA_CONF_FILE + ": " + e);
        }
        try {
            tikaConf = new TikaConfig(tikaConfPath);
        } catch (IOException | SAXException | TikaException e) {
            throw new WordsExtractorException("Can't initialize Tika configuration file " + tikaConfPath + ": " + e);
        }
    }

    /**
     * Extract text from a file
     * @param path The path of a file
     * @return extracted text
     */
    @NotNull
    public String extractTxtFrom(final Path path) throws WordsExtractorException {
        try (final InputStream stream = new FileInputStream(new File(path.toUri()))) {
            new AutoDetectParser(tikaConf).parse(stream, handler, new Metadata());
            return handler.toString();
        }
        catch (IOException | TikaException | SAXException e) {
            throw new WordsExtractorException("Can't extract text from " + path + ": " + e);
        }
    }
}
