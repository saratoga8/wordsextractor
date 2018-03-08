package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.file_proc.extractors.TextExtractorInterface;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileManagerTest {

    @DisplayName("Extract text files from different formats files")
    @ParameterizedTest
    @ValueSource(strings = {"eng.txt", "cyr.txt", "heb.txt"})
    void extractTxtFiles(String fileName) {

        try {
            URL path = this.getClass().getClassLoader().getResource(fileName);
            Assert.assertNotNull("Can't found the resource file " + fileName, path);

            String TXT_FILE_PATH = Paths.get(path.toURI()).toString();

            Path extractedTxtFile = new FileManager(TXT_FILE_PATH).extractTxtFiles(123).get(0);

            String actualTxt = new String(Files.readAllBytes(extractedTxtFile), Charset.forName(TextExtractorInterface.CHAR_SET));
            String expectedTxt = new String(Files.readAllBytes(Paths.get(TXT_FILE_PATH)), Charset.forName(TextExtractorInterface.CHAR_SET));

            Assert.assertEquals("Texts aren't equals", expectedTxt.trim(), actualTxt.trim());
        } catch (Exception e) {
            Assert.assertTrue("There is exception: " + e.toString(), false);
        }
    }

    @DisplayName("Empty text file")
    @ParameterizedTest
    @ValueSource(strings = {"empty.txt"})
    void emptyFile(String fileName) {
        try {
            URL path = this.getClass().getClassLoader().getResource(fileName);
            Assert.assertNotNull("Can't found the resource file " + fileName, path);

            String TXT_FILE_PATH = Paths.get(path.toURI()).toString();
            new FileManager(TXT_FILE_PATH).extractTxtFiles(123).get(0);
            Assert.assertTrue("There should be exception of an empty text file", false);
        } catch (Exception e) {

        }
    }

    @Test
    @DisplayName("Constructor with non-existing file")
    void nonExistingFile() {
        String nonExisting = "adsfasd\\adsfadf\\dasfasd";

        try {
            new FileManager(nonExisting);
        }
        catch (Exception e) {
            if(e.getClass().equals(WordsExtractorException.class))
                return;
        }
        Assert.assertTrue("An exception hasn't thrown", false);
    }
}