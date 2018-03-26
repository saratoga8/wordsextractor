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


    private class OnlyText {
        @SerializedName("text")
        protected String word;

        @SerializedName("pos")
        protected String speechPart;

        public String getWord() {
            return word;
        }

        public String getSpeechPart() {
            return (StringUtils.isBlank(speechPart)) ? "": speechPart;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public void setSpeechPart(String speechPart) {
            this.speechPart = speechPart;
        }

        public String toString() {
            return word + " " + getSpeechPart();
        }
    }


    private class DictionaryEntry extends OnlyText {
        @SerializedName("ts")
        private String transcription;

        @SerializedName("tr")
        private ArrayList<Translation> translations;

        public String getTranscription() {
            return (StringUtils.isBlank(transcription)) ? "": "[" + transcription + "]";
        }

        public void setTranscription(String transcription) {
            this.transcription = transcription;
        }

        public String toString() {
            final StringBuilder txt = new StringBuilder(word).append(" " + getTranscription() +  (" ") + getSpeechPart() + "\n");
            translations.stream().forEach(translation -> txt.append("\t" + translation.toString() + "\n"));

            return txt.toString();
        }
    }

    private class Translation extends OnlyText {
        @SerializedName("syn")
        private ArrayList<Synonymous> synonymouses;

        @SerializedName("mean")
        private ArrayList<Example> examples;

        public ArrayList<Synonymous> getSynonymouses() {
            return synonymouses;
        }

        public void setSynonymouses(ArrayList<Synonymous> synonymouses) {
            this.synonymouses = synonymouses;
        }

        public String toString() {
            StringBuilder txt = new StringBuilder(word);
            if(synonymouses != null)
                synonymouses.stream().forEach(synonymous -> txt.append(synonymous + " "));
            if(examples != null) {
                txt.append("\n\t(");
                examples.stream().forEach(example -> txt.append(example + ", "));
                txt.setCharAt(txt.length() - 2, ')');
            }

            return txt.toString();
        }

        private class Synonymous extends OnlyText {
            public String toString() {
                return ", " + getWord();
            }
        }

        private class Example extends Synonymous {
            public String toString() {
                return getWord();
            }
        }
    }

    @NotNull
    public String toString() {
        StringBuilder txt = new StringBuilder();
        dictEntries.stream().forEach(entry -> txt.append(entry.toString() + "\n"));
        return txt.toString();
    }
}
