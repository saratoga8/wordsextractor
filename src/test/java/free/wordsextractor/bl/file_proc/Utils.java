package free.wordsextractor.bl.file_proc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Utils {
    private static Logger log = LogManager.getLogger(Utils.class);

    enum Shells { POWERSHELL, BASH }
    public static void runSystemCommand(final Shells shell, String cmd) {
        switch (shell) {
            case POWERSHELL: runCommandInPS(cmd);
        }
    }

    private static String runCommandInPS(String command) {
        log.debug("Run PowerShell command '" + command + "'");

        String[] commandList = {"powershell.exe", "-Command", command};
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
}
