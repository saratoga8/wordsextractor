package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OnlyWordsDictionaryStressTest {
    private OnlyWordsDictionary dict;

    @BeforeEach
    void setUp() {
        dict = new OnlyWordsDictionary();

        dict.addWord("one");
        dict.addWord("two");
        dict.addWord("three");
    }

    @DisplayName("Add NULL or EMPTY words")
    @Test
    void addWord() {
        dict.addWord("");
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
        dict.addWord(null);
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
    }

    @DisplayName("Contains NULL or EMPTY words")
    @Test
    void contains() {
        dict.addWord("");
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
        dict.addWord(null);
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
    }

    @DisplayName("Remove NULL or EMPTY words")
    @Test
    void removes() {
        dict.removeWord("");
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
        dict.removeWord(null);
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
        dict.removeWord("asdfasdfasdf");
        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
    }
}
