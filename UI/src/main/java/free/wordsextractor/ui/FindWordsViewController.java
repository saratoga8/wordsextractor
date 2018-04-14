package free.wordsextractor.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class FindWordsViewController extends VBox {
    private static final Logger log = LogManager.getLogger(FindWordsViewController.class);

    public FindWordsViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("found_words_list.fxml"));
//        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
