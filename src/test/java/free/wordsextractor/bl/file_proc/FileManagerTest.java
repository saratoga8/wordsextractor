package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordExtractorException;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileManagerTest {

    @DisplayName("Extract text files from different formats files")
    @ParameterizedTest
    @ValueSource(strings = {"eng.txt", "heb.txt", "cyr.txt"})
    void extractTxtFiles(String fileName) {
        try {
            String TXT_FILE_PATH = Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI()).toString();

            Path extractedTxtFile = new FileManager(TXT_FILE_PATH).extractTxtFiles(123).get(0);

            String actualTxt = new String (Files.readAllBytes(extractedTxtFile), Charset.forName(TextExtractor.CHAR_SET));
            String expectedTxt = new String (Files.readAllBytes(Paths.get(TXT_FILE_PATH)), Charset.forName(TextExtractor.CHAR_SET));

             Assert.assertEquals("Texts aren't equals", expectedTxt.trim(), actualTxt.trim());
        } catch (Exception e) {
            Assert.assertTrue("There is exception: " + e.toString(), false);
        }
    }

    @Test
    @DisplayName("Constructor with non-existing file")
    void creating() {
        String nonExisting = "adsfasd\\adsfadf\\dasfasd";

        try {
            new FileManager(nonExisting);
        }
        catch (Exception e) {
            if(e.getClass().equals(WordExtractorException.class))
                return;
        }
        Assert.assertTrue("An exception hasn't thrown", false);
    }
}