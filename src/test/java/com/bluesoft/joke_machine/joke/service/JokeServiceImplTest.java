package com.bluesoft.joke_machine.joke.service;

import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.dispatcher.JokeDispatcher;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherException;
import com.bluesoft.joke_machine.joke.service.provider.external.ChuckNorrisJokeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JokeServiceImplTest {

    @Autowired
    private JokeService jokeService;

    @MockBean
    private JokeDispatcher jokeDispatcherMock;

    @Test
    public void shouldCallGetJokeWithCategoryFromGivenProviderWhenAllParametersAreProvided() throws JokeDispatcherException, JokeServiceException {
        //given
        final String providerName = ChuckNorrisJokeProvider.class.getSimpleName();
        final String category = "Political";

        //when
        final Joke randomJoke = jokeService.getRandomJoke(providerName, category);

        //then
        Mockito.verify(jokeDispatcherMock).getJokeWithCategoryFromGivenProvider(providerName, category);
    }

    @Test
    public void shouldCallGetJokeWithRandomCategoryFromGivenProviderWhenOnlyProviderNameIsPresent() throws JokeDispatcherException, JokeServiceException {
        //given
        final String providerName = ChuckNorrisJokeProvider.class.getSimpleName();

        //when
        jokeService.getRandomJoke(providerName, "   ");

        //then
        Mockito.verify(jokeDispatcherMock).getJokeWithRandomCategoryFromGivenProvider(providerName);
    }

    @Test
    public void shouldCallCetJokeFromAnyProviderServingThisCategoryWhenOnlyCategoryIsPresent() throws JokeDispatcherException, JokeServiceException {
        //given
        final String category = "Political";

        //when
        jokeService.getRandomJoke(null, category);

        //then
        Mockito.verify(jokeDispatcherMock).getJokeFromAnyProviderServingThisCategory(category);
    }

    @Test
    public void shouldCallGetJokeFromAnyAvailableProviderWhenNoParametersAreProvided() throws JokeDispatcherException, JokeServiceException {
        //given

        //when
        jokeService.getRandomJoke("", null);

        //then
        Mockito.verify(jokeDispatcherMock).getJokeFromAnyAvailableProvider();
    }

    @Test
    public void shouldCallGetAllCategoriesFromGivenProviderIfProviderNameIsPresent() throws JokeDispatcherException, JokeServiceException {
        //given
        final String providerName = ChuckNorrisJokeProvider.class.getSimpleName();

        //when
        jokeService.getJokeCategories(providerName);

        //then
        Mockito.verify(jokeDispatcherMock).getAllCategoriesFromGivenProvider(providerName);
    }

    @Test
    public void shouldCallGetAllCategoriesWhenProviderIsNotPresent() throws JokeDispatcherException, JokeServiceException {
        //given

        //when
        jokeService.getJokeCategories(null);

        //then
        Mockito.verify(jokeDispatcherMock).getAllCategories();
    }

    @Test
    public void shouldCallGetAllCategoriesFromGivenProviderWhenProviderNameIsPresent() throws JokeDispatcherException, JokeServiceException {
        //given
        final String providerName = ChuckNorrisJokeProvider.class.getSimpleName();

        //when
        jokeService.getJokeCategories(providerName);

        //then
        Mockito.verify(jokeDispatcherMock).getAllCategoriesFromGivenProvider(providerName);
    }

    @Test
    public void shouldCallGetAvailableJokeProvidersNames() {
        //given

        //when
        jokeService.getJokeProviders();

        //then
        Mockito.verify(jokeDispatcherMock).getAvailableJokeProvidersNames();
    }
}