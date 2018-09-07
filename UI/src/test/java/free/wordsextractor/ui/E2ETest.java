package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.file_proc.FileManager;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.TableViewMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.registerPrimaryStage;


public class E2ETest extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(E2ETest.class);
    private static String KNOWNS_FILE_NAME = "knowns.dict";

    @BeforeAll
    public static void setUpAll() throws TimeoutException {
         registerPrimaryStage();
    }

    private String writeLinesToFile(String fileName, List<String> lines) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
        File file = new File(fileName);
        FileUtils.writeLines(file, lines, "\n");
        return file.getAbsolutePath();
    }

    @Test
    void withKnowns() {
        try {
            String knownWordsPath = writeLinesToFile(KNOWNS_FILE_NAME, Arrays.asList("to", "a", "we"));

            String txtPath = FileManager.getResourcesFilePath("eng.txt", this).toString();
            String apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this).toString();

            String paths[] = Main.translate(new String[] {txtPath, apiKeyPath, knownWordsPath});
            launch(WordsExtractorGUI.class, paths);
            TableView table = lookup("#table").queryTableView();
            FxAssert.verifyThat(table, TableViewMatchers.hasNumRows(38));
            FxAssert.verifyThat(table, TableViewMatchers.hasTableCell("follow"));
            FxAssert.verifyThat(table, Matchers.not(TableViewMatchers.hasTableCell("to")));
            FxAssert.verifyThat(table, Matchers.not(TableViewMatchers.hasTableCell("a")));
            FxAssert.verifyThat(table, Matchers.not(TableViewMatchers.hasTableCell("we")));
            log.debug(table.getItems().get(0).toString());
            log.debug(table.getItems().get(0).getClass().getName());
        } catch (Exception e) {
            Assert.assertTrue("Test aborted because of: " + e, false);
        }
    }

    @DisplayName("Without known words")
    @Test
    void withoutKnowns() {
        try {
            String txtPath = FileManager.getResourcesFilePath("eng.txt", this).toString();
            String apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this).toString();

            String paths[] = Main.translate(new String[] {txtPath, apiKeyPath});
            launch(WordsExtractorGUI.class, paths);

            TableView table = lookup("#table").queryTableView();
            FxAssert.verifyThat(table, TableViewMatchers.hasNumRows(41));
            FxAssert.verifyThat(table, TableViewMatchers.hasTableCell("follow"));
            FxAssert.verifyThat(table, TableViewMatchers.hasTableCell("to"));
            FxAssert.verifyThat(table, TableViewMatchers.hasTableCell("a"));
            FxAssert.verifyThat(table, TableViewMatchers.hasTableCell("we"));
        } catch (Exception e) {
            Assert.assertTrue("Test aborted because of: " + e, false);
        }
    }

    @AfterEach
    void tearDown() throws TimeoutException, IOException {
        Files.deleteIfExists(Paths.get(KNOWNS_FILE_NAME));
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}