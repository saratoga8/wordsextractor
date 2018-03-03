package free.wordsextractor.bl.txt_proc;

import free.wordsextractor.bl.file_proc.Utils;
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
            String words = new WordsExtractor(Arrays.asList(Paths.get(path.toURI()))).createDictionary().toString();

            String out = Utils.runSystemCommand(Utils.Shells.BASH, "cat " + path.getPath() + " | tr [[:punct:]] ' ' | awk '{for(i=1;i<=NF;i++) a[$i]++} END {for(k in a) print k,a[k]}' | sort");

            Assert.assertEquals(words, out);
        } catch (Exception e) {
            System.err.println("Test aborted because of the exception: " + e);
        }
    }
}