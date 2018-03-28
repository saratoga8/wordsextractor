package free.wordsextractor.bl;

import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.Utils;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.translation.TranslationManager;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@DisplayName("E2E Tests")
public class E2ETest {

    @Ignore
    @DisplayName("E2E test without known words")
    @ParameterizedTest(name = "{index} => path={0}, fromLang={1}, toLang={2}")
    @CsvSource({"eng.txt, eng, ru"})
    public void e2eWithoutKnowns(String path, String langFrom, String langTo) {
        System.out.println("Path: " + path + ", from " + langFrom + ", to " + langTo);
        try {
            String EXTRACTED_WORDS_FILE = "extracted.words";

            final FileManager fileMngr = new FileManager(Utils.getResourcePath(this, path));
            final List<Path> pathsList = fileMngr.extractTxtFiles(123);
            final WordsExtractor extractor = new WordsExtractor(pathsList);
            final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

            final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
            translationMngr.removeKnownWords(Paths.get(TranslationManager.KNOWN_WORDS_FILE_NAME));
            List<String> words = translationMngr.getExtractedWordsDict().getWords();
           // String translation = new YandexTranslation(Translation.Langs.getLang(langFrom), Translation.Langs.getLang(langTo)).translate(words);
           // System.out.println("Translation: " + translation);
        }
        catch (WordsExtractorException | URISyntaxException e) {
            System.err.println("Running interrupted by exception " + e);
        }
    }
}
