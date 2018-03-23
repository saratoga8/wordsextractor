package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public abstract class WebTranslation extends Translation {
    private static final Logger log = LogManager.getLogger(WebTranslation.class);        /* logger */
    final protected String serviceURL;

    @NotNull
    public WebTranslation(String serviceURL, Langs fromLang, Langs toLang) {
        super(fromLang, toLang);
        this.serviceURL = serviceURL;
    }

    @NotNull
    protected abstract String buildRequest(String word);

    @NotNull
    protected String getApiKey(final URI uri) {
        log.debug("Read API key from " + uri.getPath());

        Path path = Paths.get(uri);
        if (path.toFile().exists()) {
            try (final Scanner scanner = new Scanner(path, TextExtractorInterface.CHAR_SET)) {
                return scanner.next();
            } catch (IOException e) {
                log.error("Can't read an API key from the file " + path.toAbsolutePath() + ": " + e);
            }
        }
        else
            log.error("Can't read an API key from the file " + path.toAbsolutePath() + ", because it doesn't exist");
        return "";
    }
}
