package free.wordsextractor.bl.net;

import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * HTTP client
 */
public abstract class WebClient {
    private static final Logger log = LogManager.getLogger(WebClient.class);        /* logger */
    private final static int RESPONSE_OK = 200, RESPONSE_MULTIPLE_CHOICE = 300;

    /**
     * Get response from the given URL
     * @param url The URL
     * @return String of the response. Empty string if the request has failed
     */
    public static String getResponseFrom(String url) {
        if(!TextUtils.isBlank(url)) {
            final var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            final var httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
            try {
                var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                return handleResponse(response);
            } catch (IOException | InterruptedException e) {
                log.error("Can't get response from the URL: " + url + ": " + e);
            }
        }
        else
            log.error("The given URL is NULL or EMPTY");
        return "";
    }

    /**
     * Handle the given response
     * @param response HTTP response item
     * @return String of response. Empty string if the request has failed
     */
    private static String handleResponse(final HttpResponse<String> response) {
        final int status = response.statusCode();
        if ((status < RESPONSE_OK) || (status > RESPONSE_MULTIPLE_CHOICE))
            log.error("The response status code is " + status);
        return response.body();
    }

    /**
     * Get string of response entity
     * @param response HTTP response item
     * @return The response entity string
     *
    private static String getResponseEntityStr(final HttpResponse response) {
        final HttpEntity entity = response.getEntity();
        try {
            return entity != null ? EntityUtils.toString(entity) : "";
        } catch (IOException e) {
            log.error("Can't convert response entity to string: " + e);
        }
        return "";
    }*/
}
