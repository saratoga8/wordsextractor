package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class WordsExtractorGUI extends Application {
    private static final Logger log = LogManager.getLogger(WordsExtractorGUI.class);

    protected TranslationsDictionary dict = new TranslationsDictionary();
    private Parent root;

    @Override
    public void init() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("found_words_list.fxml"));
        ListWordsViewController controller = new ListWordsViewController(dict);
        loader.setController(controller);
        root = loader.load();
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(root, 850, 550));
        stage.centerOnScreen();
        stage.setTitle("Extracted words from text");
        stage.setOnCloseRequest(handle -> {
            log.debug("on closing");
        });
        stage.show();
    }

    @Override
    public void stop() {
        log.debug("stop the app");
    }

/*    private void bl() {
        if (args.length > 2) {
            String txtFilePath = args[1], extractedWordsTxtFilePath = args[2];

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
        }
        else
            System.err.println("Invalid number of parameters.\nShould be: " + args[0] + " file1 file2\nWhere: \n\tfile1 - where text should be extracted from\n\tfile2 - where the extracted text should be saved");
    }*/
}
