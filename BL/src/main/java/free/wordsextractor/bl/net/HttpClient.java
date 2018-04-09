package free.wordsextractor.bl.net;

import com.drew.lang.annotations.NotNull;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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
    public static String getResponseFrom(String url) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final ResponseHandler<String> responseHandler = response -> handleResponse(response);
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
    private static String handleResponse(final HttpResponse response) {
        final int status = response.getStatusLine().getStatusCode();
        if ((status < RESPONSE_OK) || (status > RESPONSE_MULTIPLE_CHOICE))
            log.error("The response status code is " + status);
        return getResponseEntityStr(response);
    }

    private static String getResponseEntityStr(final HttpResponse response) {
        final HttpEntity entity = response.getEntity();
        try {
            return entity != null ? EntityUtils.toString(entity) : "";
        } catch (IOException e) {
            log.error("Can't convert response entity to string: " + e);
        }
        return "";
    }
}
