package free.wordsextractor.bl.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoogleTranslation extends free.wordsextractor.bl.translation.Translation {
    final private static Logger log = LogManager.getLogger(GoogleTranslation.class);        /* logger */
    final private Translate translate = TranslateOptions.getDefaultInstance().getService();

    public GoogleTranslation(final Langs fromLang, final Langs toLang) {
        super(fromLang, toLang);
    }

    @Override
    public String translate(String word) {
        log.debug("Translating word " + word + " from " + fromLang.getVal() + " to " + toLang.getVal());
        Translation translation = translate.translate(word,  TranslateOption.sourceLanguage(fromLang.getVal()), TranslateOption.targetLanguage(toLang.getVal()));
        return translation.getTranslatedText();
    }
}
