package free.wordsextractor.bl.txt_proc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

class WordsExtractorTest {
    @Test
    @ParameterizedTest
    @ValueSource(strings = {"eng.txt"})
    public void wordsExtraction(String fileName) {
        URL path = this.getClass().getClassLoader().getResource(fileName);
        Assert.assertNotNull("Can't found the resource file " + fileName, path);

        try {
            String words = new WordsExtractor(Arrays.asList(Paths.get(path.toURI()))).createDictionary().getWords().toString();
            Assert.assertEquals(words, "");
        } catch (Exception e) {
            System.err.println("Test aborted because of the exception: " + e);
        }
    }
}