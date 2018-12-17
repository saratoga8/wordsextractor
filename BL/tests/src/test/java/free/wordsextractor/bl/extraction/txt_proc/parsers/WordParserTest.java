package free.wordsextractor.bl.extraction.txt_proc.parsers;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.translation.Translation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class WordParserTest {
    private WordParser parser;

    @BeforeEach
    void init() {
        try {
            parser = new WordParser(Translation.Langs.ENG);
        } catch (WordsExtractorException e) {
            Assert.assertTrue("The test aborted by the exception: " + e, false);
        }
    }

    @DisplayName("Parse word without suffixes/prefixes")
    @ParameterizedTest
    @ValueSource(strings = {"word"})
    void parse(String word) {
        Assert.assertEquals("The word '" + word + " shouldn't contain any suffixes/prefixes", List.of(word), parser.parse(word));
    }

    @DisplayName("Parse word with suffix")
    @ParameterizedTest
    @CsvSource({"ed, word"})
    void parseSuff(String suf, String expected) {
        var words = parser.parse(expected + suf);
        Assert.assertEquals("[" + expected + suf + ", " + expected + "]", words.toString());
    }

    @DisplayName("Parse word with complex suffix")
    @ParameterizedTest
    @CsvSource({"ied, word, worded"})
    void parseComplexSuff(String suf, String expected1, String expected2) {
        var words = parser.parse(expected1 + suf);
        Assert.assertEquals("[" + expected1 + suf + ", " + expected2 + ", " + expected1 + "]", words.toString());
    }

    @DisplayName("Parse word with prefix")
    @ParameterizedTest
    @CsvSource({"en, word"})
    void parsePref(String pref, String expected) {
        var words = parser.parse(pref + expected);
        Assert.assertEquals("[" + pref + expected + ", " + expected + "]", words.toString());
    }

    @DisplayName("Parse word with complex prefix")
    @ParameterizedTest
    @CsvSource({"under, define, undefine"})
    void parseComplexPref(String pref, String expected1, String expected2) {
        var words = parser.parse(pref + expected1);
        Assert.assertEquals("[" + pref + expected1 + ", " + expected1 + ", " + expected2 + "]", words.toString());
    }


    @DisplayName("Parse word with prefix and suffix")
    @ParameterizedTest
    @CsvSource({"en, word, ed"})
    void parseBoth(String pref, String root, String suf) {
        var words = parser.parse(pref + root + suf);
        Assert.assertEquals("[" + pref + root + suf + ", " + root + suf + ", " + pref + root + ", " + root + "]", words.toString());
    }
}