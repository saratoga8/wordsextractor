package free.wordsextractor.bl.translation;

import com.google.gson.Gson;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import free.wordsextractor.bl.net.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Translations by web services
 */
public abstract class WebTranslation extends Translation {
    private static final Logger log = LogManager.getLogger(WebTranslation.class);        /* logger */
    final protected String serviceURL;                                                   /* URL of a translations service */

    /**
     * Constructor
     * @param serviceURL The URL of the translations service
     * @param fromLang Translate from language
     * @param toLang Translate to language
     */
    protected WebTranslation(String serviceURL, Langs fromLang, Langs toLang) {
        super(fromLang, toLang);
        this.serviceURL = serviceURL;
    }

    /**
     * Build request to the translations service
     * @param word The word should be translated by the request
     * @return The built request string
     */
    protected abstract String buildRequest(String word);

    /**
     * Get an API key from a file
     * @param path The path to the file containing the key
     * @return The key string
     */
    protected String getApiKey(final Path path) {
        log.debug("Read API key from " + path);

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

    /**
     * Translate the given word
     * @param word The word
     * @return Translation string
     * @throws WordsExtractorException There are errors isIn the response
     */
    public String translate(String word) throws WordsExtractorException {
        String responseTxt = HttpClient.getResponseFrom(buildRequest(word));
        if(StringUtils.isBlank(responseTxt)) {
            log.error("Can't get translation from " + serviceURL);
            return "";
        }
        else {
            chkResponseNotError(responseTxt);
            return new Gson().fromJson(responseTxt, getTranslationBean()).toString();
        }
    }

    /**
     * Get Translation bean class instance for converting JSON response
     * @return Bean's class instance
     */
    public abstract Class getTranslationBean();

    /**
     * Check the given response string isn't error
     * @param responseTxt The text of response
     * @throws WordsExtractorException It is error and the exception contains the error response message
     */
    public abstract void chkResponseNotError(String responseTxt) throws WordsExtractorException;
}
