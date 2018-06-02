import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.translation.Translation;
import free.wordsextractor.bl.translation.TranslationManager;
import free.wordsextractor.bl.translation.yandex.YandexTranslation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String [] args) {
        String txtFilePath = args[1];
        String apiKeyPath = args[2];
        String knownWordsPath = args[3];

        try {
            final FileManager fileMngr = new FileManager(txtFilePath);
            final List<Path> pathsList = fileMngr.extractTxtFiles(123);
            final WordsExtractor extractor = new WordsExtractor(pathsList);
            final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

            final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
            translationMngr.removeKnownWords(Paths.get(knownWordsPath));
            final Dictionary unknownWordsDict = translationMngr.getExtractedWordsDict();
//            wordsStatsDict = wordsStatsDict.extractWordsOfDict(unknownWordsDict);

            Dictionary translations = new YandexTranslation(Paths.get(apiKeyPath), Translation.Langs.getLang("eng"), Translation.Langs.getLang("ru")).translate(unknownWordsDict.getWords());


//            String translationsDictPath = translations.saveIn();

//            Application.launch(WordsExtractorGUI.class, );
        }
        catch (WordsExtractorException e) {
            System.err.println("Running interrupted by exception " + e);
        }

    }
}
