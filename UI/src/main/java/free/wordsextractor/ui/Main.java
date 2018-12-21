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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * The main class of the application
 */
public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    private static int FROM = 0, TO = 1;

    private static final Path KNOWNS_DICT_DEFAULT_PATH = Paths.get("knowns.dict");
    private static final String TRANSLATIONS_FILE_NAME = ".translations";
    private static final String STATS_FILE_NAME = ".stats";


    /**
     * Translate the text from the file with the given path
     * @param paths Array of paths of used files
     * @return Array of paths of created files
     * @throws IOException
     * @throws WordsExtractorException
     */
    public static String[] translate(String[] paths, Translation.Langs[] langs) throws IOException, WordsExtractorException {
        if (paths.length < 2)
            return getLastSessionPaths(paths);

        String txtFilePath = paths[0];
        String apiKeyPath = paths[1];
        final Path knownWordsPath = (paths.length > 2) ? Paths.get(paths[2]): createLocalEmptyFile(KNOWNS_DICT_DEFAULT_PATH);

        log.debug("Extracting text file from " + txtFilePath);
        final FileManager fileMngr = new FileManager(txtFilePath);
        final List<Path> pathsList = fileMngr.extractTxtFiles(123);

        log.debug("Extract words from file " + pathsList.get(0).toString());
        final WordsExtractor extractor = new WordsExtractor(pathsList);
        final Dictionary wordsStatsDict = extractor.createWordsStatsDictionary(langs[FROM]);

        log.debug("Remove know words by file " + knownWordsPath.toString() + " from words");
        final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
        final Dictionary unknownWordsDict = translationMngr.getExtractedWordsDict();
        final OnlyWordsDictionary knownWordsDict = new OnlyWordsDictionary(knownWordsPath, langs[FROM]);
        unknownWordsDict.removeWordsOfDictUsingParser(knownWordsDict);

        log.debug("Translate words with API from " + apiKeyPath);
        final Dictionary translations = new YandexTranslation(Paths.get(apiKeyPath), Translation.Langs.getLang("eng"), Translation.Langs.getLang("ru")).translate(unknownWordsDict.getWords());

        log.debug("Save binary dictionaries");
        final Map<Dictionary, String> dictsMap = Map.of(translations, TRANSLATIONS_FILE_NAME, wordsStatsDict, STATS_FILE_NAME);
        saveDictionaries(dictsMap);
        return new String[] {TRANSLATIONS_FILE_NAME, STATS_FILE_NAME, knownWordsPath.toString()};
    }

    private static String[] getLastSessionPaths(String[] paths) throws WordsExtractorException {
        if  (!Files.exists(Paths.get(STATS_FILE_NAME)) || !Files.exists(Paths.get(TRANSLATIONS_FILE_NAME)))
            throw new WordsExtractorException("Invalid arguments list. The arguments should be: path1 path2 path3(optional)\nWhere: path1 - path of a being translated file\n       path2 - path of a file with API key\n       path3 - path of a file with known words");
        if ((paths.length == 0) && (!Files.exists(KNOWNS_DICT_DEFAULT_PATH)))
            throw new WordsExtractorException("Known words dictionary hasn't found. The path to one can be passed in parameter");
        return new String[] {TRANSLATIONS_FILE_NAME, STATS_FILE_NAME, paths[0]};
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
            String [] paths = translate(args, new Translation.Langs[] {Translation.Langs.ENG, Translation.Langs.RUS});
            Application.launch(WordsExtractorGUI.class, paths);
        }
        catch (WordsExtractorException | IOException e) {
            System.err.println("Running interrupted by exception: " + e);
        }
    }

    /**
     * Create local empty file
     * @param path File's name
     * @return File's path
     * @throws IOException
     */
    private static Path createLocalEmptyFile(Path path) throws IOException {
        return Files.createFile(path).getFileName();
    }
}
