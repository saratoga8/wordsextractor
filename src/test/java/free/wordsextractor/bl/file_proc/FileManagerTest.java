package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordExtractorException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileManagerTest {

    @Test
    @Ignore
    void extractTxtFiles() {
        Assert.assertTrue(false);
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