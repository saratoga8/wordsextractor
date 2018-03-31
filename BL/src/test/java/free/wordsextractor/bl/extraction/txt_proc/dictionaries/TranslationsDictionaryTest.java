package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created by saratoga on 31/03/18.
 */
class TranslationsDictionaryTest {
    @DisplayName("Get sorted translations")
    @Test
    void getSortedTranslation() {
        TranslationsDictionary dict = new TranslationsDictionary();
        dict.addTranslation("ghi", "translation3");
        dict.addTranslation("def", "translation2");
        dict.addTranslation("abc", "translation1");

        Assert.assertEquals("[translation1, translation2, translation3]", dict.getSortedTranslations().toString());
    }

}