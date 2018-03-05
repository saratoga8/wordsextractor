package free.wordsextractor.bl.txt_proc;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DictionaryTest {

    private Dictionary dict;

    @BeforeEach
    public void init() {
        dict = new Dictionary();
    }

    @DisplayName("Add words to Dictionary")
    @Test
    public void addWords() {
        dict.addWord("one");
        Assert.assertTrue("The dictionary doesn't contain the first word", dict.contains("one"));
        Assert.assertEquals("Statistics is invalid after adding the first word", dict.toString(), "one 1");

        dict.addWord("two");
        Assert.assertTrue("The dictionary doesn't contain the second word", dict.contains("two"));
        Assert.assertTrue("The first word has disappeared after adding the second", dict.contains("one"));
        Assert.assertEquals("Statistics is invalid after adding the second word", dict.toString(), "one 1two 1");

        dict.addWord("one");
        Assert.assertEquals("Statistics is invalid after adding the first existing word", dict.toString(), "one 2two 1");
        dict.addWord("two");
        Assert.assertEquals("Statistics is invalid after adding the second existing word", dict.toString(), "one 2two 2");
    }

    @DisplayName("Add words with punctuations and numbers")
    @ParameterizedTest
    @ValueSource(strings = {"word,", ";word", ":word-", "   word   ", "34word", "word24", "10word21"})
    public void addWordsPunct(String word) {
        dict.addWord(word);
        Assert.assertTrue("The word 'word' hasn't added to dictionary", dict.contains("word"));
    }

    @DisplayName("Empty dictionary")
    @Test
    public void emptyDict() {
        Assert.assertEquals(new Dictionary().toString(), "");
    }

    @DisplayName("Add an empty word, only punctuations or only nums")
    @ParameterizedTest
    @ValueSource(strings = {"", "    ", ":-", " 2,", "45"})
    public void addEmptyWord() {
        dict.addWord("");
        Assert.assertEquals(dict.toString(), "");
    }

    @DisplayName("Adding words to a dictionary from different threads")
    @Test
    public void differentThreads() {
        
    }
}