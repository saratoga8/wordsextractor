package free.wordsextractor.bl.translation.yandex;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.translation.Translation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class YandexTranslationTest {

    private YandexTranslation yandex;
    private static final String WORD = "add";
    private static final String EXPECTED_TRANSLATION = "add [æd] verb\n" +
            "\tдобавлять, прибавлять, увеличивать, добавить, прибавить, увеличить\n" +
            "\t(append, increase, gain) \n" +
            "\tскладывать, сложить\n" +
            "\t(put) \n" +
            "\tдополнить, дополнять\n" +
            "\t(complement) \n" +
            "\tпридавать, придать, присоединить\n" +
            "\t(give, attach) \n" +
            "\tдобавиться\n" +
            "\t(supplement) \n" +
            "\tподлить, подливать, долить\n" +
            "\t(pour) \n" +
            "\tпополнять\n" +
            "\t(fill) \n" +
            "\tдописать\n" +
            "\t(finish) \n" +
            "\tприсовокупить\n" +
            "\n" +
            "add [æd] adjective\n" +
            "\tдополнительный\n" +
            "\t(additional) \n" +
            "\n";

    @BeforeEach
    void setUp() {
        try {
            yandex = new YandexTranslation(FileManager.getResourcesFilePath("yandex_api.key", this), Translation.Langs.ENG, Translation.Langs.RUS);
        }
        catch (Exception e) {
            Assert.assertTrue("The test aborted by exception: " + e, false);
        }
    }

    @DisplayName("Build request")
    @Test
    void request() {
        String expectedRequest = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20180320T114111Z.e550b57aff38f6e8.f7574e722eede01b56f12e9edd3a7df35e337e41&lang=en-ru&text=" + WORD;
        Assert.assertEquals(expectedRequest,yandex.buildRequest(WORD));
    }

    @DisplayName("Translate a word")
    @Test
    void translate() {
        try {
            Assert.assertEquals(EXPECTED_TRANSLATION, yandex.translate(WORD));
        }
        catch (WordsExtractorException e) {
            Assert.assertTrue("The test aborted by the exception: " + e, false);
        }
    }

    @DisplayName("Translate words")
    @Test
    void translateWords() {
        List<String> words = new LinkedList<> (Arrays.asList("test", WORD));
        Assert.assertEquals(EXPECTED_TRANSLATION, yandex.translate(words).getTranslation(WORD));
    }

    @DisplayName("Translate words(with not translatable ones)")
    @Test
    void translateWordsNotTranslatable() {
        List<String> words = new LinkedList<>(Arrays.asList("test", WORD, "adfasdfasd"));
        Assert.assertEquals(2, yandex.translate(words).getSortedTranslations().size());
        Assert.assertEquals(1, yandex.translate(words).getNotTranslatedWords().size());
    }

    @DisplayName("Invalid Yandex Api Key")
    @Test
    public void invalidApiKey() {
        try {
            yandex = new YandexTranslation(FileManager.getResourcesFilePath("lingvo.token", this), Translation.Langs.ENG, Translation.Langs.RUS);
            yandex.translate("test");
        } catch (Exception e) {
            Assert.assertEquals("There is error response from Yandex: API key is invalid", e.getMessage());
        }
    }

    @DisplayName("Invalid translation langs")
    @Test
    public void invalidTransLangs() {
        try {
            yandex = new YandexTranslation(FileManager.getResourcesFilePath("yandex_api.key", this), Translation.Langs.ENG, Translation.Langs.TEST);
            yandex.translate("test");
        } catch (Exception e) {
            Assert.assertEquals("There is error response from Yandex: The specified language is not supported", e.getMessage());
        }
    }
}