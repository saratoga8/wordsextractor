package free.wordsextractor.bl.txt_proc;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DictionaryTest {
    @Test
    public void addWords() {
        Dictionary dict = new Dictionary();
        dict.addWord("one");
        Assert.assertTrue(dict.contains("one"));
        dict.addWord("two");
        Assert.assertTrue(dict.contains("two"));
    }
}