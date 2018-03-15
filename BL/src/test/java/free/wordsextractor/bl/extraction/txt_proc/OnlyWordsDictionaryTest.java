package free.wordsextractor.bl.extraction.txt_proc;

import free.wordsextractor.bl.WordsExtractorException;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class OnlyWordsDictionaryTest {

    @DisplayName("Create only words dictionary from a file")
    @ParameterizedTest
    @ValueSource(strings = {"list.txt"})
    public void createDictFromFile(String fileName) {
        try {
            Dictionary dict = getDictionaryFromFile(this, fileName);
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
            getDictionaryFromFile(this, fileName);
            Assert.assertTrue("Should be thrown an exception at the previous line", false);
        } catch (IOException | WordsExtractorException | URISyntaxException e) {
            Assert.assertEquals("The thrown exception is of another type", WordsExtractorException.class, e.getClass());
        }
    }


    public static Dictionary getDictionaryFromFile(Object obj, String fileName) throws IOException, WordsExtractorException, URISyntaxException {
        URL path = obj.getClass().getClassLoader().getResource(fileName);
        Assert.assertNotNull("Can't found the resource file " + fileName, path);

        return new OnlyWordsDictionary(Paths.get(path.toURI()));
    }
}