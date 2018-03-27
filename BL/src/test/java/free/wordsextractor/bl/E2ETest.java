package free.wordsextractor.bl;

import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.Dictionary;
import free.wordsextractor.bl.translation.Translation;
import free.wordsextractor.bl.translation.TranslationManager;
import free.wordsextractor.bl.translation.yandex.YandexTranslation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runners.Parameterized;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class E2ETest {
    @DisplayName("E2E test")
    @Test
    @ParameterizedTest(name = "{index} => path={0}, fromLang={1}, toLang={2}")
    @CsvSource({"eng.txt, eng, ru"})
    @Parameterized.Parameters
    public void e2e(String path, String langFrom, String langTo) {
        try {
            String EXTRACTED_WORDS_FILE = "extracted.words";

            final FileManager fileMngr = new FileManager(path);
            final List<Path> pathsList = fileMngr.extractTxtFiles(123);
            final WordsExtractor extractor = new WordsExtractor(pathsList);
            final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

            final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
            translationMngr.removeKnownWords(Paths.get(TranslationManager.KNOWN_WORDS_FILE_NAME));
            List<String> words = translationMngr.getExtractedWordsDict().getWords();
            new YandexTranslation(Translation.Langs.getLang(langFrom), Translation.Langs.getLang(langTo)).translate(words);
        }
        catch (WordsExtractorException e) {
            System.err.println("Running interrupted by exception " + e);
        }
    }
}
