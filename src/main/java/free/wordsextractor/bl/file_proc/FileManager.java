package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.file_proc.extractors.ExtractionManager;
import free.wordsextractor.bl.file_proc.extractors.TextExtractorInterface;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Manage operations with a file
 */
public class FileManager {
    static private final Logger log = LogManager.getLogger(FileManager.class);           /** logger */

    private final Path path;                                                             /** path of the file text should be extracted from */
    private final ExtractionManager extractionMgr;                                       /** manager of extractions */

    /**
     * Constructor
     * @param path The path of a file. From the file will be extracted text
     * @throws WordsExtractorException
     */
    public FileManager(String path) throws WordsExtractorException {
        log.debug("Initialization by file " + path);
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile())
                this.path = Paths.get(path);
            else
                throw new WordsExtractorException("The given path " + path + " isn't file!");
        }
        else
            throw new WordsExtractorException("The file " + path + " doesn't exist!");

        extractionMgr = new ExtractionManager();
    }

    /**
     * Extract text files each one with the given size
     * @param eachFileSizeBytes The size of a distinguish text file in bytes
     * @return The list of paths of the text files containing the extracted text
     * @throws WordsExtractorException
     */
    public List<Path> extractTxtFiles(long eachFileSizeBytes) throws WordsExtractorException {
        log.debug("Extracting text files with size " + eachFileSizeBytes + " of each one");

        final LinkedList<Path> files = new LinkedList<>();
        String txt = extractionMgr.extractTxtFrom(path).trim();
        if (!txt.isEmpty())
            files.add(saveTxt(txt));
        else
            throw new WordsExtractorException("No text has extracted from " + path);

        return files;
    }

    /**
     * Save text in a file
     * @param txt The text for saving
     * @return The path of created text file
     */
    private Path saveTxt(String txt) throws WordsExtractorException {
        if (StringUtils.isBlank(txt))
            throw new WordsExtractorException("The given text for saving is NULL or EMPTY");

        log.debug("Save text '" + txt + "' in a file");
        try {
            final File txtFile = File.createTempFile(path.getFileName().toString().split("\\.")[0], ".txt");
            txtFile.deleteOnExit();
            try (FileOutputStream outputStream = new FileOutputStream(txtFile)) {
                try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, TextExtractorInterface.CHAR_SET)) {
                    try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                        bufferedWriter.write(txt);
                        bufferedWriter.close();
                    }
                    log.debug("Text saved in the file " + txtFile.getCanonicalPath());
                    return Paths.get(txtFile.getCanonicalPath());
                }
            }
        }
        catch (IOException e) {
            throw new WordsExtractorException("Can't save text in file: " + e.toString());
        }
    }
}
