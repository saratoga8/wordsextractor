package free.wordsextractor.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class WordCellController extends ListCell<WordInfo> {
    private static final Logger log = LogManager.getLogger(WordCellController.class);

    @FXML
    private HBox listCell;

    @FXML
    private Label wordLbl, statsLbl;

    private FXMLLoader loader;


    @Override
    protected void updateItem(WordInfo item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            if (loader == null)
                initLoader();
            wordLbl.setText(item.getWord());
            statsLbl.setText(item.getStats());
            setGraphic(listCell);
        }
        setText(null);
    }

    private void initLoader() {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("list_cell.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            log.error("Can't load the loader of WordCellController");
        }
    }

    public String getWord() {
        return (wordLbl != null) ? wordLbl.getText(): "";
    }
}
