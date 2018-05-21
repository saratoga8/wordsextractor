package free.wordsextractor.ui;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.loadui.testfx.controls.impl.VisibleNodesMatcher;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static org.loadui.testfx.GuiTest.waitUntil;
import static org.testfx.api.FxToolkit.registerPrimaryStage;


public class WordsExtractorGUITest  extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(WordsExtractorGUITest.class);

    private Button cancelBtn;


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

    @Test
    void testCancel() {
        Assert.assertNotNull(lookup("#vbox").query());
        Assert.assertTrue(lookup("#vbox").query().isVisible());
        clickOn("#btnCancel");
        Assert.assertNull(lookup("#vbox").query());
    }

    @Test
    void selectAll() {
        ListView list = lookup("#wordsListView").queryListView();
        Assert.assertEquals("[one, two, three]", list.getItems().toString());
    }

    @Override
    public void start(Stage stage) {
        stage.show();
    }

}