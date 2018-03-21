package free.wordsextractor.bl.net;

import free.wordsextractor.bl.WordsExtractorException;
import free.wordsextractor.bl.extraction.file_proc.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpClientTest {
    @DisplayName("HTTP client")
    @Test
    public void httpClient() {
        String command = null;
        try {
            command = (Utils.getShell() == Utils.Shells.POWERSHELL) ? "Invoke-RestMethod http://ipinfo.io/json | Select -exp ip": "";
            Utils.runSystemCommand(command);
            Assert.assertEquals("194.90.225.123",HttpClient.getResponseFrom("https://api.ipify.org"));
        } catch (WordsExtractorException e) {
            Assert.assertTrue("Test aborted because of exception: " + e, false);
        }
    }
}