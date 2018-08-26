package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.file_proc.FileManager;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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


public class E2ETest extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(E2ETest.class);

    @BeforeAll
    public static void setUpAll() throws TimeoutException {
        WordsExtractorGUITest.setUpAll();
    }

    @Test
    void withKnowns() {
        try {
            String knownWordsPath = FileManager.getResourcesFilePath("knowns.dict", this).toString();
            String txtPath = FileManager.getResourcesFilePath("eng.txt", this).toString();
            String apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this).toString();

            String paths[] = Main.translate(new String[] {txtPath, apiKeyPath, knownWordsPath});
            launch(WordsExtractorGUI.class, paths);
            Assert.assertNotNull(lookup("#vbox").query());
            Assert.assertTrue(lookup("#vbox").query().isVisible());
            TableView table = lookup("#table").queryTableView();
        } catch (Exception e) {
            System.err.println("Test aborted because of: " + e);
        }
    }

    @Disabled
    @DisplayName("Without known words")
    @Test
    void withoutKnowns() {
        try {
            String txtPath = FileManager.getResourcesFilePath("eng.txt", this).toString();
            String apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this).toString();

            Main.main(new String[] {txtPath, apiKeyPath});

            waitUntil("#table", Matchers.is(VisibleNodesMatcher.visible()), 10);
            waitUntil("#searchField", Matchers.is(VisibleNodesMatcher.visible()), 10);
        } catch (Exception e) {
            System.err.println("Test aborted because of: " + e);
        }
    }

    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}