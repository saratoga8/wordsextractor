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
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.function.Supplier;

public class ListWordsViewController {
    private static final Logger log = LogManager.getLogger(ListWordsViewController.class);

    @FXML
    private Text selectedTxt;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> wordsListView;
    private ObservableList<String> wordsList;
    final private HashSet<String> selectedIndices = new HashSet<>();

    @FXML
    private Button btnOK, btnCancel;
    @FXML
    private Button selectAllBtn;
    @FXML
    private Button deselectAllBtn;

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
    }

    private void initList() {
        wordsList = FXCollections.observableList(dict.getSortedTranslations());
        wordsListView.setItems(wordsList);
        wordsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        wordsListView.setCellFactory(param -> {
            final WordCell cell = new WordCell();
            cell.setOnMouseClicked(event -> {
                if (event.getEventType() == MouseEvent.MOUSE_CLICKED)
                    handleWordSelection();
            });
            cell.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                final Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                String word = cell.getItem();
                if (!TextUtils.isBlank(word)) {
                    log.debug("Translation: " + dict.getTranslation(word));
                    translationPopUp.setTxt(dict.getTranslation(word));
                    translationPopUp.show(stage);
                }
            });
            cell.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                translationPopUp.hide();
            });
            return cell;
        });
    }

    private void initBtns() {
        initButton(selectAllBtn,   MouseEvent.MOUSE_CLICKED, () -> selectAllHandling());
        initButton(deselectAllBtn, MouseEvent.MOUSE_CLICKED, () -> deselectAllHandling());

        btnCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            final Node source = (Node) event.getSource();
            ((Stage) source.getScene().getWindow()).close();
            log.debug("canceling");
        });
    }

    private void handleWordSelection() {
        String selectedWord = wordsListView.getSelectionModel().getSelectedItem();
        if(!StringUtils.isBlank(selectedWord)) {
            if (selectedIndices.contains(selectedWord))
                selectedIndices.remove(selectedWord);
            else
                selectedIndices.add(selectedWord);
            wordsListView.getSelectionModel().clearSelection();
            selectedIndices.parallelStream().forEach(wordsListView.getSelectionModel()::select);

            selectedTxt.setText("Selected " + selectedIndices.size() + " from " + wordsList.size());
        }
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


    private Void selectAllHandling() {
        wordsListView.getSelectionModel().selectAll();
        selectedIndices.clear();
        wordsListView.getItems().forEach(selectedIndices::add);
        selectedTxt.setText("Selected " + selectedIndices.size() + " from " + wordsList.size());
        return null;
    }

    private Void deselectAllHandling() {
        wordsListView.getSelectionModel().clearSelection();
        selectedIndices.clear();
        selectedTxt.setText("Selected " + selectedIndices.size() + " from " + wordsList.size());
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
