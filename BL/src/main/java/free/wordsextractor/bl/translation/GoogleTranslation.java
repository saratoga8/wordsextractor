package free.wordsextractor.bl.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class GoogleTranslation extends free.wordsextractor.bl.translation.Translation {
    final private Translate translate = TranslateOptions.getDefaultInstance().getService();

    private GoogleTranslation(Langs fromLang, Langs toLang) {
        super(fromLang, toLang);
    }


    @Override
    public String translate(String word) {
        Translation translation = translate.translate(word,  TranslateOption.sourceLanguage(fromLang.name().toLowerCase()), TranslateOption.targetLanguage(toLang.name().toLowerCase()));
        return translation.getTranslatedText();
    }
}
