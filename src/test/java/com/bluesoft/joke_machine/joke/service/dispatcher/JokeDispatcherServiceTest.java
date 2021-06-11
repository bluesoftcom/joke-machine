package com.bluesoft.joke_machine.joke.service.dispatcher;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherCategoryNotFoundException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherJokeNotFoundException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherProviderNotFoundException;
import com.bluesoft.joke_machine.joke.service.provider.JokeProvider;
import com.bluesoft.joke_machine.joke.service.provider.external.ChuckNorrisJokeProvider;
import com.bluesoft.joke_machine.joke.service.provider.randomness.RandomnessProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bluesoft.joke_machine.joke.service.dispatcher.JokeDispatcherService.ALL_PROVIDERS_ARE_UNAVAILABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JokeDispatcherServiceTest {

    private JokeDispatcher jokeDispatcher;
    private List<JokeProvider> jokeProviderMockList;

    private JokeDispatcherServiceTest() {
        defaultSetup();
    }

    private void defaultSetup() {
        this.jokeProviderMockList = List.of(
                Mockito.mock(JokeProvider.class),
                Mockito.mock(ChuckNorrisJokeProvider.class));

        final RandomnessProvider randomnessProvider = getRandomnessProvider();
        this.jokeDispatcher = new JokeDispatcherService(jokeProviderMockList, randomnessProvider);
        mockJokeProvidersMethods();
    }

    @Test
    public void shouldGetJokeWithCategoryFromGivenProvider() throws JokeDispatcherException {
        //given
        final JokeProvider jokeProvider = jokeProviderMockList.get(0);
        final String providerName = jokeProvider.getProviderName();
        final Category category = jokeProvider.getAvailableCategories().get().stream().findFirst().get();

        //when
        final Joke joke = jokeDispatcher.getJokeWithCategoryFromGivenProvider(providerName, category.getValue());

        //then
        assertThat(joke).isNotNull();
        assertThat(providerName).isEqualTo(joke.getCategory().getProvider());
        assertThat(joke.getCategory()).isEqualTo(category);
    }

    @Test
    public void shouldGetJokeWithRandomCategoryFromGivenProvider() throws JokeDispatcherException {
        //given
        final JokeProvider jokeProvider = jokeProviderMockList.get(0);
        final String providerName = jokeProvider.getProviderName();

        //when
        final Joke joke = jokeDispatcher.getJokeWithRandomCategoryFromGivenProvider(providerName);

        //then
        assertThat(joke).isNotNull();
        assertThat(joke.getCategory().getProvider()).isEqualTo(providerName);
        assertThat(joke.getCategory()).isIn(jokeProvider.getAvailableCategories().get());
    }


    @Test
    public void shouldGetJokeFromAnyProviderServingThisCategory() throws JokeDispatcherException {
        //given
        final List<String> categoriesServedByAllAvailableProviders = getCategoriesServedByAllAvailableProviders();

        assertThat(categoriesServedByAllAvailableProviders).isNotEmpty();
        final String categoryServedByAllProviders = categoriesServedByAllAvailableProviders.get(0);

        //when
        final Joke joke = jokeDispatcher.getJokeFromAnyProviderServingThisCategory(categoryServedByAllProviders);

        //then
        assertThat(joke).isNotNull();
        final List<String> providersNames = jokeProviderMockList
                .stream()
                .map(JokeProvider::getProviderName)
                .collect(Collectors.toList());
        assertThat(joke.getCategory().getProvider()).isIn(providersNames);
        assertThat(joke.getCategory().getValue()).isEqualTo(categoryServedByAllProviders);
    }


    @Test
    public void shouldGetJokeFromAnyAvailableProvider() throws JokeDispatcherProviderNotFoundException {
        //given
        final List<String> providersNames = jokeProviderMockList
                .stream()
                .map(JokeProvider::getProviderName)
                .collect(Collectors.toList());

        final Set<Category> allAvailableCategories = jokeProviderMockList
                .stream()
                .map(JokeProvider::getAvailableCategories)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        //when
        final Joke joke = jokeDispatcher.getJokeFromAnyAvailableProvider();

        //then
        assertThat(joke).isNotNull();
        assertThat(joke.getCategory().getProvider()).isIn(providersNames);
        assertThat(joke.getCategory()).isIn(allAvailableCategories);
    }


    @Test
    public void shouldGetAllCategories() {
        //given
        final Set<Category> allAvailableCategories = jokeProviderMockList
                .stream()
                .map(JokeProvider::getAvailableCategories)
                .map(Optional::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        //when
        final Set<Category> allCategories = jokeDispatcher.getAllCategories();

        //then
        assertThat(allCategories).hasSameElementsAs(allAvailableCategories);
    }

    @Test
    public void shouldThrowProviderNotFoundException() {
        //given

        //when
        final Executable e = () -> jokeDispatcher.getAllCategoriesFromGivenProvider("notExistingProviderName");

        //then
        assertThrows(JokeDispatcherProviderNotFoundException.class, e);
    }


    @Test
    public void shouldThrowCategoryNotFoundException() {
        //given

        //when
        final Executable e = () -> jokeDispatcher.getJokeFromAnyProviderServingThisCategory("notExistingCategory");

        //then
        assertThrows(JokeDispatcherCategoryNotFoundException.class, e);
    }

    @Test
    public void shouldThrowProviderNotFoundExceptionWhenCallingGetJokeWithCategoryFromGivenProvider() {
        //given
        final String notExistingProviderName = "notExistingProviderName";
        final String existingCategoryName = jokeProviderMockList.get(0)
                .getAvailableCategories()
                .get()
                .stream()
                .findFirst()
                .get()
                .getValue();

        //when
        final Executable e = () -> jokeDispatcher.getJokeWithCategoryFromGivenProvider(notExistingProviderName, existingCategoryName);

        //then
        assertThrows(JokeDispatcherProviderNotFoundException.class, e);
    }


    @Test
    public void shouldThrowCategoryNotFoundExceptionWhenCallingGetJokeWithCategoryFromGivenProvider() {
        //given
        final String notExistingCategoryName = "notExistingCategoryName";

        //when
        final Executable e = () -> jokeDispatcher.getJokeFromAnyProviderServingThisCategory(notExistingCategoryName);

        //then
        assertThrows(JokeDispatcherCategoryNotFoundException.class, e);
    }

    @Test
    public void shouldGetAvailableJokeProvidersNames() {
        //given
        final List<String> providersNames = jokeProviderMockList
                .stream()
                .map(JokeProvider::getProviderName)
                .collect(Collectors.toList());
        //when
        final Set<String> availableJokeProvidersNames = jokeDispatcher.getAvailableJokeProvidersNames();

        //then
        assertThat(availableJokeProvidersNames).containsExactlyInAnyOrderElementsOf(providersNames);
    }

    @Test
    public void shouldThrowJokeDispatcherJokeNotFoundExceptionForProviderFailingToFetchJokeFromCategory() throws JokeDispatcherException {
        //given
        final JokeProvider jokeProvider = jokeProviderMockList.get(0);
        mockGetJokeFromCategoryIfProviderBecomesUnableToPerformOperation(jokeProvider);

        //when
        final Executable e = () -> jokeDispatcher.getJokeWithRandomCategoryFromGivenProvider(jokeProvider.getProviderName());

        //then
        assertThrows(JokeDispatcherJokeNotFoundException.class, e);
        defaultSetup();
    }

    @Test
    public void shouldThrowJokeDispatcherProviderNotFoundExceptionIfAllProvidersHasNoCategories() {
        //given
        jokeProviderMockList.forEach(this::mockGetAvailableCategoriesToReturnOptionalEmpty);

        //when
        final Executable e = () -> jokeDispatcher.getJokeFromAnyAvailableProvider();

        //then
        final JokeDispatcherProviderNotFoundException providerNotFoundException =
                assertThrows(JokeDispatcherProviderNotFoundException.class, e);
        assertThat(providerNotFoundException.getMessage()).isEqualTo(ALL_PROVIDERS_ARE_UNAVAILABLE);
        defaultSetup();
    }

    private void mockAllJokeProviderMethods(final JokeProvider jokeProvider,
                                            final Set<String> availableCategories) {
        mockGetProviderName(jokeProvider);
        final String mockJokeProviderName = jokeProvider.getProviderName();
        mockGetJokeFromCategory(jokeProvider, mockJokeProviderName, availableCategories);
        mockGetAvailableCategories(jokeProvider, availableCategories, mockJokeProviderName);
    }

    private void mockGetJokeFromCategory(final JokeProvider jokeProvider,
                                         final String mockJokeProviderName,
                                         final Set<String> availableCategories) {
        availableCategories.forEach(categoryName -> {
            final Category category = new Category(categoryName, mockJokeProviderName);
            Mockito.when(jokeProvider.getJokeFromCategory(category))
                    .thenReturn(Optional.of(new Joke("this is joke content", category)));
        });
    }

    private void mockGetJokeFromCategoryIfProviderBecomesUnableToPerformOperation(final JokeProvider jokeProvider) {
        Mockito.when(jokeProvider.getJokeFromCategory(Mockito.any()))
                .thenReturn(Optional.empty());
    }

    private void mockGetProviderName(final JokeProvider jokeProvider) {
        Mockito.when(jokeProvider.getProviderName())
                .thenCallRealMethod();
    }

    private void mockGetAvailableCategories(final JokeProvider jokeProvider,
                                            final Set<String> availableCategories,
                                            final String mockJokeProviderName) {
        final Set<Category> categories = availableCategories
                .stream()
                .map(categoryName -> new Category(categoryName, mockJokeProviderName))
                .collect(Collectors.toSet());

        Mockito.when(jokeProvider.getAvailableCategories())
                .thenReturn(Optional.of(categories));
    }

    private void mockGetAvailableCategoriesToReturnOptionalEmpty(final JokeProvider jokeProvider) {
        Mockito.when(jokeProvider.getAvailableCategories())
                .thenReturn(Optional.empty());
    }

    /**
     * JAVA 9 FEATURE - Set.of()
     */
    private void mockJokeProvidersMethods() {
        final Set<String> availableCategoriesProvider1 = Set.of("School", "Work", "Christmas", "Tools");
        final Set<String> availableCategoriesProvider2 = Set.of("Planes", "Work", "Life", "Tools");

        mockAllJokeProviderMethods(jokeProviderMockList.get(0), availableCategoriesProvider1);
        mockAllJokeProviderMethods(jokeProviderMockList.get(1), availableCategoriesProvider2);
    }

    private RandomnessProvider getRandomnessProvider() {
        return new RandomnessProvider() {
            @Override
            public Integer getRandomInt(final Integer cellingValue) {
                return RandomnessProvider.super.getRandomInt(cellingValue);
            }
        };
    }

    private List<String> getCategoriesServedByAllAvailableProviders() {
        final List<List<String>> listOfListOfCategories = jokeProviderMockList.stream()
                .map(JokeProvider::getAvailableCategories)
                .map(Optional::get)
                .map(e -> e.stream().map(Category::getValue).collect(Collectors.toList()))
                .collect(Collectors.toList());

        final List<String> retainer = listOfListOfCategories.get(0);
        listOfListOfCategories.forEach(e -> e.retainAll(retainer));
        return retainer;
    }

}