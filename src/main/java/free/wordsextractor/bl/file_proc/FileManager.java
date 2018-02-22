package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordExtractorException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Manage operations with a file
 */
public class FileManager {
    private final Path path;

    public FileManager(String path) throws WordExtractorException {
        if (new File(path).exists())
            this.path = Paths.get(path);
        else
            throw new WordExtractorException("The file " + path + " doesn't exist!");
    }

    private String getTxt() {
        final List<TextExtractor> extractors = Arrays.asList(new VideoTextExtractor(path), new RegularTextExtractor(path));

        String txt = "";
        for (TextExtractor txtExtractor: extractors) {
            txt = txtExtractor.extractTxt();
            if (!txt.isEmpty())
                break;
        }
        return txt;
    }

    public List<Path> extractTxtFiles() {
        LinkedList<Path> files = new LinkedList<>();
        return files;
    }
}
