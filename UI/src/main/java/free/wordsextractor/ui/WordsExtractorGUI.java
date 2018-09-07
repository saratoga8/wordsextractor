package free.wordsextractor.ui;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.Dictionary;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

/**
 * GUI of the application
 */
public class WordsExtractorGUI extends Application {
    private static final Logger log = LogManager.getLogger(WordsExtractorGUI.class);

    private int initialWordsNum = 0;
    private ObservableList<WordInfo> wordsList;
    final private Stack<WordInfo> deletedWords = new Stack<>();

    private TranslationPopUp translationPopUp;

    private Text text = null;
    private TableView<WordInfo> table = null;
    private VBox vBox = null;
    private ButtonBar bar = null;

    private Dimension2D stageSize = new Dimension2D(850, 550);

    private String knownWordsPath = "";

    /**
     * Initialization of app: get known words, statistics and translations from files
     * @throws IOException
     */
    @Override
    public void init() throws IOException {
        wordsList = FXCollections.observableArrayList();
        readDictionariesFromFiles(getParameters().getUnnamed());
        initialWordsNum = wordsList.size();
    }

    private void readDictionariesFromFiles(final List<String> params) throws IOException {
        try {
            final Dictionary stats = readDictionaryFromFile(params.get(1));

            if (params.size() > 2) {
                knownWordsPath = params.get(2);

            }

            final Dictionary translations = readDictionaryFromFile(params.get(0));
            translations.getWords().forEach(word -> {
                final WordInfo info = new WordInfo(word, translations.getTranslation(word), stats.getTranslation(word));
                wordsList.add(info);
            });
        } catch (ClassNotFoundException e) {
            throw new IOException("Can't initialize dictionaries: " + e);
        }
    }

    final Dictionary readDictionaryFromFile(String path) throws IOException, ClassNotFoundException {
        if (!TextUtils.isEmpty(path) && Files.exists(Paths.get(path)))
            return Dictionary.readAsBinFrom(path);
        else
            throw new IOException("Can't read dictionary from file " + path);
    }

