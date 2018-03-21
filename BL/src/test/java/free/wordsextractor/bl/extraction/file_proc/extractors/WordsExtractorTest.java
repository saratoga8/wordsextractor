package free.wordsextractor.bl.extraction.file_proc.extractors;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

class WordsExtractorTest {
    private static Logger log = LogManager.getLogger(WordsExtractorTest.class);


    @ParameterizedTest
    @ValueSource(strings = {"eng.txt"})
    public void wordsExtraction(String fileName) throws WordsExtractorException {
        URL path = this.getClass().getClassLoader().getResource(fileName);
        Assert.assertNotNull("Can't found the resource file " + fileName, path);

        String command = getCommandStr(path.getPath());
        if (!StringUtils.isBlank(command)) {
            try {
                String expectedWords = new WordsExtractor(Arrays.asList(Paths.get(path.toURI()))).createWordsStatsDictionary().toString();
                String actualWords = Utils.runSystemCommand(command);

                Assert.assertEquals(expectedWords, actualWords);
            } catch (Exception e) {
                log.error("Test aborted because of the exception: " + e);
            }
        }
    }

    private String getCommandStr(String path) throws WordsExtractorException {
        Utils.Shells shell = Utils.getShell();
        switch (shell) {
            case BASH: return "cat " + path + " | tr [[:punct:]] ' ' | awk '{for(i=1;i<=NF;i++) a[$i]++} END {for(k in a) print k,a[k]}' | sort";
            case POWERSHELL: log.warn("For PowerShlell hasn't implemented yet", false); return "";
            default:
                log.error("Unknown shell " + shell.name());
        }
        return "";
    }
}