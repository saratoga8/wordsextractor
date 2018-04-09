package free.wordsextractor.bl.translation.yandex;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.translation.Translation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

class YandexTranslationStressTest {
    private YandexTranslation yandex;

    @BeforeEach
    void setUp() {
        try {
            yandex = new YandexTranslation(FileManager.getResourcesFilePath("yandex_api.key", this), Translation.Langs.ENG, Translation.Langs.RUS);
        }
        catch (Exception e) {
            Assert.assertTrue("The test aborted by exception: " + e, false);
        }
    }

    @DisplayName("Build request for translating an EMPTY or NULL word")
    @Test
    void buildRequest() {
        Assert.assertEquals("", yandex.buildRequest(""));
        Assert.assertEquals("", yandex.buildRequest(null));
    }

    @DisplayName("Translate EMPTY or NULL word")
    @Test
    void translateWord() {
        try {
            Assert.assertEquals("", yandex.translate(""));
            Assert.assertEquals("", yandex.translate((String) null));
        }
        catch (WordsExtractorException e) {
            Assert.assertTrue("The test aborted by the exception: " + e, false);
        }
    }

    @DisplayName("Translate empty list of words")
    @Test
    void translateWords() {
        try {
            Assert.assertEquals("", yandex.translate(new LinkedList<>().toString()));
        } catch (WordsExtractorException e) {
            Assert.assertTrue("The test aborted by the exception: " + e, false);
        }
    }
}