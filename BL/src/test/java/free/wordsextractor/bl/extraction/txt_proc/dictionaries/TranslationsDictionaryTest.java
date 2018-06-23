package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import free.wordsextractor.bl.WordsExtractorException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * Created by saratoga on 31/03/18.
 */
class TranslationsDictionaryTest {
    private TranslationsDictionary dict;

    @BeforeEach
    void setUp() {
        dict = new TranslationsDictionary();
    }

    @DisplayName("Get sorted translations")
    @Test
    void getSortedTranslations() {
        dict.addTranslation("ghi", "translation3");
        dict.addTranslation("def", "translation2");
        dict.addTranslation("abc", "translation1");

        Assert.assertEquals("[translation1, translation2, translation3]", dict.getSortedTranslations().toString());
    }

    @DisplayName("Get translations")
    @Test
    void getTranslations() {
        dict.addTranslation("ghi", "translation3");
        dict.addTranslation("def", "translation2");
        dict.addTranslation("abc", "translation1");

        List<String> translations = (List<String>) dict.getTranslations();
        translations.sort(String::compareToIgnoreCase);

        Assert.assertEquals("[translation1, translation2, translation3]", translations.toString());
    }

    @DisplayName("Get words")
    @Test
    void getWords() {
        dict.addTranslation("ghi", "translation3");
        dict.addTranslation("def", "translation2");
        dict.addTranslation("mno", "");
        dict.addTranslation("abc", "translation1");
        dict.addTranslation("jkl", "");

        List<String> words = dict.getWords();
        words.sort(String::compareToIgnoreCase);

        List<String> unTranslated = dict.getNotTranslatedWords();
        unTranslated.sort(String::compareToIgnoreCase);

        Assert.assertEquals("Get all dictionary's words", "[abc, def, ghi, jkl, mno]", words.toString());
        Assert.assertEquals("Get untranslated words", "[jkl, mno]", unTranslated.toString());
    }

    @DisplayName("Get a word translation")
    @Test
    void getTranslation() {
        dict.addTranslation("ghi", "translation3");
        dict.addTranslation("def", "translation2");
        dict.addTranslation("abc", "translation1");

        Assert.assertEquals("translation3", dict.getTranslation("ghi"));
        Assert.assertEquals("translation2", dict.getTranslation("def"));
        Assert.assertEquals("translation1", dict.getTranslation("abc"));
    }

    @DisplayName("Dictionary contains words")
    @Test
    void containsWords() {
        dict.addTranslation("one", "translation of one");
        dict.addTranslation("three", "translation of three");
        dict.addTranslation("two", "translation of two");
        dict.addTranslation("four", "translation of four");

        Assert.assertTrue(dict.contains("one"));
        Assert.assertTrue(dict.contains("two"));
        Assert.assertTrue(dict.contains("three"));
        Assert.assertTrue(dict.contains("four"));

        Assert.assertFalse(dict.contains("adsfadsfa"));
    }

    @DisplayName("Remove words")
    @Test
    void removeWords() {
        dict.addTranslation("one", "translation of one");
        dict.addTranslation("three", "translation of three");
        dict.addTranslation("two", "translation of two");
        dict.addTranslation("four", "translation of four");

        Assert.assertTrue(dict.contains("one"));
        dict.removeWord("one");
        Assert.assertFalse(dict.contains("one"));
        Assert.assertTrue(dict.contains("two"));
        dict.removeWord("two");
        Assert.assertFalse(dict.contains("two"));
        Assert.assertTrue(dict.contains("three"));
        dict.removeWord("three");
        Assert.assertFalse(dict.contains("three"));
        Assert.assertTrue(dict.contains("four"));
        dict.removeWord("four");
        Assert.assertFalse(dict.contains("four"));
        Assert.assertTrue(dict.getWords().isEmpty());

        dict.removeWord("four");
        Assert.assertTrue(dict.getTranslations().isEmpty());
        Assert.assertTrue(dict.getNotTranslatedWords().isEmpty());
    }

    @DisplayName("Add a word without translation to dictionary")
    @Test
    void addWord() {
        try {
            dict.addWord("test");
            Assert.assertTrue("Should be thrown exception", false);
        }
        catch (WordsExtractorException e) {
        }
    }

    @DisplayName("Save dictionary")
    @Test
    void saveDict() {
        dict.addTranslation("ghi", "translation3");
        dict.addTranslation("def", "translation2");
        dict.addTranslation("abc", "translation1");

        Assert.assertEquals("[translation1, translation2, translation3]", dict.getSortedTranslations().toString());

        try {
            File file = File.createTempFile("dict", "bin");
            file.deleteOnExit();
            dict.saveAsBinIn(file.getAbsolutePath());
            TranslationsDictionary readDict = Dictionary.readAsBinFrom(file.getAbsolutePath());

            Assert.assertEquals("[translation1, translation2, translation3]", readDict.getSortedTranslations().toString());
        } catch (Exception e) {
            Assert.assertTrue("Test aborted by the exception: " + e, false);
        }
    }
}