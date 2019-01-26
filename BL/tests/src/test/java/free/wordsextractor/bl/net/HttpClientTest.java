package free.wordsextractor.bl.net;

import free.wordsextractor.common.tests.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpClientTest {
    @DisplayName("HTTP client with existing URL")
    @Test
    public void httpClientValidURL() {
        String URL = "https://api.ipify.org";
        try {
            String command = (Utils.getShell() == Utils.Shells.POWERSHELL) ? "Invoke-RestMethod http://ipinfo.io/json | Select -exp ip": "curl " + URL;
            String ip = Utils.runSystemCommand(command);
            Assert.assertEquals(ip, WebClient.getResponseFrom(URL));
        } catch (Exception e) {
            Assert.assertTrue("Test aborted because of exception: " + e, false);
        }
    }
}