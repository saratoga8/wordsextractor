package free.wordsextractor.ui;

import free.wordsextractor.bl.extraction.txt_proc.dictionaries.OnlyWordsDictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.TranslationsDictionary;
import free.wordsextractor.bl.extraction.txt_proc.dictionaries.WordsStatisticsDictionary;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.cleanupStages;
import static org.testfx.api.FxToolkit.registerPrimaryStage;

public class GuiTest2 extends ApplicationTest {
    private static final Logger log = LogManager.getLogger(GuiTest.class);

    private static String paths[] = { ".translations", ".stats", ".knowns" };

    private static Map<String, Integer> words = Map.of("one", 1, "two", 2, "three", 3, "ten", 10, "eleven", 11, "forty-five", 45);

    @BeforeAll
    public static void setUpAll() throws TimeoutException {
        registerPrimaryStage();

        try {
            TranslationsDictionary translations = new TranslationsDictionary();
            words.keySet().forEach(word -> translations.addTranslation(word, "Translation\nof\n" + word));

            WordsStatisticsDictionary stats = new WordsStatisticsDictionary();
            words.keySet().forEach(word -> { for (int i = 0; i < words.get(word); i++) stats.addWord(word); });

            OnlyWordsDictionary knowns = new OnlyWordsDictionary();
            knowns.addWord("known1");
            knowns.addWord("known2");

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

    @DisplayName("Sorting by stats")
    @Test
    void statsSorting() {
        TableView list = lookup("#table").queryTableView();
        Assert.assertEquals(words.size(), list.getItems().size());
        clickOn("stats");
        Assert.assertEquals("{[one 1],[two 2],[three 3],[ten 10],[eleven 11],[forty-five 45]}", GuiTest.tableViewToStr(list));
        clickOn("stats");
        Assert.assertEquals("{[forty-five 45],[eleven 11],[ten 10],[three 3],[two 2],[one 1]}", GuiTest.tableViewToStr(list));
        clickOn("words");
        clickOn("stats");
        Assert.assertEquals("{[one 1],[two 2],[three 3],[ten 10],[eleven 11],[forty-five 45]}", GuiTest.tableViewToStr(list));
        clickOn("ten");
        clickOn("one");
        clickOn("stats");
        Assert.assertEquals("{[forty-five 45],[eleven 11],[three 3],[two 2]}", GuiTest.tableViewToStr(list));
        clickOn("#undoBtn");
        clickOn("stats");
        Assert.assertEquals("{[forty-five 45],[eleven 11],[three 3],[two 2],[one 1]}", GuiTest.tableViewToStr(list));
        clickOn("#undoBtn");
        clickOn("stats");
        Assert.assertEquals("{[one 1],[two 2],[three 3],[ten 10],[eleven 11],[forty-five 45]}", GuiTest.tableViewToStr(list));
        clickOn("ten");
        clickOn("one");
        clickOn("stats");
        clickOn("#undoAllBtn");
        Assert.assertEquals("{[forty-five 45],[eleven 11],[ten 10],[three 3],[two 2],[one 1]}", GuiTest.tableViewToStr(list));
    }

    @AfterEach
    void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        cleanupStages();
    }
}
