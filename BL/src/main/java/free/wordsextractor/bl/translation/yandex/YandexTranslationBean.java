package free.wordsextractor.bl.translation.yandex;

import com.drew.lang.annotations.NotNull;
import com.google.gson.annotations.SerializedName;
import free.wordsextractor.bl.translation.TranslationBean;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class YandexTranslationBean implements TranslationBean {
    private static final Logger log = LogManager.getLogger(YandexTranslationBean.class);     /* log item */

    @SerializedName("def")
    private ArrayList<DictionaryEntry> dictEntries;


    public ArrayList<DictionaryEntry> getDictEntries() {
        return dictEntries;
    }

    public void setDictEntries(ArrayList<DictionaryEntry> dictEntries) {
        this.dictEntries = dictEntries;
    }

    @Override
    public String getName() {
        return "Yandex";
    }

    public class DictionaryEntry {
        @SerializedName("text")
        private String word;

        @SerializedName("pos")
        private String speechPart;

        @SerializedName("ts")
        private String transcription;

        public String getWord() {
            return word;
        }

        public String getSpeechPart() {
            return speechPart;
        }

        public String getTranscription() {
            return transcription;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public void setSpeechPart(String speechPart) {
            this.speechPart = speechPart;
        }

        public void setTranscription(String transcription) {
            this.transcription = transcription;
        }

        public String toString() {
            StringBuilder txt = new StringBuilder(word);
            txt = txt.append((" ") + getSpeechPart());
            if (!StringUtils.isBlank(transcription))
                txt = txt.append((" ") + getTranscription());
            log.debug("Translation is " + txt.toString());
            return txt.toString();
        }
    }

    @NotNull
    public String toString() {
        StringBuilder txt = new StringBuilder();
        dictEntries.stream().forEach(entry -> txt.append(entry.toString() + "\n"));
        return txt.toString();
    }
}
