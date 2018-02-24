package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordExtractorException;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(Parameterized.class)
class FileManagerTest {
    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[] { "cyr.txt", "eng.txt", "heb.txt"};
    }

    @Parameterized.Parameter(0)
    private String fileName;

    public FileManagerTest(String fileName) {
        this.fileName = fileName;
    }

    @Test
    void extractTxtFiles() {
        try {
            String TXT_FILE_PATH = this.getClass().getClassLoader().getResource(fileName).getPath();
            Path extractedTxtFile = new FileManager(TXT_FILE_PATH).extractTxtFiles(123).get(0);
            String extractedTxt = new String (Files.readAllBytes(extractedTxtFile), Charset.forName("UTF-16"));
            String actualTxt = new String (Files.readAllBytes(Paths.get(TXT_FILE_PATH)));

            Assert.assertEquals("Texts aren't equals", extractedTxt.trim(), actualTxt.trim());
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