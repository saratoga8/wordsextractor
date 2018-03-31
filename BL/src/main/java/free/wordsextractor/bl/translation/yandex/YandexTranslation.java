package free.wordsextractor.bl.translation.yandex;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.translation.WebTranslation;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;

public class YandexTranslation extends WebTranslation {
    private static final Logger log = LogManager.getLogger(YandexTranslation.class);     /* log item */

    private final static String SERVICE_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";
    private final static String API_KEY_FILE_NAME = "yandex_api.key";

    private final HashMap<Integer, String> respCodes = new HashMap<>();

    public YandexTranslation(Langs fromLang, Langs toLang) {
        super(SERVICE_URL, fromLang, toLang);

        respCodes.put(401, "Invalid API key");
        respCodes.put(402, "This API key has been blocked");
        respCodes.put(403, "Exceeded the daily limit on the number of requests");
        respCodes.put(413, "The text size exceeds the maximum");
        respCodes.put(501, "The specified translation direction is not supported");
    }

    @Override
    protected String buildRequest(String word) {
        if (!StringUtils.isBlank(word)) {
            try {
                final Path apiFilePath = FileManager.getResourcesFilePath(API_KEY_FILE_NAME, this);
                return super.serviceURL + "?key=" + getApiKey(apiFilePath)
                        + "&lang=" + fromLang.getVal() + "-" + toLang.getVal()
                        + "&text=" + word;
            } catch (WordsExtractorException | URISyntaxException e) {
                log.error("Can't read API key from file " + API_KEY_FILE_NAME + ": " + e);
            }
        }
        else
            log.error("The given word is EMPTY or NULL");
        return "";
    }

    @Override
    protected HashMap<Integer, String> getResponseCodes() {
        return respCodes;
    }

    @Override
    public Class getTranslationBean() {
        return YandexTranslationBean.class;
    }


}
