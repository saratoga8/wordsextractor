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

class OnlyWordsDictionaryTest {

    @DisplayName("Create only words dictionary from a file")
    @ParameterizedTest
    @ValueSource(strings = {"list.txt"})
    public void createDictFromFile(String fileName) {
        try {
            URL path = this.getClass().getClassLoader().getResource(fileName);
            Assert.assertNotNull("Can't found the resource file " + fileName, path);

            Dictionary dict = new OnlyWordsDictionary(Paths.get(path.toURI()));
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
            URL path = this.getClass().getClassLoader().getResource(fileName);
            Assert.assertNotNull("Can't found the resource file " + fileName, path);

            Dictionary dict = new OnlyWordsDictionary(Paths.get(path.toURI()));
            Assert.assertEquals("The words in dictionary aren't equals to the words from file","[one, two, three, four, five, six, seven, eight]", dict.getWords().toString());
        } catch (IOException | WordsExtractorException | URISyntaxException e) {
            Assert.assertTrue("The test has aborted because of exception: " + e, false);
        }
    }
}