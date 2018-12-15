package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.translation.Translation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TranslationsDictionaryStressTest {
    private TranslationsDictionary dict;

    @BeforeEach
    void setUp() {
        try {
            dict = new TranslationsDictionary(Translation.Langs.ENG);
        } catch (WordsExtractorException e) {
            Assert.assertTrue("The test aborted by the exception: " + e, false);
        }

        dict.addTranslation("one", "translation1");
        dict.addTranslation("two", "translation2");
    }

    @DisplayName("Adding NULL or EMPTY words or translations")
    @Test
    void addTranslation() {
        dict.addTranslation("", "empty translation");
        List<String> words = dict.getWords();
        words.sort(String::compareToIgnoreCase);

        Assert.assertEquals("[one, two]", words.toString());

        dict.addTranslation(null, "null translation");
        words = dict.getWords();
        words.sort(String::compareToIgnoreCase);
        Assert.assertEquals("[one, two]", words.toString());

        dict.addTranslation("adsfasd", null);
        words = dict.getWords();
        words.sort(String::compareToIgnoreCase);
        Assert.assertEquals("[one, two]", words.toString());
    }

    @DisplayName("Get translation of NULL or EMPTY")
    @Test
    void getTranslation() {
        Assert.assertEquals("",dict.getTranslation("asdfasdf"));
        Assert.assertEquals("",dict.getTranslation(""));
        Assert.assertEquals("",dict.getTranslation(null));
    }

    @DisplayName("Contains EMPTY or NULL words")
    @Test
    public void contains() {
        Assert.assertFalse(dict.contains(""));
        Assert.assertFalse(dict.contains(null));
    }

    @DisplayName("Remove EMPTY or NULL words")
    @Test
    public void removes() {
        Assert.assertFalse(dict.removeWord(""));
        Assert.assertFalse(dict.removeWord(null));
        Assert.assertFalse(dict.removeWord("asdfasdfas"));

        List<String> words = dict.getWords();
        words.sort(String::compareToIgnoreCase);
        Assert.assertEquals("[one, two]", words.toString());
    }
}
