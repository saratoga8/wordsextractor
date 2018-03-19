package free.wordsextractor.bl.translation;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class GoogleTranslationTest {

    @Test
    public void googleTranslate() {
        String translation = new GoogleTranslation(Translation.Langs.ENG, Translation.Langs.RUS).translate("slut");
        Assert.assertEquals("", translation);
    }
}