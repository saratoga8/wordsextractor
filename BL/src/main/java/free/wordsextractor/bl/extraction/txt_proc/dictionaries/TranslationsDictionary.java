package free.wordsextractor.bl.extraction.txt_proc.dictionaries;

import com.drew.lang.annotations.NotNull;
import com.google.common.collect.Lists;
import free.wordsextractor.bl.WordsExtractorException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TranslationsDictionary implements Dictionary {
    private static final Logger log = LogManager.getLogger(TranslationsDictionary.class);        /* logger */

    private final Hashtable<String, String> dict = new Hashtable<>();
    private final List<String> notTranslatedWords = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void addWord(String word) throws WordsExtractorException {
        throw new WordsExtractorException("Only word shouldn't be added. A word should be added with translation");
    }

    @NotNull
    @Override
    synchronized public void addTranslation(String word, String translation) {
        if(!StringUtils.isBlank(word)) {
            if(translation != null) {
                if (!translation.isEmpty())
                    dict.put(word, translation);
                else
                    notTranslatedWords.add(word);
            }
            else
                log.error("The given translation of '" + word + "' is NULL");
        }
        else
            log.error("The given word is NULL or EMPTY");
    }



    @Override
    synchronized public boolean contains(String word) {
        OperationOnWord<Boolean> operation = wd -> dict.containsKey(wd) || notTranslatedWords.contains(wd);
        return Dictionary.secureOperationOnWord(word, operation, Boolean.FALSE);
    }

    @Override
    synchronized public boolean removeWord(String word) {
        OperationOnWord<Boolean> operation = wd -> (dict.containsKey(word)) ? (dict.remove(word) != null): notTranslatedWords.remove(word);
        return Dictionary.secureOperationOnWord(word, operation, Boolean.FALSE);
    }

    @Override
    synchronized public String getTranslation(String word) {
        return Dictionary.secureOperationOnWord(word, (wrd -> (dict.containsKey(wrd)) ? dict.get(wrd): ""), "");
    }

    @Override
    synchronized public List<String> getWords() {
        final List<String> unitedList = Lists.newArrayList(dict.keySet());
        unitedList.addAll(notTranslatedWords);
        return unitedList;
    }

    @Override
    synchronized public List<?> getTranslations() {
        return new ArrayList<>(dict.values());
    }

    @Override
    synchronized public List<?> getSortedTranslations() {
        final List<String> translatedWords = Lists.newArrayList(dict.keySet());
        translatedWords.sort(String.CASE_INSENSITIVE_ORDER);

        final List<String> sortedTranslations = new LinkedList<>();
        translatedWords.forEach(word -> sortedTranslations.add(getTranslation(word)));
        return sortedTranslations;
    }

    @NotNull
    @Override
    public List<String> getNotTranslatedWords() {
        return notTranslatedWords;
    }
}