    /**
     * Start the GUI
     * @param stage App's stage
     */
    @Override
    public void start(Stage stage) {
        stage.setMinWidth(stageSize.getWidth());
        stage.setMinHeight(stageSize.getHeight());

        stage.centerOnScreen();
        stage.setTitle("Extracted words from text");
        stage.setOnCloseRequest(handle -> {
            log.debug("on closing");
        });

        translationPopUp = new TranslationPopUp();

        vBox = new VBox();
        vBox.setSpacing(3);
        vBox.setMinWidth(stageSize.getWidth());
        vBox.setMinHeight(stageSize.getHeight());
        vBox.setId("vbox");

        initHeader(vBox);
        initTable(vBox);
        initChangeListener(stage);
        initFooter(vBox);

        final Scene scene = new Scene(new Group());
        ((Group)scene.getRoot()).getChildren().add(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void initChangeListener(final  Stage stage) {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            vBox.setPrefHeight(stage.getHeight()); vBox.setMinWidth(stage.getWidth());
            stageSize = new Dimension2D(stage.getWidth(), stage.getHeight());
            initTable(vBox);
            initFooter(vBox);

            double tbl_height = stage.getHeight() - ((HBox)vBox.getChildren().get(0)).getHeight() - bar.getHeight() - table.lookup(".column-header-background").getBoundsInLocal().getHeight();
            table.setPrefHeight(tbl_height);
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }

    /**
     * Initialization of the footer of the main win
     * @param vBox Vertical box of the app
     */
    private void initFooter(final VBox vBox) {
        if (bar == null) {
            bar = new ButtonBar();
            final Button okBtn = initOkBtn();
            final Button cancelBtn = initCancelBtn();

            ButtonBar.setButtonData(cancelBtn, ButtonBar.ButtonData.RIGHT);
            ButtonBar.setButtonData(okBtn, ButtonBar.ButtonData.LEFT);
            bar.getButtons().addAll(okBtn, cancelBtn);
            vBox.getChildren().add(bar);
        }
        bar.setMaxHeight(((Button)bar.getButtons().get(0)).getHeight());
    }

    private Button initOkBtn() {
        final Button okBtn = new Button();
        okBtn.setText("OK");
        okBtn.setMnemonicParsing(false);

        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> saveDeletedWords());
        return okBtn;
    }

    private void saveDeletedWords() {
        Dictionary knownWordsDict;
        try {
            knownWordsDict = readDictionaryFromFile(knownWordsPath);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can't read file " + knownWordsPath + " with known words: " + e);
            return;
        }
        deletedWords.forEach(wordInfo -> {
            try {
                knownWordsDict.addWord(wordInfo.getWord());
            } catch (WordsExtractorException e) {
                log.error("Can't add deleted word '" + wordInfo.getWord() + "' to the known words dictionary: " + e);
            }
        });
        try {
            knownWordsDict.saveAsBinIn(knownWordsPath);
        } catch (IOException e) {
            log.error("Can't save known words dictionary " + knownWordsPath + " after adding deleted words: " + e);
        }
    }

    private Button initCancelBtn() {
        final Button cancelBtn = new Button();
        cancelBtn.setText("Cancel");
        cancelBtn.setMnemonicParsing(false);
        cancelBtn.setCancelButton(true);
        cancelBtn.setId("btnCancel");

        cancelBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            final Node source = (Node) event.getSource();
            ((Stage) source.getScene().getWindow()).close();
        });
        return cancelBtn;
    }

    private void initHeader(final VBox vBox) {
        text = new Text();
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeWidth(0);
        text.setText("Removed: 0");
        text.setId("removingTxt");

        final TextField txtField = new TextField();
        txtField.setId("searchField");

        final HBox hBox = new HBox();
        hBox.getChildren().addAll(txtField, text);
        initUndoBtns(hBox);

        vBox.getChildren().add(hBox);
    }

    private void initCols(final TableView table) {
        final TableColumn<WordInfo, String> statsCol, wordsCol;

        if (table.getColumns() == null || table.getColumns().isEmpty()) {
            wordsCol = initCol("words", "word", "");
            statsCol = initCol("stats", "stats", "-fx-alignment: CENTER-RIGHT;");

            table.getColumns().addAll(wordsCol, statsCol);
        } else {
            wordsCol = (TableColumn<WordInfo, String>) table.getColumns().get(0);
            statsCol = (TableColumn<WordInfo, String>) table.getColumns().get(1);
        }

        double max_int_width = new Text().getFont().getSize() * Integer.toString(Integer.MAX_VALUE).length();
        wordsCol.setPrefWidth(stageSize.getWidth() - max_int_width);
        statsCol.setMaxWidth(max_int_width);
    }

    private TableColumn<WordInfo, String> initCol(String name, String propVal, String style) {
        final TableColumn<WordInfo, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(propVal));
        if (!TextUtils.isBlank(style))
            column.setStyle(style);
        column.setEditable(false);
        column.setResizable(true);
        return column;
    }

    private void initTable(final VBox vBox) {
        if (table == null) {
            table = new TableView<>();
            table.setId("table");
            vBox.getChildren().add(table);
        }

        initCols(table);

        table.setItems(wordsList);
        table.setRowFactory(tableView -> {
            TableRow<WordInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getEventType() == MouseEvent.MOUSE_CLICKED)
                    handleWordSelection(table.getSelectionModel().getSelectedItem());
            });
            row.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                final Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                showTranslationPopUp(stage, row.getItem());
            });
            row.addEventHandler(MouseEvent.MOUSE_EXITED, event -> translationPopUp.hide());
            return row;
        });
    }

    private void showTranslationPopUp(final Stage stage, final WordInfo info) {
        if (info != null) {
            translationPopUp.setTxt(info.getTranslation());
            translationPopUp.show(stage);
            translationPopUp.setX(stage.getX() + stage.getWidth() + 3);
            translationPopUp.setY(stage.getY());
        }
    }

    private void initUndoBtns(final HBox hBox) {
        final Button undoBtn = initUndoBtn("Undo", "undoBtn");
        final Button undoAllBtn = initUndoBtn("Undo All", "undoAllBtn");

        initButton(undoBtn,    MouseEvent.MOUSE_CLICKED, () -> undoHandling());
        initButton(undoAllBtn, MouseEvent.MOUSE_CLICKED, () -> undoAllHandling());

        hBox.getChildren().addAll(undoBtn, undoAllBtn);
    }

    private Button initUndoBtn(String txt, String id) {
        final Button undoBtn = new Button();
        undoBtn.setText(txt);
        undoBtn.setMnemonicParsing(false);
        undoBtn.setId(id);
        return undoBtn;
    }

    private void handleWordSelection(final WordInfo selectedInfo) {
//        WordInfo selectedInfo = wordsListView.getSelectionModel().getSelectedItem();

        if(!StringUtils.isBlank(selectedInfo.getWord())) {
            deletedWords.push(selectedInfo);
//            words.remove(selectedInfo);
            if (wordsList.remove(selectedInfo)) {
                initTable(vBox);
                text.setText("Removed " + deletedWords.size() + " from " + initialWordsNum);
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
                initTable(vBox);
                text.setText("Removed " + deletedWords.size() + " from " + wordsList.size());
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


    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
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
