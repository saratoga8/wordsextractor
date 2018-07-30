package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.WordsStatisticsDictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.function.Supplier;

public class ListWordsViewController implements Initializable {
    private static final Logger log = LogManager.getLogger(ListWordsViewController.class);

    @FXML
    private Text removingTxt;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<WordInfo> wordsListView;
    private int initialWordsNum = 0;
    private ObservableList<WordInfo> wordsList;
    final private Stack<WordInfo> deletedWords = new Stack<>();

    @FXML
    private Button btnOK, btnCancel;
    @FXML
    private Button undoBtn, undoAllBtn;

//    final Hashtable<String, WordInfo> words;

    public ListWordsViewController(final TranslationsDictionary translations, final WordsStatisticsDictionary stats) {
        wordsList = FXCollections.observableArrayList();

//        words = new Hashtable<>();
        translations.getWords().forEach(word -> {
            final WordInfo info = new WordInfo(word, translations.getTranslation(word), stats.getTranslation(word));
//            words.put(word, info);
            wordsList.add(info);
        });
    }


    private TranslationPopUp translationPopUp;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initList();
        initBtns();

        translationPopUp = new TranslationPopUp();
//        initialWordsNum = words.size();
    }

    private void initList() {
        wordsListView.setItems(wordsList);
        wordsListView.setCellFactory(param -> {
            final WordCellController cell = new WordCellController();
            cell.setOnMouseClicked(event -> {
                if (event.getEventType() == MouseEvent.MOUSE_CLICKED)
                    handleWordSelection();
            });
            cell.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                final Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                showTranslationPopUp(stage, cell.getItem());
            });
            cell.addEventHandler(MouseEvent.MOUSE_EXITED, event -> translationPopUp.hide());
            return cell;
        });
    }

    private void showTranslationPopUp(Stage stage, WordInfo info) {
        if (info != null) {
            translationPopUp.setTxt(info.getTranslation());
            translationPopUp.show(stage);
            translationPopUp.setX(stage.getX() + stage.getWidth() + 3);
            translationPopUp.setY(stage.getY());
        }
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
        WordInfo selectedInfo = wordsListView.getSelectionModel().getSelectedItem();
        if(!StringUtils.isBlank(selectedInfo.getWord())) {
            deletedWords.push(selectedInfo);
//            words.remove(selectedInfo);
            if (wordsList.remove(selectedInfo)) {
                initList();
                removingTxt.setText("Removed " + deletedWords.size() + " from " + initialWordsNum);
            }
            else
                log.error("Can't remove '" + selectedInfo.getWord() + "' from words list");
        }
        else
            log.warn("Selected word is blank or NULL");
    }

    private Void undoHandling() {
        if (!deletedWords.isEmpty()) {
            WordInfo deleted = deletedWords.pop();
//            words.put(deleted.getWord(), deleted);
            if (wordsList.add(deleted)) {
                initList();
                removingTxt.setText("Selected " + deletedWords.size() + " from " + wordsList.size());
            }
            else
                log.error("Can't add '" + deleted.getWord() + "' from words list");
        }
        return null;
    }

    private Void undoAllHandling() {
        while(!deletedWords.isEmpty()) {
            undoHandling();
        }
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
