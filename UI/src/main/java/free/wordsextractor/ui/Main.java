package free.wordsextractor.ui;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.OnlyWordsDictionary;
import free.wordsextractor.bl.translation.Translation;
import free.wordsextractor.bl.translation.TranslationManager;
import free.wordsextractor.bl.translation.yandex.YandexTranslation;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static String[] translate(String[] paths) throws IOException, WordsExtractorException {
        String txtFilePath = paths[0];
        String apiKeyPath = paths[1];
        Path knownWordsPath = (paths.length > 2) ? Paths.get(paths[2]): createLocalEmptyFile("knowns.dict");

        if(knownWordsPath == null)
            return new String[] {};

        final FileManager fileMngr = new FileManager(txtFilePath);
        final List<Path> pathsList = fileMngr.extractTxtFiles(123);

        final WordsExtractor extractor = new WordsExtractor(pathsList);
        final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

        final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
        translationMngr.removeKnownWords(knownWordsPath);
        final Dictionary unknownWordsDict = translationMngr.getExtractedWordsDict();

        final Dictionary knownWordsDict = new OnlyWordsDictionary(knownWordsPath);
        wordsStatsDict.removeWordsOfDict(knownWordsDict);

        final Dictionary translations = new YandexTranslation(Paths.get(apiKeyPath), Translation.Langs.getLang("eng"), Translation.Langs.getLang("ru")).translate(unknownWordsDict.getWords());

        String createdPaths[] = { ".translations", ".stats", ".knowns" };
        translations.saveAsBinIn(paths[0]);
        wordsStatsDict.saveAsBinIn(paths[1]);
        knownWordsDict.saveAsBinIn(paths[2]);
        return createdPaths;
    }

    public static void main(String [] args) {
        try {
            String [] paths = translate(args);
            Application.launch(WordsExtractorGUI.class, paths);
        }
        catch (WordsExtractorException | IOException e) {
            System.err.println("Running interrupted by exception " + e.fillInStackTrace());
        }
    }

    private static Path createLocalEmptyFile(final String name) {
        try {
            File file = new File(name);
            file.createNewFile();
            return Paths.get(file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Can't create an empty file: " + e);
            return null;
        }
    }
}
