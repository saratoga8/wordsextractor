package free.wordsextractor.bl.txt_proc;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DictionaryTest {
    @Test
    public void emptyDict() {
        Assert.assertTrue("Created dictionary shouldn't contain any words", new Dictionary().getWords().isEmpty());
    }

    @Test
    public void addWords() {
        Dictionary dict = new Dictionary();
        dict.addWord("one");
        Assert.assertTrue(dict.getWords().contains("one"));
        dict.addWord("two");
        Assert.assertTrue(dict.getWords().contains("two"));
        Assert.assertEquals(dict.getWords().size(), 2);
    }
}