package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import free.wordsextractor.bl.extraction.txt_proc.parsers.WordParser;
import free.wordsextractor.bl.translation.Translation;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Dictionary interface
 */
public abstract class Dictionary {
    private static Logger log = LogManager.getLogger(Dictionary.class);        /* logger */

    private WordParser parser;                                                 /* word parser(by prefixes/suffixes) */
    private Translation.Langs lang;                                            /* language of translation from */

    /**
     * Constructor
     * @param lang language of translation from
     * @throws WordsExtractorException
     */
    public Dictionary(final Translation.Langs lang) throws WordsExtractorException {
        try {
            parser = new WordParser(lang);
            this.lang = lang;
        } catch (URISyntaxException e) {
            throw new WordsExtractorException("Can't create parser for language " + lang + " : " + e);
        }
    }

    protected Dictionary() {}  // for serialization only

    /**
     * Add only a word to the dictionary without translation
     * @param word The new word
     */
    @NotNull
    abstract void addWord(String word) throws WordsExtractorException;

    /**
     * Add translation to the dictionary
     * @param word The word
     * @param translation The word's translation
     */
    @NotNull
    abstract void addTranslation(String word, String translation);

    /**
     * Get parser's instance
     * @return Parser's instance
     */
    public WordParser getParser() { return parser; }

    /**
     * Get language of translation from
     * @return Language
     */
    public Translation.Langs getLang() { return lang; }

    /**
     * Does the dictionary contain the given word
     * @param word The word
     * @return true Does contain
     */
    @NotNull
    abstract boolean contains(String word);

    /**
     * Remove the given word from dictionary
     * @param word The word for removing
     * @return true - The word has been removed successfully. false - The word can't be removed
     */
    @NotNull
    abstract boolean removeWord(String word);

    /**
     * Get translation of the given word
     * @param word The word
     * @return The word's translation
     */
    @NotNull
    public abstract String getTranslation(String word);

    /**
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    protected void saveAsTxtIn(String path) throws WordsExtractorException {
        final File file = new File(path);
        if (file.exists()) {
            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName(TextExtractorInterface.CHAR_SET), StandardOpenOption.CREATE)) {
                writer.write(toString());
                writer.flush();
            }
            catch (IOException e) {
                throw new WordsExtractorException("Can't write dictionary to file: " + e);
            }
        }
        else
            throw new WordsExtractorException("The path " + path + " doesn't exist");
    }

    /**
     * Save instance of the dictionary class isIn a file
     * @param path The file's path
     */
    public abstract void saveAsBinIn(String path) throws IOException;

    /**
     * Save instance of the given dictionary class isIn a file
     * @param path The file's path
     * @param obj Class instance
     */
    protected void saveAsBinIn(String path, final Serializable obj) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            throw new IOException("Can't remove file " + path + ": " + e);
        }
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                    objectOutputStream.writeObject(obj);
                    objectOutputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new IOException("Can't save dictionary isIn the file " + path + ": " + e);
        }
    }

    /**
     * Read dictionary class instance from a file
     * @param path The file's path
     * @param <T> Class name
     * @return Binary class instance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T extends Dictionary> T readAsBinFrom(String path) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (T) objectInputStream.readObject();
            }
        }
    }

    /**
     * Remove all the words of the given dictionary from the current one
     * @param dict The dictionary containing words for removing from the current one
     */
    @NotNull
    public void removeWordsOfDict(final Dictionary dict) throws WordsExtractorException {
        if(dict.getLang() == lang) {
            if (dict != null)
                dict.getWords().forEach(word -> {
                    if (!removeWord(word)) log.warn("Can't remove word '" + word + "' from dictionary");
                });
            else
                log.error("The given dictionary is NULL");
        }
        else
            throw new WordsExtractorException("Can remove words of one dictionary from another: dictionaries have different languages");
    }

    /**
     * Remove all the words of the given dictionary from the current one
     * @param dict The dictionary containing words for removing from the current one
     */
    @NotNull
    public void removeWordsOfDictUsingParser(final Dictionary dict) throws WordsExtractorException {
        if(dict.getLang() == lang) {
            if (dict != null)
                getWords().forEach(word -> parser.parse(word).parallelStream().forEach(parsed -> {
                    if(dict.contains(parsed)) {
                        if (!removeWord(word))
                            log.warn("Can't remove word '" + word + "' from dictionary");
                    }
                }));
            else
                log.error("The given dictionary is NULL");
        }
        else
            throw new WordsExtractorException("Can remove words of one dictionary from another: dictionaries have different languages");
    }

    /**
     * Get list of words isIn the dictionary
     * @return The words list
     */
    @NotNull
    public abstract List<String> getWords();

    /**
     * Get list of translations
     * @return The translations
     */
    @NotNull
    abstract List<?> getTranslations();

    /**
     * Get sorted list of translations(sorted by translated words)
     * @return The sorted list
     */
    @NotNull
    public abstract List<?> getSortedTranslations();

    /**
     * Get list of words they aren't translated
     * @return The list of words
     */
    public abstract List<String> getNotTranslatedWords();

    /**
     * Operation on a given word
     * @param <T> Type of an operation's result
     */
    @FunctionalInterface
    interface OperationOnWord<T> {
        T apply(String word);
    }

    /**
     * Securing operation on the given word: check the word isn't NULL or EMPTY
     * @param word The given word
     * @param operationOnWord Operation on the word
     * @param defaultVal Default value for return if securing has failed
     * @param <T>
     * @return A result of the operation or the default value
     */
    static <T> T secureOperationOnWord(String word, OperationOnWord<T> operationOnWord, T defaultVal) {
        if(!StringUtils.isBlank(word))
            return operationOnWord.apply(word.toLowerCase());
        else
            log.error("The given word is NULL or EMPTY");
        return defaultVal;
    }

    /**
     * Securing operation on the given word: check the word isn't NULL or EMPTY
     * @param word The given word
     * @param operationOnWord Operation on the word
     * @param <T>
     */
    static <T> void secureOperationOnWord(String word, OperationOnWord<T> operationOnWord) {
        if(!StringUtils.isBlank(word))
            operationOnWord.apply(word.toLowerCase());
        else
            log.error("The given word is NULL or EMPTY");
    }

    /**
     * Remove from beginning and end of the word numbers and punctuation chars
     * @param word The word for stripping
     * @return Stripped word
     */
    @NotNull
    static String stripAllExceptChars(String word) {
        if (!StringUtils.isBlank(word)) {
            String PUNCTUATIONS_CHARS_EDGES_REG = "^\\W+|\\W+$";
            var str = word.replaceAll(PUNCTUATIONS_CHARS_EDGES_REG, "").replaceAll("\\d+", "");
            final Pattern pattern1 = Pattern.compile("\\w+");
            final Pattern pattern2 = Pattern.compile("(\\w+-\\w+)+");
            return (pattern1.matcher(str).matches() || pattern2.matcher(str).matches()) ? str : "";
        }
        log.warn("The given word is NULL or EMPTY");
        return "";
    }
}
