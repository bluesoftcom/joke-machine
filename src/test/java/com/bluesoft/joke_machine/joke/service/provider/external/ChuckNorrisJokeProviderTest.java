package com.bluesoft.joke_machine.joke.service.provider.external;

import com.bluesoft.joke_machine.http.HttpClient;
import com.bluesoft.joke_machine.http.HttpClientException;
import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChuckNorrisJokeProviderTest {

    @MockBean
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    @InjectMocks
    private ChuckNorrisJokeProvider chuckNorrisJokeProvider;

    @Test
    public void shouldGetJokeFromCategory() throws HttpClientException, IOException {
        //given
        final HttpResponse httpResponseMock = Mockito.mock(HttpResponse.class);
        final String randomJokeContent = getJsonFromFile("randomJokeFromChuckNorrisProvider.json");

        Mockito.when(httpResponseMock.body()).thenReturn(randomJokeContent);
        Mockito.when(httpResponseMock.statusCode()).thenReturn(HttpStatus.OK.value());

        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(httpResponseMock);

        //when
        final Optional<Joke> joke = chuckNorrisJokeProvider.getJokeFromCategory(new Category("music", chuckNorrisJokeProvider.getProviderName()));

        //then
        assertThat(joke).isNotEqualTo(Optional.empty());
    }

    @Test
    public void shouldReturnOptionalEmptyWhenUnableToParseJson() throws HttpClientException, IOException {
        //given
        final HttpResponse httpResponseMock = Mockito.mock(HttpResponse.class);
        final String randomJokeContent = getJsonFromFile("randomJokeFromChuckNorrisProvider.json");

        Mockito.when(httpResponseMock.body()).thenReturn(randomJokeContent.replace(":",";"));
        Mockito.when(httpResponseMock.statusCode()).thenReturn(HttpStatus.OK.value());

        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(httpResponseMock);

        //when
        final Optional<Joke> joke = chuckNorrisJokeProvider.getJokeFromCategory(
                new Category("music", chuckNorrisJokeProvider.getProviderName()));

        //then
        assertThat(joke).isEqualTo(Optional.empty());
    }


    @Test
    public void shouldGetAvailableCategories() throws IOException, HttpClientException {
        //given
        final HttpResponse httpResponseMock = Mockito.mock(HttpResponse.class);
        final String availableCategoriesJson = getJsonFromFile("chuckNorrisProviderCategories.json");

        Mockito.when(httpResponseMock.body()).thenReturn(availableCategoriesJson);
        Mockito.when(httpResponseMock.statusCode()).thenReturn(HttpStatus.OK.value());

        Mockito.when(httpClient.get(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(httpResponseMock);

        //when
        final Optional<Set<Category>> availableCategories = chuckNorrisJokeProvider.getAvailableCategories();

        //then
        assertThat(availableCategories).isNotEqualTo(Optional.empty());
    }

    @Test
    public void should() {
        //given

        //when

        //then
    }


    private String getJsonFromFile(final String fileName) throws IOException {
        final String path = resourceLoader
                .getResource(String.format("classpath:/%s",fileName))
                .getFile()
                .getPath();
        return Files.readString(Path.of(path));
    }
}