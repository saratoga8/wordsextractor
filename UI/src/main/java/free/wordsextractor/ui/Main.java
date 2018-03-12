package free.wordsextractor.ui;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.FileManager;
import free.wordsextractor.bl.extraction.file_proc.extractors.WordsExtractor;
import free.wordsextractor.bl.extraction.txt_proc.Dictionary;
import free.wordsextractor.bl.translation.TranslationManager;

import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String [] args) {
        if (args.length > 2) {
            String txtFilePath = args[1],
                    extractedWordsTxtFilePath = args[2];


            try {
                final FileManager fileMngr = new FileManager(txtFilePath);
                final List<Path> pathsList = fileMngr.extractTxtFiles(123);
                final WordsExtractor extractor = new WordsExtractor(pathsList);
                Dictionary wordsStatsDict = extractor.createWordsStatsDictionary();

                final TranslationManager translationMngr = new TranslationManager(wordsStatsDict);
                translationMngr.removeKnownWords(); /*
                translationMngr.createExtractedWordsDict().saveIn(extractedWordsTxtFilePath); */
            }
            catch (WordsExtractorException e) {
                System.err.println("Running interrupted by exception " + e);
            }
        }
        else
            System.err.println("Invalid number of parameters.\nShould be: " + args[0] + " file1 file2\nWhere: \n\tfile1 - where text should be extracted from\n\tfile2 - where the extracted text should be saved");
    }
}
