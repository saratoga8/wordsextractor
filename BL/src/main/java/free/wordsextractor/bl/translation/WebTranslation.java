package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;
import com.google.gson.Gson;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import free.wordsextractor.bl.net.HttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
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

    @NotNull
    protected abstract HashMap<Integer, String> getResponseCodes();

    @NotNull
    public String translate(String word) {
        log.debug("Translating the word '" + word + "' by " + getTranslationBean().getName() + "'s dictionary");

        String responseTxt = HttpClient.getResponseFrom(buildRequest(word), getResponseCodes());
        return new Gson().fromJson(responseTxt, getTranslationBean()).toString();
    }

    @NotNull
    public abstract Class getTranslationBean();
}
