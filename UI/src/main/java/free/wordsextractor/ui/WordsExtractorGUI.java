package free.wordsextractor.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WordsExtractorGUI extends Application {
    public static void main(String [] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new FindWordsViewController());
        stage.setScene(scene);
        stage.setTitle("Extracted words from text");
        stage.show();

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
