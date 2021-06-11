package com.bluesoft.joke_machine.http;

import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JAVA 11  FEATURE - HTTP CLIENT
 */
public class BasicHttpClient implements HttpClient {

    private java.net.http.HttpClient embeddedHttpClient;
    private Map<String, String> globalHeaders;

    public BasicHttpClient(final java.net.http.HttpClient.Version version,
                           final Integer timeout) {
        embeddedHttpClient = java.net.http.HttpClient.newBuilder()
                .version(version)
                .connectTimeout(Duration.ofSeconds(timeout))
                .build();
        globalHeaders = Map.of("Content-Type", "application/json");
    }

    public HttpResponse<String> get(final String baseUrl,
                                    final String path,
                                    final Map<String, String> requestHeaders,
                                    final Map<String, String> queryParameters) throws HttpClientException {

        requestHeaders.putAll(globalHeaders);

        final URI uri = createUri(baseUrl, path, queryParameters);
        final String[] formattedRequestHeaders = convertRequestHeaders(requestHeaders);

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .headers(formattedRequestHeaders)
                .build();

        try {
            return embeddedHttpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientException("Error in getting data from external resource", e);
        }
    }

    @Override
    public HttpResponse<String> get(String baseUrl, String path) throws HttpClientException {
        return get(baseUrl, path, new HashMap<>(), new HashMap<>());
    }

    @Override
    public HttpResponse<String> get(final String baseUrl, final String path, final Map<String, String> queryParameters) throws HttpClientException {
        return get(baseUrl, path, new HashMap<>(), queryParameters);
    }

    /**
     * JAVA 16  FEATURE - mapMulti
     */
    private String[] convertRequestHeaders(Map<String, String> requestHeaders) {
        return requestHeaders.entrySet()
                .stream()
                .mapMulti((e, consumer) -> {
                    consumer.accept(e.getKey());
                    consumer.accept(e.getValue());
                })
                .map(Object::toString)
                .toArray(String[]::new);
    }

    private URI createUri(final String baseUrl,
                          final String path,
                          final Map<String, String> queryParameters) {

        var queryParametersMVM = toMultiValueMap(queryParameters);
        return UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path(path)
                .queryParams(queryParametersMVM)
                .build()
                .toUri();
    }

    private MultiValueMap<String, String> toMultiValueMap(Map<String, String> queryParameters) {
        return CollectionUtils.toMultiValueMap(queryParameters.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> List.of(v.getValue()))));
    }

}
