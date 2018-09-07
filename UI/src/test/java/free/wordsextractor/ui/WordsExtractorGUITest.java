package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.txt_proc.dictionaries.OnlyWordsDictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.WordsStatisticsDictionary;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.cleanupStages;
import static org.testfx.api.FxToolkit.registerPrimaryStage;


public class WordsExtractorGUITest  extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(WordsExtractorGUITest.class);

    private static String paths[] = { ".translations", ".stats", ".knowns" };

    @BeforeAll
    public static void setUpAll() throws TimeoutException {
        registerPrimaryStage();

        TranslationsDictionary translations = new TranslationsDictionary();
        translations.addTranslation("one", "Translation\nof\none");
        translations.addTranslation("two", "Translation of two");
        translations.addTranslation("three", "Translation\nof three");

        OnlyWordsDictionary knowns = new OnlyWordsDictionary();
        knowns.addWord("known1");
        knowns.addWord("known2");

        WordsStatisticsDictionary stats = new WordsStatisticsDictionary();
        for(int i= 0; i < 3; ++i) { stats.addWord("three"); }
        for(int i= 0; i < 2; ++i) { stats.addWord("two"); }
        stats.addWord("one");

        try {
            translations.saveAsBinIn(paths[0]);
            stats.saveAsBinIn(paths[1]);
            knowns.saveAsBinIn(paths[2]);
        } catch (IOException e) {
            Assert.assertTrue("Can't save dictionaries objects: " + e, false);
        }
    }

    @BeforeEach
    public void before() {
        try {
            ApplicationTest.launch(WordsExtractorGUI.class, paths);
         } catch (Exception e) {
            Assert.assertTrue("Aborted by exception: " + e, false);
        }
    }

    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        cleanupStages();
    }

    @DisplayName("Cancel")
    @Test
    void testCancel() {
        int before = listWindows().size();
        Assert.assertNotNull(lookup("#vbox"));
        Assert.assertTrue(lookup("#vbox").query().isVisible());
        clickOn("#btnCancel");
        Assert.assertEquals(before - 1, listWindows().size());
    }


    @DisplayName("Undo All")
    @Test
    void undoAll() {
        TableView table = lookup("#table").queryTableView();
        Assert.assertEquals(3, table.getItems().size());
        clickOn("one");
        clickOn("two");
        clickOn("three");
        Assert.assertEquals(0, table.getItems().size());
        clickOn("#undoAllBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("#undoAllBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("one");
        clickOn("#undoAllBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("#btnCancel");
    }

    @DisplayName("Undo")
    @Test
    void undo() {
        TableView table = lookup("#table").queryTableView();
        Assert.assertEquals(3, table.getItems().size());
        clickOn("one");
        clickOn("#undoBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("#undoBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("one");
        clickOn("two");
        clickOn("three");
        Assert.assertEquals(0, table.getItems().size());
        clickOn("#undoBtn");
        Assert.assertEquals(1, table.getItems().size());
        clickOn("#undoBtn");
        Assert.assertEquals(2, table.getItems().size());
        clickOn("#undoBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("#undoBtn");
        Assert.assertEquals(3, table.getItems().size());
        clickOn("#btnCancel");
    }

    @DisplayName("Title")
    @Test
    void selectionTitle() {
        Text txt = lookup("#removingTxt").queryText();
        Assert.assertEquals("Removed: 0", txt.getText());
        TableView list = lookup("#table").queryTableView();
        clickOn("one");
        Assert.assertEquals("Removed 1 from 3", txt.getText());
        clickOn("two");
        Assert.assertEquals("Removed 2 from 3", txt.getText());
        clickOn("three");
        Assert.assertEquals("Removed 3 from 3", txt.getText());
        clickOn("#btnCancel");
    }

    private String tableViewToStr(TableView table) {
        StringBuilder builder = new StringBuilder();
        table.getItems().forEach(item -> builder.append("[" + item.toString() + "],"));
        return (builder.length() > 0) ? "{" + builder.deleteCharAt(builder.length() - 1).toString() + "}": "{}";
    }

    @DisplayName("Remove by selection")
    @Test
    void remove() {
        TableView list = lookup("#table").queryTableView();
        Assert.assertEquals(3, list.getItems().size());
        clickOn("one");
        Assert.assertEquals("{[three 3],[two 2]}", tableViewToStr(list));
        clickOn("two");
        Assert.assertEquals("{[three 3]}", tableViewToStr(list));
        clickOn("three");
        Assert.assertEquals("{}", tableViewToStr(list));
        clickOn("#undoBtn");
        Assert.assertEquals("{[three 3]}", tableViewToStr(list));
        clickOn("#undoBtn");
        Assert.assertEquals("{[three 3],[two 2]}", tableViewToStr(list));
        clickOn("#btnCancel");
    }


    @DisplayName("Translation window")
    @Test
    void translationWin() {
        Assert.assertEquals(1, listWindows().size());
        moveTo("one");
        Assert.assertEquals(2, listWindows().size());
        Label lbl = (Label) ((Popup)listWindows().get(1)).getContent().get(0);
        Assert.assertEquals("Translation\nof\none", lbl.getText());
        Assert.assertTrue("Pop up window of translation isn't at the same Y position as the parent win", listWindows().get(0).getY() == listWindows().get(1).getY());
        double expectedX = listWindows().get(0).getX() + listWindows().get(0).getWidth() + 3;
        Assert.assertTrue("Translation's pop up X position isn't at the right side of the parent win", expectedX == listWindows().get(1).getX());

        moveTo(10, 10);
        Assert.assertEquals(1, listWindows().size());
        moveTo("two");
        Assert.assertEquals(2, listWindows().size());
        lbl = (Label) ((Popup)listWindows().get(1)).getContent().get(0);
        Assert.assertEquals("Translation of two", lbl.getText());

        Assert.assertTrue("Pop up window of translation isn't at the same Y position as the parent win", listWindows().get(0).getY() == listWindows().get(1).getY());
        expectedX = listWindows().get(0).getX() + listWindows().get(0).getWidth() + 3;
        Assert.assertTrue("Translation's pop up X position isn't at the right side of the parent win", expectedX == listWindows().get(1).getX());
        clickOn("#btnCancel");
    }

    @Override
    public void start(Stage stage) {
        stage.show();
    }

}