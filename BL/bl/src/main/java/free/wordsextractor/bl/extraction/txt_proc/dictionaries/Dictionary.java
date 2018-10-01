package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.extractors.TextExtractorInterface;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Dictionary interface
 */
public interface Dictionary {
    Logger log = LogManager.getLogger(Dictionary.class);        /* logger */

    /**
     * Add only a word to the dictionary without translation
     * @param word The new word
     */
    @NotNull
    void addWord(String word) throws WordsExtractorException;

    /**
     * Add translation to the dictionary
     * @param word The word
     * @param translation The word's translation
     */
    @NotNull
    void addTranslation(String word, String translation);


    @NotNull
    boolean contains(String word);

    /**
     * Remove the given word from dictionary
     * @param word The word for removing
     * @return true - The word has been removed successfully. false - The word can't be removed
     */
    @NotNull
    boolean removeWord(String word);

    /**
     * Get translation of the given word
     * @param word The word
     * @return The word's translation
     */
    @NotNull
    String getTranslation(String word);

    /**
     * Save the dictionary
     * @param path The path of the file for saving the dictionary
     * @throws WordsExtractorException
     */
    @NotNull
    default void saveAsTxtIn(String path) throws WordsExtractorException {
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
     * Save instance of the dictionary class in a file
     * @param path The file's path
     */
    void saveAsBinIn(String path) throws IOException;

    /**
     * Save instance of the given dictionary class in a file
     * @param path The file's path
     * @param obj Class instance
     */
    default void saveAsBinIn(String path, final Serializable obj) throws IOException {
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
            throw new IOException("Can't save dictionary in the file " + path + ": " + e);
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
    static <T extends Dictionary> T readAsBinFrom(String path) throws IOException, ClassNotFoundException {
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
    default void removeWordsOfDict(final Dictionary dict) {
        if(dict != null)
            dict.getWords().forEach(word -> { if(!removeWord(word)) log.warn("Can't remove word '" + word + "' from dictionary"); });
        else
            log.error("The given dictionary is NULL");
    }

    /**
     * Get list of words in the dictionary
     * @return The words list
     */
    @NotNull
    List<String> getWords();

    /**
     * Get list of translations
     * @return The translations
     */
    @NotNull
    List<?> getTranslations();

    /**
     * Get sorted list of translations(sorted by translated words)
     * @return The sorted list
     */
    @NotNull
    List<?> getSortedTranslations();

    /**
     * Get list of words they aren't translated
     * @return The list of words
     */
    List<String> getNotTranslatedWords();

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
}
