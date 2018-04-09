package free.wordsextractor.bl.extraction.file_proc;

import free.wordsextractor.bl.WordsExtractorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;

public abstract class Utils {
    private static final Logger log = LogManager.getLogger(Utils.class);

    public enum Shells { POWERSHELL, BASH }

    public static String runSystemCommand(String cmd) throws WordsExtractorException {
        final Shells shell = getShell();
        switch (shell) {
            case POWERSHELL: return runCommandInPS(cmd);
            case BASH:       return runCommandInBash(cmd);
            default:
                log.error("Unknown command line environment: " + shell.toString());
        }
        return "";
    }

    private static String runCommandInPS(String command) {
        log.debug("Run PowerShell command '" + command + "'");

        String[] commandList = {"powershell.exe", "-Command", command};
        return runCommand(commandList);
    }

    private static String runCommand(String[] commandList) {
        final ProcessBuilder pb = new ProcessBuilder(commandList);
        try {
            Process p = pb.start();
            try ( BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())) ) {
                StringBuilder strBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    strBuilder = strBuilder.append(line);
                return strBuilder.toString();
            }
        } catch (IOException e) {
            log.error("Can't run the command: " + e);
        }
        return "";
    }

    private static String runCommandInBash(String command) {
        log.debug("Run Bash command '" + command + "'");

        String[] commandList = {"bash", "-c", command};
        return runCommand(commandList);
    }

    public static Shells getShell() throws WordsExtractorException {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            throw new WordsExtractorException("Can't detect the OS' name");
        }
        osName = osName.toLowerCase(Locale.ENGLISH);
        if (osName.contains("windows")) {
            return Utils.Shells.POWERSHELL;
        }
        else if (osName.contains("linux")) {
            return Utils.Shells.BASH;
        }
        else
            throw new WordsExtractorException("Unknown OS' name " + osName);
    }

    public static String getResourcePathStr(Object obj, String fileName) throws URISyntaxException {
        URL path = obj.getClass().getClassLoader().getResource(fileName);
        Assert.assertNotNull("Can't found the resource file " + fileName, path);
        return Paths.get(path.toURI()).toString();
    }
}
