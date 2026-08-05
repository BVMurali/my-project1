package com.eventhub.framework.helpers;

import com.eventhub.framework.utilities.LogUtils;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Lightweight REST helper for optional API-level validations/setup that
 * complement the UI flows (e.g. verifying backend state without opening a
 * browser). Not required for the current UI test flows, provided for
 * extensibility per the enterprise framework requirements.
 */
public class ApiHelper {

    private static final Logger log = LogUtils.getLogger(ApiHelper.class);
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public HttpResponse<String> get(String url) {
        return send(HttpRequest.newBuilder(URI.create(url)).GET().build());
    }

    public HttpResponse<String> post(String url, String jsonBody) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return send(request);
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            log.info("Sending {} request to {}", request.method(), request.uri());
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("API request failed: " + request.uri(), e);
        }
    }
}
