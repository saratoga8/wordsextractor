package free.wordsextractor.bl.translation;

import free.wordsextractor.bl.WordsExtractorException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.LinkedList;

class TranslationTest {

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Translate words list")
    @ParameterizedTest
    @ValueSource(strings = {"one two three"})
    void translateWords(String words) {
        try {
            Translation translation = Mockito.mock(Translation.class, Mockito.withSettings().useConstructor(Translation.Langs.ENG, Translation.Langs.RUS));
            String arr[] = words.split(" ");
            for (int i = 0; i < arr.length; i++) {
                Mockito.when(translation.translate(arr[i])).thenReturn("translation" + (i + 1));
            }
            Mockito.doCallRealMethod().when(translation).translate(Arrays.asList(arr));
            Assert.assertEquals("[translation1, translation3, translation2]", translation.translate(Arrays.asList(arr)).getSortedTranslations().toString());
            Assert.assertEquals("[two, one, three]", translation.translate(Arrays.asList(arr)).getWords().toString());
        }
        catch (WordsExtractorException e) {
            Assert.fail("Test has failed because of exception: " + e);
        }
    }

    @DisplayName("Translate empty words list")
    @Test
    void translateEmpty() {
        try {
            Translation translation = Mockito.mock(Translation.class, Mockito.withSettings().useConstructor(Translation.Langs.ENG, Translation.Langs.RUS));
            Mockito.doCallRealMethod().when(translation).translate(new LinkedList<>());
            Assert.assertTrue(translation.translate(new LinkedList<>()).getSortedTranslations().isEmpty());
            Assert.assertTrue(translation.translate(new LinkedList<>()).getWords().isEmpty());
        }
        catch (WordsExtractorException e) {
            Assert.fail("Test has failed because of exception: " + e);
        }
    }
}