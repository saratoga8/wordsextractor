package free.wordsextractor.ui;

import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.loadui.testfx.controls.impl.VisibleNodesMatcher;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static org.loadui.testfx.GuiTest.waitUntil;
import static org.testfx.api.FxToolkit.registerPrimaryStage;


public class WordsExtractorGUITest  extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(WordsExtractorGUITest.class);

    @BeforeAll
    public static void setUpAll() throws TimeoutException {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        registerPrimaryStage();
    }

    @BeforeEach
    public void before() {
        try {
            ApplicationTest.launch(TestedApp.class);
            waitUntil("#wordsListView", Matchers.is(VisibleNodesMatcher.visible()), 10);
            waitUntil("#searchField", Matchers.is(VisibleNodesMatcher.visible()), 10);
        } catch (Exception e) {
            log.error("Aborted by exception: " + e);
        }
    }

    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @DisplayName("Cancel")
    @Test
    void testCancel() {
        Assert.assertNotNull(lookup("#vbox").query());
        Assert.assertTrue(lookup("#vbox").query().isVisible());
        clickOn("#btnCancel");
        Assert.assertNull(lookup("#vbox").query());
    }

    @DisplayName("Select All")
    @Test
    void selectAll() {
        ListView list = lookup("#wordsListView").queryListView();
        Assert.assertEquals(0, list.getSelectionModel().getSelectedItems().size());
        clickOn("#selectAllBtn");
        Assert.assertEquals(3, list.getSelectionModel().getSelectedItems().size());
    }

    @DisplayName("Deselect All")
    @Test
    void deSelectAll() {
        ListView list = lookup("#wordsListView").queryListView();
        Assert.assertEquals(0, list.getSelectionModel().getSelectedItems().size());
        clickOn("#selectAllBtn");
        Assert.assertEquals(3, list.getSelectionModel().getSelectedItems().size());
        clickOn("#deselectAllBtn");
        Assert.assertEquals(0, list.getSelectionModel().getSelectedItems().size());
    }

    @DisplayName("Selection title")
    @Test
    void selectionTitle() {
        Text txt = lookup("#selectedTxt").queryText();
        Assert.assertEquals("Selected: 0", txt.getText());
        ListView list = lookup("#wordsListView").queryListView();
        clickOn("one");
        Assert.assertEquals("Selected 1 from 3", txt.getText());
        clickOn("two");
        Assert.assertEquals("Selected 2 from 3", txt.getText());
        clickOn("three");
        Assert.assertEquals("Selected 3 from 3", txt.getText());
        clickOn("one");
        Assert.assertEquals("Selected 2 from 3", txt.getText());
        clickOn("two");
        Assert.assertEquals("Selected 1 from 3", txt.getText());
        clickOn("three");
        Assert.assertEquals("Selected 0 from 3", txt.getText());
        clickOn("#selectAllBtn");
        Assert.assertEquals("Selected 3 from 3", txt.getText());
        clickOn("#deselectAllBtn");
        Assert.assertEquals("Selected 1 from 3", txt.getText());
    }

    @Override
    public void start(Stage stage) {
        stage.show();
    }

}