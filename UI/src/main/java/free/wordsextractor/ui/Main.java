package free.wordsextractor.ui;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
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
import java.util.Map;

/**
 * The main class of the application
 */
public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    /**
     * Translate the text from the file with the given path
     * @param paths Array of paths of used files
     * @return Array of paths of created files
     * @throws IOException
     * @throws WordsExtractorException
     */
    public static String[] translate(String[] paths) throws IOException, WordsExtractorException {
        if (paths.length < 2)
            throw new WordsExtractorException("Invalid arguments list. The arguments should be: path1 path2 path3(optional)\nWhere: path1 - path of a file being translated\n       path2 - path of a file with API key\n       path3 - path of a file with known words");

        String txtFilePath = paths[0];
        String apiKeyPath = paths[1];
        final Path knownWordsPath = (paths.length > 2) ? Paths.get(paths[2]): createLocalEmptyFile("knowns.dict");

        log.debug("Extracting text file from " + txtFilePath);
        final FileManager fileMngr = new FileManager(txtFilePath);
        final List<Path> pathsList = fileMngr.extractTxtFiles(123);

        log.debug("Extract words from file " + pathsList.get(0).toString());
        final WordsExtractor extractor = new WordsExtractor(pathsList);
        final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

        log.debug("Remove know words by file " + knownWordsPath.toString() + " from words");
        final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
        translationMngr.removeKnownWords(knownWordsPath);
        final Dictionary unknownWordsDict = translationMngr.getExtractedWordsDict();

        log.debug("Translate words with API from " + apiKeyPath);
        final Dictionary translations = new YandexTranslation(Paths.get(apiKeyPath), Translation.Langs.getLang("eng"), Translation.Langs.getLang("ru")).translate(unknownWordsDict.getWords());

        log.debug("Save binary dictionaries");
        final Map<Dictionary, String> dictsMap = Map.of(translations, ".translations", wordsStatsDict, ".stats");
        saveDictionaries(dictsMap);
        return new String[] {".translations", ".stats", "knowns.dict"};
    }

    private static void saveDictionaries(final Map<Dictionary, String> dictsMap) {
        dictsMap.forEach((dict, path) -> {
            try {
                dict.saveAsBinIn(path);
            } catch (IOException e) {
                System.err.println("Can't save dictionary in file " + path + ": " + e);
            }
        });
    }

    /**
     * Main function
     * @param args App's parameters
     */
    public static void main(String [] args) {
        try {
            String [] paths = translate(args);
            Application.launch(WordsExtractorGUI.class, paths);
        }
        catch (WordsExtractorException | IOException e) {
            System.err.println("Running interrupted by exception: " + e);
        }
    }

    /**
     * Create local empty file
     * @param name File's name
     * @return File's path
     * @throws IOException
     */
    private static Path createLocalEmptyFile(String name) throws IOException {
        final File file = new File(name);
        file.createNewFile();
        return Paths.get(file.getAbsolutePath());
    }
}
