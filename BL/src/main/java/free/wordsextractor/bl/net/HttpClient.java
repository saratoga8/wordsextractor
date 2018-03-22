package free.wordsextractor.bl.net;

import com.drew.lang.annotations.NotNull;
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
import java.util.HashMap;

/**
 * HTTP client
 */
public abstract class HttpClient {
    private static final Logger log = LogManager.getLogger(HttpClient.class);        /* logger */
    private final static int RESPONSE_OK = 200, RESPONSE_MULTIPLE_CHOICE = 300;

    @NotNull
    /**
     * Get response from the given URL
     * @param url The URL
     * @param respCodes Response codes (error cases)
     * @return String of the response. Empty string if the request has failed
     */
    public static String getResponseFrom(String url, final HashMap<Integer, String> respCodes) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final ResponseHandler<String> responseHandler = response -> handleResponse(response, respCodes);
            return client.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            log.error("Can't get response from the URL: " + url + ": " + e);
        }
        return "";
    }

    @NotNull
    /**
     * Handle the given response
     * @param response HTTP response item
     * @param respCodes Response codes (error cases)
     * @return String of response. . Empty string if the request has failed
     * @throws IOException
     */
    protected static String handleResponse(final HttpResponse response, final HashMap<Integer, String> respCodes) throws IOException {
        final int status = response.getStatusLine().getStatusCode();
        if ((status >= RESPONSE_OK) && (status < RESPONSE_MULTIPLE_CHOICE)) {
            final HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : "";
        } else {
            String errorStr = (respCodes.containsKey(Integer.valueOf(status))) ? respCodes.get(Integer.valueOf(status)): "Unexpected response status: " + status;
            throw new ClientProtocolException("Can't handle response: " + errorStr);
        }
    }
}
