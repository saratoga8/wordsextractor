package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import free.wordsextractor.common.tests.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class OnlyWordsDictionaryTest {
    private OnlyWordsDictionary dict;

    @BeforeEach
    void setUp() {
        dict = new OnlyWordsDictionary();
    }

    @DisplayName("Create only words dictionary from a file")
    @ParameterizedTest
    @ValueSource(strings = {"list.txt"})
    public void createDictFromFile(String fileName) {
        try {
            Dictionary dict = new OnlyWordsDictionary(Paths.get(Utils.getResourcePathStr(this, fileName)));
            Assert.assertEquals("The words in dictionary aren't equals to the words from file","[six, four, one, seven, two, three, five, eight]", dict.getWords().toString());
        } catch (IOException | URISyntaxException e) {
            Assert.assertTrue("The test has aborted because of exception: " + e, false);
        }
    }

    @DisplayName("Create only words dictionary from an empty file")
    @ParameterizedTest
    @ValueSource(strings = {"empty.txt"})
    public void createDictFromEmptyFile(String fileName) {
        try {
            Assert.assertTrue(new OnlyWordsDictionary(Paths.get(Utils.getResourcePathStr(this, fileName))).getWords().isEmpty());
        } catch (Exception e) {
            Assert.assertTrue("The test aborted due the exception: " + e, false);
        }
    }

    @DisplayName("Adds words")
    @Test
    void addWords() {
        dict.addWord("one");
        dict.addWord("two");
        dict.addWord("three");

        Assert.assertEquals("[one, two, three]", dict.getWords().toString());
    }

    @DisplayName("Contains words")
    @Test
    void containsWords() {
        dict.addWord("one");
        dict.addWord("two");
        dict.addWord("three");

        Assert.assertTrue(dict.contains("one"));
        Assert.assertTrue(dict.contains("two"));
        Assert.assertTrue(dict.contains("three"));

        Assert.assertFalse(dict.contains("asdfasdfasdfasf"));
    }

    @DisplayName("Removes words")
    @Test
    void remWords() {
        dict.addWord("one");
        dict.addWord("two");
        dict.addWord("three");

        Assert.assertEquals("[one, two, three]", dict.getWords().toString());

        dict.removeWord("two");
        Assert.assertEquals("[one, three]", dict.getWords().toString());
        dict.removeWord("one");
        Assert.assertEquals("[three]", dict.getWords().toString());
        dict.removeWord("three");
        Assert.assertTrue(dict.getWords().isEmpty());
    }

    @DisplayName("Case sensitive")
    @Test
    void casesensitive() {
        dict.addWord("one");
        dict.addWord("two");
        dict.addWord("three");

        Assert.assertTrue(dict.contains("One"));
        Assert.assertTrue(dict.contains("tWo"));
        Assert.assertTrue(dict.contains("THREE"));

        dict.addWord("Four");
        dict.addWord("fiVE");
        dict.addWord("SIX");

        Assert.assertTrue(dict.contains("four"));
        Assert.assertTrue(dict.contains("five"));
        Assert.assertTrue(dict.contains("six"));
    }

    @DisplayName("Save/Read dictionary")
    @Test
    void saveDict() {
        try {
            dict.addWord("one");
            dict.addWord("two");
            dict.addWord("three");

            File file = File.createTempFile("dict", "bin");
            file.deleteOnExit();
            dict.saveAsBinIn(file.getAbsolutePath());
            OnlyWordsDictionary readDict = Dictionary.readAsBinFrom(file.getAbsolutePath());

            Assert.assertTrue(readDict.contains("one"));
            Assert.assertTrue(readDict.contains("two"));
            Assert.assertTrue(readDict.contains("three"));

        } catch (IOException | ClassNotFoundException e) {
            Assert.assertTrue("Test aborted because of: " + e, false);
        }
    }
}