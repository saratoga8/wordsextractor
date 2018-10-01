package free.wordsextractor.ui;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import javafx.geometry.VerticalDirection;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.registerPrimaryStage;


public class E2ETest extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(E2ETest.class);
    private static String KNOWNS_FILE_NAME = "knowns.dict";

    private String txtPath, apiKeyPath;

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

    @BeforeEach
    public void setUp() throws WordsExtractorException, URISyntaxException {
         txtPath = FileManager.getResourcesFilePath("eng.txt", this).toString();
         apiKeyPath = FileManager.getResourcesFilePath("yandex_api.key", this).toString();
    }

    @DisplayName("With known words")
    @Test
    void withKnowns() {
        try {
            String knownWordsPath = writeLinesToFile(KNOWNS_FILE_NAME, Arrays.asList("to", "a", "we"));

            String paths[] = Main.translate(new String[] {txtPath, apiKeyPath, knownWordsPath});

            launch(WordsExtractorGUI.class, paths);

            TableView table = lookup("#table").queryTableView();
            int before = listWindows().size();
            String expected = "[add 1, an 1, and 1, annotation 2, array 1, by 3, can 1, configure 2, csvsource 1, data 4, few 1, follow 1, have 3, if 1, is 1, method 3, methods 1, multiple 1, objects 1, of 1, one 1, only 1, or 1, our 3, parameterized 1, parameters 1, pass 1, provided 1, rules 1, specify 1, string 1, test 8, the 3, these 1, this 1, used 1, using 2, when 2]";
            Assert.assertEquals(expected, table.getItems().sorted().toString());

            clickOn("words");
            clickOn("add");
            clickOn("array");
            clickOn("can");
            scroll(30, VerticalDirection.DOWN);
            clickOn("using");
            clickOn("used");
            clickOn("#undoBtn");
            moveTo("this");
            scroll(30, VerticalDirection.UP);
            clickOn("configure");

            clickOn("#btnOk");
            clickOn("#btnCancel");
            Assert.assertEquals(before - 1, listWindows().size());

            String content = new String(Files.readAllBytes(Paths.get(knownWordsPath)));
            expected = "to\na\nwe\nadd\narray\ncan\nusing\nconfigure\n";
            Assert.assertEquals(expected, content);

        } catch (Exception e) {
            Assert.assertTrue("Test aborted because of: " + e, false);
        }
    }

    @DisplayName("Without known words")
    @Test
    void withoutKnowns() {
        try {
            String paths[] = Main.translate(new String[] {txtPath, apiKeyPath});
            launch(WordsExtractorGUI.class, paths);

            TableView table = lookup("#table").queryTableView();
            String expected = "[a 2, add 1, an 1, and 1, annotation 2, array 1, by 3, can 1, configure 2, csvsource 1, data 4, few 1, follow 1, have 3, if 1, is 1, method 3, methods 1, multiple 1, objects 1, of 1, one 1, only 1, or 1, our 3, parameterized 1, parameters 1, pass 1, provided 1, rules 1, specify 1, string 1, test 8, the 3, these 1, this 1, to 5, used 1, using 2, we 6, when 2]";
            Assert.assertEquals(expected, table.getItems().sorted().toString());
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