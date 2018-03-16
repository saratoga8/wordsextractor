package free.wordsextractor.bl.translation;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.OnlyWordsDictionaryTest;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

class TranslationManagerTest {

    @DisplayName("Remove known words from extracted")
    @Test
    void removeKnownWords() {
        try {
            TranslationManager mngr = new TranslationManager(OnlyWordsDictionaryTest.getDictionaryFromFile(this, "list.txt"));
            Assert.assertEquals("[one, two, three, four, five, six, seven, eight]", mngr.getExtractedWordsDict().getWords().toString());

            mngr.removeKnownWords(Paths.get(this.getClass().getClassLoader().getResource(TranslationManager.KNOWN_WORDS_FILE_NAME).toURI()));
            Assert.assertEquals("[four, five, six, seven, eight]", mngr.getExtractedWordsDict().getWords().toString());
        } catch (WordsExtractorException | IOException | URISyntaxException e) {
            System.err.println("Test has aborted because of exception: " + e);
        }
    }
}