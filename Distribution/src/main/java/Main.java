import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import free.wordsextractor.bl.translation.TranslationManager;
import free.wordsextractor.ui.WordsExtractorGUI;
import javafx.application.Application;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String [] args) {
        String txtFilePath = args[1],

        try {
            final FileManager fileMngr = new FileManager(txtFilePath);
            final List<Path> pathsList = fileMngr.extractTxtFiles(123);
            final WordsExtractor extractor = new WordsExtractor(pathsList);
            Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

            final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
            translationMngr.removeKnownWords(Paths.get(TranslationManager.KNOWN_WORDS_FILE_NAME));
            translationMngr.getExtractedWordsDict().saveIn(extractedWordsTxtFilePath);
        }
        catch (WordsExtractorException e) {
            System.err.println("Running interrupted by exception " + e);
        }
        Application.launch(WordsExtractorGUI.class, args);
    }
}
