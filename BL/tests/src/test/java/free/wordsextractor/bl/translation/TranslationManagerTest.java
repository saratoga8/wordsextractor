package free.wordsextractor.bl.translation;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.OnlyWordsDictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;
import free.wordsextractor.common.tests.Utils;
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
            Dictionary dict = new OnlyWordsDictionary(Paths.get(Utils.getResourcePathStr(this, "list.txt")), Translation.Langs.ENG);
            TranslationManager mngr = new TranslationManager(dict);
            Assert.assertEquals("[six, four, one, seven, two, three, five, eight]", mngr.getExtractedWordsDict().getWords().toString());

            TranslationsDictionary knownWords = new TranslationsDictionary(Translation.Langs.ENG);
            knownWords.addTranslation("one", "number 1");
            knownWords.addTranslation("two", "number 2");
            knownWords.addTranslation("three", "number 3");
            knownWords.saveAsBinIn(".knowns");
            mngr.removeKnownWords(Paths.get(Utils.getResourcePathStr(this, "knowns.dict")), Translation.Langs.ENG);
            Assert.assertEquals("[six, four, seven, five, eight]", mngr.getExtractedWordsDict().getWords().toString());
        } catch (IOException | URISyntaxException | WordsExtractorException e) {
            Assert.assertTrue("Test has aborted because of exception: " + e, false);
        }
    }
}