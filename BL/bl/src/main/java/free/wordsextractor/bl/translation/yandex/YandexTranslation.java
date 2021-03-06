package free.wordsextractor.bl.translation.yandex;

import com.google.gson.Gson;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.translation.WebTranslation;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

/**
 * Translation by Yandex web service
 */
public class YandexTranslation extends WebTranslation {
    private static final Logger log = LogManager.getLogger(YandexTranslation.class);     /* log item */

    private final static String SERVICE_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";   /* URL of the Yandex dictionary web service */
    private final String apiKey;                                                                                /* Path of a file containing API key */

    /**
     * Constructor
     * @param apiPath Path of a file containing API key
     * @param fromLang Translate from language
     * @param toLang Translate to language
     */
    public YandexTranslation(final Path apiPath, final Langs fromLang, final Langs toLang) throws WordsExtractorException {
        super(SERVICE_URL, fromLang, toLang);
        this.apiKey = getApiKey(apiPath);
        if (StringUtils.isBlank(apiKey))
            throw new WordsExtractorException("Can't get API key of Yandex from file " + apiPath.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String buildRequest(String word) {
        if (!StringUtils.isBlank(word)) {
            if (!StringUtils.isBlank(apiKey))
                return super.serviceURL + "?key=" + apiKey + "&lang=" + fromLang.getVal() + "-" + toLang.getVal() + "&text=" + word;
        }
        else
            log.error("The given word is EMPTY or NULL");
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class getTranslationBean() {
        return YandexTranslationBean.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chkResponseNotError(String responseTxt) throws WordsExtractorException {
        if (!StringUtils.isBlank(responseTxt)) {
            if (responseTxt.contains("code") && responseTxt.contains("message") ) {
                YandexErrorBean err = new Gson().fromJson(responseTxt, YandexErrorBean.class);
                throw new WordsExtractorException("There is error response from Yandex: " + err.getMsg());
            }
        }
        else
            log.error("The given response text is EMPTY or NULL");
    }
}
