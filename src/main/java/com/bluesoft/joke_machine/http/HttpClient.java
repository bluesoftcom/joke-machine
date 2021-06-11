package com.bluesoft.joke_machine.http;

import java.net.http.HttpResponse;
import java.util.Map;

public interface HttpClient {

    HttpResponse<String> get(final String baseUrl,
                             final String path,
                             final Map<String, String> requestHeaders,
                             final Map<String, String> queryParameters) throws HttpClientException;

    HttpResponse<String> get(final String baseUrl,
                             final String path) throws HttpClientException;

    HttpResponse<String> get(final String baseUrl,
                             final String path,
                             final Map<String, String> queryParameters) throws HttpClientException;
}
