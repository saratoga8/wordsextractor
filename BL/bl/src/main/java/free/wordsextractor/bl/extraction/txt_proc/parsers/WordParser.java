package free.wordsextractor.bl.extraction.txt_proc.parsers;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.translation.Translation;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

public class WordParser {
    private static Logger log = LogManager.getLogger(WordParser.class);        /* logger */
    public static final Map<Translation.Langs, String> parsersFiles = Map.of(Translation.Langs.ENG, "en.property");
    public static String PARSERS_DIR = "parsers";

    final private List<WordPart> suffixes;
    final private List<WordPart> prefixes;


    public WordParser(final Translation.Langs lang) throws WordsExtractorException {
        String fileName = parsersFiles.get(lang);
        if (TextUtils.isBlank(fileName))
            throw new WordsExtractorException("There is no parser file for the language " + lang.getVal());

        var path = PARSERS_DIR + File.separator + fileName;

        // TODO Should be improved
        suffixes = loadWordParts(path, Suffix.NAME + "es", str -> {
            try {
                return new Suffix(str);
            } catch (WordsExtractorException e) {
                log.error("Can't initialize suffix: " + e);
                return null;
            }
        });
        prefixes = loadWordParts(path, Prefix.NAME + "es", str -> {
            try {
                return new Prefix(str);
            } catch (WordsExtractorException e) {
                log.error("Can't initialize prefix: " + e);
                return null;
            }
        });
        suffixes.add(new Suffix(""));
        prefixes.add(new Prefix(""));
    }

    public List<String> parse(String word) {
        var words = new HashSet<String>();

        var foundSuffixes = getWordPartsIn(word, suffixes);
        var foundPrefixes = getWordPartsIn(word, prefixes);

        String root = getRootOf(word, foundPrefixes, foundSuffixes);

        if (!foundSuffixes.isEmpty()) {
            foundSuffixes.forEach(suf -> foundPrefixes.forEach(pref -> words.add(pref.toString() + root + suf.toString())));
        }
        if (!foundPrefixes.isEmpty()) {
            foundPrefixes.forEach(pref -> foundSuffixes.forEach(suf -> words.add(pref.toString() + root + suf.toString())));
        }
        words.remove(null);

        log.debug("Parsed variants: " + words.toString() + " for the word '" + word + "'");
        return new ArrayList<>(words);
    }

    private String getRootOf(String word, final List<WordPart> prefs, final List<WordPart> suffs) {
        prefs.sort(Comparator.comparing(part -> part.toString().length()));
        suffs.sort(Comparator.comparing(part -> part.toString().length()));

        String withoutPref = StringUtils.removeStart(word, prefs.get(prefs.size() - 1).toString());
        return StringUtils.removeEnd(withoutPref, suffs.get(suffs.size() - 1).toString());
    }

    private List<WordPart> getWordPartsIn(String word, List<WordPart> parts) {
        var found = new LinkedList<WordPart>();
        parts.forEach(part -> { if (part.isIn(word)) found.add(part); } );
        return found;
    }

    private List<WordPart> loadWordParts(String path, String wordPartName, Function<String, WordPart> buildWordPart) throws WordsExtractorException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream != null) {
            Properties props = new Properties();
            try {
                props.load(stream);
            } catch (IOException e) {
                throw new WordsExtractorException("Can't load properties file " + path + ": " + e);
            }
            String found = props.getProperty(wordPartName);
            if (found != null) {
                String[] txt = found.split(",");
                var wordParts = new LinkedList<WordPart>();
                if(txt.length != 0) {
                    for (String str: txt) {
                        var part = buildWordPart.apply(str);
                        if (part != null)
                            wordParts.add(part);
                        else
                            throw new WordsExtractorException("Can't add word part " + wordPartName + " when it's null");
                    }
                }
                return wordParts;
            }
            else
                throw new WordsExtractorException("The property name '" + wordPartName + "' hasn't found in the file " + path);
        }
        else
            throw new WordsExtractorException("The properties file " + path + " hasn't found");
    }
}
