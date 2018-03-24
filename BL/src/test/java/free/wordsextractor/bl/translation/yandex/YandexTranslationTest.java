package free.wordsextractor.bl.translation.yandex;

import free.wordsextractor.bl.translation.Translation;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class YandexTranslationTest {
    private static YandexTranslation yandex = new YandexTranslation(Translation.Langs.ENG, Translation.Langs.RUS);
    private static String WORD = "hot-button";

    @DisplayName("Build request")
    @Test
    void request() {
        String expectedRequext = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20180320T114111Z.e550b57aff38f6e8.f7574e722eede01b56f12e9edd3a7df35e337e41&lang=en-ru&text=hot-button";
        Assert.assertEquals(expectedRequext,yandex.buildRequest(WORD));
    }

    @DisplayName("Translate")
    @Test
    void translate() {
        Assert.assertEquals("" ,yandex.translate(WORD));
    }
}