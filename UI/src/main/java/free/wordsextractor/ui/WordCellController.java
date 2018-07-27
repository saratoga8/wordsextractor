package free.wordsextractor.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class WordCellController extends ListCell<WordInfo> {
    @FXML
    private HBox listCell;

    @FXML
    private Label wordLbl, statsLbl;

    @Override
    protected void updateItem(WordInfo item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            wordLbl.setText(item.getWord());
            statsLbl.setText(item.getStats());
        }
    }

    public String getWord() {
        return wordLbl.getText();
    }
}
