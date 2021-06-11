package com.bluesoft.joke_machine.joke.service.provider.external;

import com.bluesoft.joke_machine.http.HttpClient;
import com.bluesoft.joke_machine.http.HttpClientException;
import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Service
public class ChuckNorrisJokeProvider implements ExternalJokeProvider {

    public static final String THE_RESPONSE_BODY_IS_NOT_A_VALID_JSON = "The response body is not a valid Json";
    private final Logger log = LoggerFactory.getLogger(ChuckNorrisJokeProvider.class);

    private static final String baseUrl = "https://api.chucknorris.io";
    private static final String randomJokePath = "/jokes/random";
    private static final String categoryPath = "/jokes/categories";

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Autowired
    public ChuckNorrisJokeProvider(final HttpClient httpClient,
                                   final ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Joke> getJokeFromCategory(final Category category) {
        try {
            var queryParams = Map.of("category", category.getValue().toLowerCase());
            final HttpResponse<String> responseBody =
                    httpClient.get(baseUrl, randomJokePath, queryParams);
            return Optional.of(convertToJoke(responseBody, category));
        } catch (HttpClientException | ExternalJokeProviderException e) {
            log.error("Cannot fetch joke from {} in {} category",
                    category.getProvider(),
                    category.getValue());
            return Optional.empty();
        }
    }


    @Override
    public Optional<Set<Category>> getAvailableCategories() {
        try {
            final String responseBody = extractBodyOrThrowException(httpClient.get(baseUrl, categoryPath));
            return Optional.of(convertToSetOfCategories(responseBody));
        } catch (HttpClientException | ExternalJokeProviderException e) {
            log.error("Cannot fetch category from {}", this.getProviderName());
            return Optional.empty();
        } catch (JsonProcessingException e) {
            log.error("Cannot deserializer response with joke categories from {}", this.getProviderName());
            return Optional.empty();
        }
    }

    private Joke convertToJoke(final HttpResponse<String> responseBody,
                               final Category category) throws ExternalJokeProviderException {
        try {
            final String body = extractBodyOrThrowException(responseBody);
            final JsonNode jsonNode = objectMapper.readTree(body);
            return new Joke(jsonNode.path("value").asText().trim(), category);
        } catch (JsonProcessingException e) {
            throw new ExternalJokeProviderException(THE_RESPONSE_BODY_IS_NOT_A_VALID_JSON);
        }
    }

    private Set<Category> convertToSetOfCategories(final String responseBody) throws JsonProcessingException {
        return Arrays.stream(objectMapper.readValue(responseBody, String[].class))
                .map(String::toLowerCase)
                .map(StringUtils::capitalize)
                .map(c -> new Category(c, this.getProviderName()))
                .collect(Collectors.toSet());
    }

    private String extractBodyOrThrowException(final HttpResponse<String> response) throws ExternalJokeProviderException {
        if (response.statusCode() != HttpStatus.OK.value()) {
            throw new ExternalJokeProviderException(String.format("%s did not respond with not valid http status",
                    this.getProviderName()));
        }
        return response.body();
    }

    //this is only for demo purposes....
    public DeserializationConfig getDeserializationConfig() {
        return objectMapper.getDeserializationConfig();
    }
}
