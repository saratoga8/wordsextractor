package free.wordsextractor.bl.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public abstract class HttpClient {
    private static final Logger log = LogManager.getLogger(HttpClient.class);        /* logger */
    private final static int RESPONSE_OK = 200, RESPONSE_MULTIPLE_CHOICE = 300;


    public static String getResponseFrom(String url) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final ResponseHandler<String> responseHandler = response -> handleResponse(response);
            return client.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            log.error("Can't get response from the URL: " + url + ": " + e);
        }
        return "";
    }

    private static String handleResponse(final HttpResponse response) throws IOException {
        final int status = response.getStatusLine().getStatusCode();
        if ((status >= RESPONSE_OK) && (status < RESPONSE_MULTIPLE_CHOICE)) {
            final HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : "";
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}
