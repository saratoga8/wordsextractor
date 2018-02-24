package free.wordsextractor.bl.file_proc;

import free.wordsextractor.bl.WordExtractorException;
import free.wordsextractor.bl.file_proc.extractors.ExtractionManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Manage operations with a file
 */
public class FileManager {
    private final Path path;

    /**
     * Constructor
     * @param path The path of a file. From the file will be extracted text
     * @throws WordExtractorException
     */
    public FileManager(String path) throws WordExtractorException {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile())
                this.path = Paths.get(path);
            else
                throw new WordExtractorException("The given path " + path + " isn't file!");
        }
        else
            throw new WordExtractorException("The file " + path + " doesn't exist!");
    }

    /**
     * Extract text files each one with the given size
     * @param eachFileSizeBytes The size of a distinguish text file in bytes
     * @return The list of paths of the text files containing the extracted text
     * @throws WordExtractorException
     */
    public List<Path> extractTxtFiles(long eachFileSizeBytes) throws WordExtractorException {
        final LinkedList<Path> files = new LinkedList<>();
        String txt = ExtractionManager.extractTxtFrom(path);
        if (!txt.isEmpty()) {
            files.add(saveTxt(txt));
        }
        else
            throw new WordExtractorException("No text has extracted from " + path);
        return files;
    }

    /**
     * Save text in a file
     * @param txt The text for saving
     * @return The path of created text file
     */
    private Path saveTxt(String txt) throws WordExtractorException {
        try {
            final File txtFile = File.createTempFile(path.getFileName().toString().split("\\.")[0], ".txt");
            txtFile.deleteOnExit();
            try (FileOutputStream outputStream = new FileOutputStream(txtFile)) {
                try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16")) {
                    try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                        bufferedWriter.write(txt);
                        bufferedWriter.close();
                    }
                    return Paths.get(txtFile.getCanonicalPath());
                }
            }
        }
        catch (IOException e) {
            throw new WordExtractorException("Can't save text in file: " + e.toString());
        }
    }
}
