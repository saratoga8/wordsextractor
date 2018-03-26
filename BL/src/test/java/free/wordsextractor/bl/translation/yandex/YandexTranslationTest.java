package free.wordsextractor.bl.translation.yandex;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.translation.Translation;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

class YandexTranslationTest {
    private static YandexTranslation yandex = new YandexTranslation(Translation.Langs.ENG, Translation.Langs.RUS);
    private static String WORD = "hot-button";
    private static String EXPECTED_TRANSLATION = "hot-button  adjective\n\tзлободневный\n\nhot button [hɔt bʌtn] noun\n\tгорячая кнопка, горячая клавиша \n\t(hot key, hotkey) \n\n";


    @DisplayName("Build request")
    @Test
    void request() {
        String expectedRequext = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20180320T114111Z.e550b57aff38f6e8.f7574e722eede01b56f12e9edd3a7df35e337e41&lang=en-ru&text=hot-button";
        Assert.assertEquals(expectedRequext,yandex.buildRequest(WORD));
    }

    @DisplayName("Translate a word")
    @Test
    void translate() {
        Assert.assertEquals(EXPECTED_TRANSLATION ,yandex.translate(WORD));
    }

    @DisplayName("Translate words")
    @Test
    void translateWords() {
        List<String> words = new LinkedList<> (Arrays.asList("test", WORD));
        try {
            Hashtable<String, String> translations = yandex.translate(words);
            Assert.assertEquals(EXPECTED_TRANSLATION, translations.get(WORD));
        } catch (WordsExtractorException e) {
            System.err.println("The test aborted because of exception: " + e);
        }
    }
}