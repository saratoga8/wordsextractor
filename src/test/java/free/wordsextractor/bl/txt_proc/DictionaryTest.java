package free.wordsextractor.bl.txt_proc;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.file_proc.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class DictionaryTest {
    private static Utils.Shells shell;
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
        Assert.assertEquals("Statistics is invalid after adding the first word", "one 1", dict.toString());

        dict.addWord("two");
        Assert.assertTrue("The dictionary doesn't contain the second word", dict.contains("two"));
        Assert.assertTrue("The first word has disappeared after adding the second", dict.contains("one"));
        Assert.assertEquals("Statistics is invalid after adding the second word", "one 1two 1", dict.toString());

        dict.addWord("one");
        Assert.assertEquals("Statistics is invalid after adding the first existing word", "one 2two 1", dict.toString());
        dict.addWord("two");
        Assert.assertEquals("Statistics is invalid after adding the second existing word", "one 2two 2", dict.toString());
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
        Assert.assertEquals("", dict.toString());
    }

    @DisplayName("Adding words to a dictionary from different threads")
    @Test
    public void differentThreads() {
        List<String> words1 = Arrays.asList("one", "two", "three", "four");
        List<String> words2 = Arrays.asList("one", "two", "three", "four");

        Arrays.asList(words1, words2).parallelStream().forEach(words -> words.parallelStream().forEach(word -> dict.addWord(word)));
        Assert.assertEquals("four 2one 2three 2two 2", dict.toString());
    }

    @DisplayName("Save dictionary in a file")
    @Test
    public void saveDict() {
        Arrays.asList("one", "two", "three", "four").parallelStream().forEach(word -> dict.addWord(word));
        try {
            String path = File.createTempFile("dict", ".txt").getAbsolutePath();
            dict.save(path);

            String commandStr = ""; /*
            switch (shell) {
                case BASH: commandStr = "cat " + path;
                case POWERSHELL: Assert.assertTrue("For PowerShlell hasn't implemented yet", false); return "";
                default:
                    log.error("Unknown shell " + shell.name());
            }
            String out = Utils.runSystemCommand(, ) */
        } catch (IOException | WordsExtractorException e) {
            Assert.assertTrue("Test aborted because of exception: " + e, false);
        }
    }
}