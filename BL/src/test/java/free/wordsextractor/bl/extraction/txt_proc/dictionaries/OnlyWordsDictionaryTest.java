package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class OnlyWordsDictionaryTest {

    @DisplayName("Create only words dictionary from a file")
    @ParameterizedTest
    @ValueSource(strings = {"list.txt"})
    public void createDictFromFile(String fileName) {
        try {
            Dictionary dict = new OnlyWordsDictionary(Paths.get(Utils.getResourcePath(this, fileName)));
            Assert.assertEquals("The words in dictionary aren't equals to the words from file","[one, two, three, four, five, six, seven, eight]", dict.getWords().toString());
        } catch (IOException | WordsExtractorException | URISyntaxException e) {
            Assert.assertTrue("The test has aborted because of exception: " + e, false);
        }
    }

    @DisplayName("Create only words dictionary from an empty file")
    @ParameterizedTest
    @ValueSource(strings = {"empty.txt"})
    public void createDictFromEmptyFile(String fileName) {
        try {
            new OnlyWordsDictionary(Paths.get(Utils.getResourcePath(this, fileName)));
            Assert.assertTrue("Should be thrown an exception at the previous line", false);
        } catch (Exception e) {
            Assert.assertEquals("The thrown exception is of another type", WordsExtractorException.class, e.getClass());
        }
    }




}