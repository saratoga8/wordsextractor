package free.wordsextractor.bl.file_proc.extractors;

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
 * Created by saratoga on 10/02/18.
 */
public class TicaTextExtractor implements TextExtractorInterface {


    public TicaTextExtractor() {
    }

    public String extractTxtFrom(final Path path) {

        try (InputStream stream = new FileInputStream(new File(path.toUri()))) {
            final BodyContentHandler handler = new BodyContentHandler();
            new AutoDetectParser().parse(stream, handler, new Metadata());
            return handler.toString();
        }
        catch (IOException | TikaException | SAXException e) {
            System.err.println("Can't extract text from " + path + ": " + e);
        }
        return "";
    }
}