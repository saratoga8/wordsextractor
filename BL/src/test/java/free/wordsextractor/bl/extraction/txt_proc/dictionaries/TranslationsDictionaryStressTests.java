package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Extreme tests of translations dictionary")
public class TranslationsDictionaryStressTests {
    private TranslationsDictionary dict;

    @BeforeEach
    void setUp() {
        dict = new TranslationsDictionary();

        dict.addTranslation("one", "translation1");
        dict.addTranslation("two", "translation2");
    }

    @DisplayName("Adding NULL or EMPTY words or translations")
    @Test
    void emptyWords() {
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

        Assert.assertEquals("",dict.getTranslation("asdfasdf"));
        Assert.assertEquals("",dict.getTranslation(""));
        Assert.assertEquals("",dict.getTranslation(null));
    }

    @DisplayName("Get translation of NULL or EMPTY")
    @Test
    void getTranslation() {
        Assert.assertEquals("",dict.getTranslation("asdfasdf"));
        Assert.assertEquals("",dict.getTranslation(""));
        Assert.assertEquals("",dict.getTranslation(null));
    }
}
