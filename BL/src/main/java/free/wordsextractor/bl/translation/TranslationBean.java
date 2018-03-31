package free.wordsextractor.bl.translation;

import com.drew.lang.annotations.NotNull;

/**
 * A bean of translation
 */
public interface TranslationBean {

    /**
     * Get name of a translation service
     * @return Translation service name
     */
    @NotNull
    String getName();

    /**
     * Convert to string
     * @return The string representation
     */
    @NotNull
    String toString();
}
