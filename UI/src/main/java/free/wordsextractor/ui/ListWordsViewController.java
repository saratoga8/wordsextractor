package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;
import java.util.function.Supplier;

public class ListWordsViewController {
    private static final Logger log = LogManager.getLogger(ListWordsViewController.class);

    @FXML
    private Text removingTxt;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> wordsListView;
    private int initialWordsNum = 0;
    private ObservableList<String> wordsList;
    final private Stack<Pair<String, String>> deletedWords = new Stack<>();

    @FXML
    private Button btnOK, btnCancel;
    @FXML
    private Button undoBtn, undoAllBtn;

    final private Dictionary dict;


    public ListWordsViewController(final Dictionary dict) {
        this.dict = dict;
    }

    private TranslationPopUp translationPopUp;

    @FXML
    private void initialize() {
        initList();
        initBtns();

        translationPopUp = new TranslationPopUp();
        initialWordsNum = dict.getWords().size();
    }

    private void initList() {
        wordsList = FXCollections.observableList(dict.getWords()).sorted();
        wordsListView.setItems(wordsList);

        wordsListView.setCellFactory(param -> {
            final WordCell cell = new WordCell();
            cell.setOnMouseClicked(event -> {
                if (event.getEventType() == MouseEvent.MOUSE_CLICKED)
                    handleWordSelection();
            });
            cell.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                final Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                String word = cell.getItem();
                if (!TextUtils.isBlank(word))
                    showTranslationPopUp(stage, word);
            });
            cell.addEventHandler(MouseEvent.MOUSE_EXITED, event -> translationPopUp.hide());
            return cell;
        });
    }

    private void showTranslationPopUp(Stage stage, String word) {
        translationPopUp.setTxt(dict.getTranslation(word));
        translationPopUp.show(stage);
        translationPopUp.setX(stage.getX() + stage.getWidth() + 3);
        translationPopUp.setY(stage.getY());
    }

    private void initBtns() {
        initButton(undoBtn,    MouseEvent.MOUSE_CLICKED, () -> undoHandling());
        initButton(undoAllBtn, MouseEvent.MOUSE_CLICKED, () -> undoAllHandling());

        btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            final Node source = (Node) event.getSource();
            ((Stage) source.getScene().getWindow()).close();
        });
    }

    private void handleWordSelection() {
        String selectedWord = wordsListView.getSelectionModel().getSelectedItem();
        if(!StringUtils.isBlank(selectedWord)) {
            deletedWords.push(new Pair<>(selectedWord, dict.getTranslation(selectedWord)));
            dict.removeWord(selectedWord);
            initList();
            removingTxt.setText("Removed " + deletedWords.size() + " from " + initialWordsNum);
        }
        else
            log.warn("Selected word is blank or NULL");
    }

    private class WordCell extends ListCell<String> {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item);
            }
        }
    }


    private Void undoHandling() {
        if (!deletedWords.isEmpty()) {
            Pair<String, String> deleted = deletedWords.pop();
            dict.addTranslation(deleted.getKey(), deleted.getValue());
            initList();
            removingTxt.setText("Selected " + deletedWords.size() + " from " + wordsList.size());
        }
        return null;
    }

    private Void undoAllHandling() {
        while(!deletedWords.isEmpty()) {
            Pair<String, String> deleted = deletedWords.pop();
            dict.addTranslation(deleted.getKey(), deleted.getValue());
        }
        initList();
        removingTxt.setText("Selected " + deletedWords.size() + " from " + wordsList.size());
        return null;
    }

    private static void initButton(final Button btn, EventType<MouseEvent> eventType, final Supplier<Void> supplier) {
        btn.addEventHandler(eventType, event -> supplier.get());
    }

    private class TranslationPopUp extends Popup {
        private Label lbl;

        public TranslationPopUp() {
            super();
            setX(300);
            setY(200);

            lbl = new Label();
            getContent().add(lbl);
        }

        public void setTxt(String txt) {
            lbl.setText(txt);
        }
    }
}
