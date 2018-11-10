package free.wordsextractor.bl.extraction.txt_proc.parsers;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.translation.Translation;
import org.apache.http.util.TextUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

public class WordParser {
    public static final Map<Translation.Langs, String> parsersFiles = Map.of(Translation.Langs.ENG, "en.xml");
    public static String PARSERS_DIR = "parsers";

    final private List<WordPart> suffixes;
    final private List<WordPart> prefixes;


    public WordParser(final Translation.Langs lang) throws WordsExtractorException, URISyntaxException {
        String fileName = parsersFiles.get(lang);
        if (TextUtils.isBlank(fileName))
            throw new WordsExtractorException("There is no parser file for the language " + lang.getVal());

        var path = FileManager.getResourcesFilePath(PARSERS_DIR + File.pathSeparator + fileName, this);

        suffixes = loadWordParts(path, Suffix.NAME, str -> {
            try {
                return new Suffix(str);
            } catch (WordsExtractorException e) {
                e.printStackTrace();
            }
        });
        prefixes = loadWordParts(path, Prefix.NAME);
    }

    public List<String> parse(String word) {
        var words = new LinkedList<>();
        if (!suffixes.isEmpty()) {
            suffixes.forEach(suf -> prefixes.forEach(pref -> words.add(pref.toString() + word + suf.toString())));
            prefixes.forEach(pref -> words.add(pref.toString() + word));
        }
        if (!prefixes.isEmpty()) {
            prefixes.forEach(suf -> suffixes.forEach(pref -> words.add(pref.toString() + word + suf.toString())));
            suffixes.forEach(suf -> words.add(word + suf.toString()));
        }
    }

    private List<WordPart> getWordPartsIn(String word, List<WordPart> parts) {
        var found = new LinkedList<WordPart>();
        parts.forEach(part -> { if (part.in(word)) found.add(part); } );
        return found;
    }

    private List<WordPart> loadWordParts(Path path, String wordPartName, Function<String, WordPart> buildWordPart) {
        ResourceBundle rb = ResourceBundle.getBundle(path.toString());
        String[] txt = rb.getStringArray(wordPartName);
        var wordParts = new LinkedList<WordPart>();
        if(txt.length != 0) {
            for (String str: txt)
                wordParts.add(buildWordPart.apply(str));
        }
        return wordParts;
    }
}
