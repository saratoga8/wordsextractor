import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;


class E2ETest {
    @Test
    void main() {
        try {
            String knownWordsPath = FileManager.getResourcesFilePath("knowns.dict", this).toString();
            String txtPath = FileManager.getResourcesFilePath("eng.txt", this).toString();
            String apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this).toString();
            new Main().main(new String[] {txtPath, apiKeyPath, knownWordsPath});
        } catch (WordsExtractorException | URISyntaxException e) {
            System.err.println("Test aborted because of: " + e);
        }
    }
}