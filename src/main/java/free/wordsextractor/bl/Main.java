package free.wordsextractor.bl;

import free.wordsextractor.bl.file_proc.FileManager;
import free.wordsextractor.bl.txt_proc.WordsExtractor;

import java.nio.file.Path;
import java.util.List;

/**
 * The main class
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        String txtFilePath = args[1],
                extractedWordsTxtFilePath = args[2];
        try {
            final FileManager fileMngr = new FileManager(txtFilePath);
            final List<Path> pathsList = fileMngr.extractTxtFiles(123);
            final WordsExtractor extractor = new WordsExtractor(pathsList);
            extractor.createDictionary().save(extractedWordsTxtFilePath);
        }
        catch (WordExtractorException e) {
            System.err.println(e.toString());
        }
    }
}
