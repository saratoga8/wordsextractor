package free.wordsextractor.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TestedApp extends WordsExtractorGUI {
    private static final Logger log = LogManager.getLogger(TestedApp.class);

    @Override
    public void init() {
        try {
            dict.addTranslation("one", "Number 1");
            dict.addTranslation("two", "Number 2");
            dict.addTranslation("three", "Number 3");
            super.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
