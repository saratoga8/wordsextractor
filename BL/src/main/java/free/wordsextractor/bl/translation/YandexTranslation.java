package free.wordsextractor.bl.translation;

import free.wordsextractor.bl.net.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
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
    public String translate(String word) {
        String responseTxt = HttpClient.getResponseFrom(buildRequest(word), respCodes);
        return responseTxt;
    }

    @Override
    protected String buildRequest(String word) {
        if (!StringUtils.isBlank(word)) {
            try {
                final URI uri = this.getClass().getClassLoader().getResource(API_KEY_FILE_NAME).toURI();
                return super.serviceURL + "?key=" + getApiKey(uri)
                                        + "&lang=" + fromLang.getVal() + "-" + toLang.getVal()
                                        + "&text=" + word;
            } catch (URISyntaxException e) {
                log.error("Can't build request string: " + e);
            }
        }
        else
            log.error("The given word is EMPTY or NULL");
        return "";
    }
}
