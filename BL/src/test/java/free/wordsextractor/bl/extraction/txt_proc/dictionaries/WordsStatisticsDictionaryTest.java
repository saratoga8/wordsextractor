package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.Utils;
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

class WordsStatisticsDictionaryTest {

    private WordsStatisticsDictionary dict;

    @BeforeEach
    public void init() {
        dict = new WordsStatisticsDictionary();
    }

    @DisplayName("Add words to WordsStatisticsDictionary")
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
        Assert.assertEquals(new WordsStatisticsDictionary().toString(), "");
    }

    @DisplayName("Add an empty word, only punctuations or only nums")
    @ParameterizedTest
    @ValueSource(strings = {"", "    ", ":-", " 2,", "45"})
    public void addEmptyWord(String word) {
        dict.addWord(word);
        Assert.assertEquals("", dict.toString());
    }

    @DisplayName("Adding words to a dictionary from a same thread")
    @Test
    public void sameThread() {
        List<String> words1 = Arrays.asList("one", "two", "three", "four");
        List<String> words2 = Arrays.asList("one", "two", "three", "four");

        words1.forEach(dict::addWord);
        words2.forEach(dict::addWord);
        dict.getWords().sort(String::compareToIgnoreCase);
        Assert.assertEquals("four 2one 2three 2two 2", dict.toString());
    }

    @DisplayName("Adding words to a dictionary from different threads")
    @Test
    public void differentThreads() {
        List<String> words1 = Arrays.asList("one", "two", "three", "four");
        List<String> words2 = Arrays.asList("one", "two", "three", "four");

        Arrays.asList(words1, words2).parallelStream().forEach(words -> words.parallelStream().forEach(word -> dict.addWord(word)));
        dict.getWords().sort(String::compareToIgnoreCase);
        Assert.assertEquals("four 2one 2three 2two 2", dict.toString());
    }

    @DisplayName("Save dictionary in a file")
    @Test
    public void saveDict() {
        Arrays.asList("one", "two", "three", "four").parallelStream().forEach(word -> dict.addWord(word));
        try {
            File file = File.createTempFile("dict", ".txt");
            file.deleteOnExit();
            String path = file.getAbsolutePath();
            dict.saveAsTxtIn(path);

            String commandStr = "";
            Utils.Shells shell = Utils.getShell();
            switch (shell) {
                case BASH: commandStr = "cat " + path; break;
                case POWERSHELL: commandStr = "Get-Content " + path; break;
                default:
                    Assert.assertTrue("Unknown shell " + shell.name(), false);
            }
            Assert.assertEquals(Utils.runSystemCommand(commandStr), "four 1one 1three 1two 1");
        } catch (IOException | WordsExtractorException e) {
            Assert.assertTrue("Test aborted because of exception: " + e, false);
        }
    }

    @DisplayName("Get words of the dictionary")
    @Test
    public void getDictWords() {
        List<String> words1 = Arrays.asList("one", "two", "three", "four");
        words1.parallelStream().forEach(word -> dict.addWord(word));
        List<?> fromDict = dict.getWords();
        words1.forEach(word -> Assert.assertTrue("There is no word " + word + " in dictionary", fromDict.contains(word)));
    }

    @DisplayName("The dictionary contains words")
    @Test
    public void dictContainsWords() {
        List<String> words1 = Arrays.asList("one", "two", "three", "four");
        words1.parallelStream().forEach(word -> dict.addWord(word));
        words1.forEach(word -> Assert.assertTrue("There is no word " + word + " in dictionary", dict.contains(word)));

        Assert.assertFalse("A dictionary cant contain an EMPTY string", dict.contains(""));
    }

    @DisplayName("Remove words from dictionary")
    @Test
    public void removeDicWords() {
        List<String> words1 = Arrays.asList("one", "two", "three", "four");
        words1.parallelStream().forEach(word -> dict.addWord(word));

        Assert.assertFalse("It's invalid to remove an empty word from dictionary", dict.removeWord(""));

        for (String word: words1) {
            Assert.assertTrue("There is no word '" + word + "' before remove in the dictionary", dict.contains(word));
            Assert.assertTrue("Can't remove the word '" + word + "' from dictionary", dict.removeWord(word));
            Assert.assertFalse("There is still word '" + word + "' after remove in the dictionary", dict.contains(word));
        }
        Assert.assertTrue("After removing all words added before, the dictionary isn't empty", dict.getWords().isEmpty());
    }
}