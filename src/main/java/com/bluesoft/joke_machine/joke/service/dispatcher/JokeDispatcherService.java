package com.bluesoft.joke_machine.joke.service.dispatcher;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherCategoryNotFoundException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherJokeNotFoundException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherProviderNotFoundException;
import com.bluesoft.joke_machine.joke.service.provider.JokeProvider;
import com.bluesoft.joke_machine.joke.service.provider.randomness.RandomnessProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class JokeDispatcherService implements JokeDispatcher {

    public static final String ALL_PROVIDERS_ARE_UNAVAILABLE = "All providers are unavailable";

    private List<JokeProvider> jokeProviders;
    private RandomnessProvider randomnessProvider;


    public JokeDispatcherService(final List<JokeProvider> jokeProviders,
                                 final RandomnessProvider randomnessProvider) {
        this.jokeProviders = jokeProviders;
        this.randomnessProvider = randomnessProvider;
    }

    @Override
    public Joke getJokeWithCategoryFromGivenProvider(final String providerName, final String categoryName)
            throws JokeDispatcherException {
        final JokeProvider jokeProvider = getProviderOrThrowException(providerName);
        final Category providersCategoryByName = findProvidersCategoryByName(jokeProvider, categoryName)
                .orElseThrow(() -> new JokeDispatcherProviderNotFoundException(
                        String.format("Given category for provider %s does not exists", providerName)));

        return jokeProvider.getJokeFromCategory(providersCategoryByName)
                .orElseThrow(() -> new JokeDispatcherJokeNotFoundException(String.format(
                        "Failed to fetch joke from %s provider", jokeProvider)));
    }


    @Override
    public Joke getJokeWithRandomCategoryFromGivenProvider(final String providerName) throws JokeDispatcherException {
        final JokeProvider jokeProvider = getProviderOrThrowException(providerName);
        final List<Category> availableCategories = List.copyOf(getAllCategoriesFromGivenProvider(providerName));
        final Category randomCategory = availableCategories.get(randomnessProvider.getRandomInt(availableCategories.size()));
        return jokeProvider.getJokeFromCategory(randomCategory)
                .orElseThrow(() -> new JokeDispatcherJokeNotFoundException(
                        String.format("Provider %s failed to fetch joke from random category", jokeProvider.getProviderName())));

    }

    @Override
    public Joke getJokeFromAnyProviderServingThisCategory(final String category) throws JokeDispatcherException {
        List<JokeProvider> jokeProvidersCopy = new ArrayList<>(jokeProviders);
        Collections.shuffle(jokeProvidersCopy);
        for (JokeProvider jokeProvider : jokeProvidersCopy) {
            final Optional<Set<Category>> availableCategories = jokeProvider.getAvailableCategories();
            if (availableCategories.isPresent()) {
                final Optional<Category> matchingCategory = availableCategories.get()
                        .stream()
                        .filter(e -> e.getValue().equalsIgnoreCase(category)).findAny();
                if (matchingCategory.isPresent()) {
                    final Optional<Joke> jokeFromCategory = jokeProvider.getJokeFromCategory(matchingCategory.get());
                    if (jokeFromCategory.isPresent()) {
                        return jokeFromCategory.get();
                    }
                }
            }
        }

        throw new JokeDispatcherCategoryNotFoundException("There is no provider serving this category");
    }

    /**
     * JAVA 16  FEATURE - mapMulti
     */
    @Override
    public Joke getJokeFromAnyAvailableProvider() throws JokeDispatcherProviderNotFoundException {
        List<JokeProvider> jokeProvidersCopy = new ArrayList<>(jokeProviders);
        Collections.shuffle(jokeProvidersCopy);
        return jokeProvidersCopy
                .stream()
                .unordered()
                .mapMulti((JokeProvider jokeProvider, Consumer<Joke> consumer) -> {
                    toOptionalJokeFromRandomCategory(jokeProvider).ifPresent(consumer);
                })
                .findAny()
                .orElseThrow(() -> new JokeDispatcherProviderNotFoundException(ALL_PROVIDERS_ARE_UNAVAILABLE));
    }


    @Override
    public Set<Category> getAllCategoriesFromGivenProvider(final String providerName)
            throws JokeDispatcherException {
        final JokeProvider providerOrThrowException = getProviderOrThrowException(providerName);
        return getCategoriesFromJokeProviderOrThrowException(providerOrThrowException);
    }

    /**
     * JAVA 16 FEATURE - mapMulti
     * JAVA 9 FEATURE - Set.of()
     */
    @Override
    public Set<Category> getAllCategories() {
        return jokeProviders.stream()
                .mapMulti((JokeProvider jokeProvider, Consumer<Category> consumer) ->
                        jokeProvider
                                .getAvailableCategories()
                                .orElse(Set.of())
                                .forEach(consumer))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAvailableJokeProvidersNames() {
        return jokeProviders.stream().map(JokeProvider::getProviderName).collect(Collectors.toSet());
    }


    private Optional<Joke> toOptionalJokeFromRandomCategory(final JokeProvider jokeProvider) {
        try {
            final var randomCategory = getCategoriesFromJokeProviderOrThrowException(jokeProvider)
                    .stream()
                    .skip(randomnessProvider.getRandomInt(jokeProviders.size()))
                    .findFirst()
                    .get();
            return jokeProvider.getJokeFromCategory(randomCategory);
        } catch (JokeDispatcherException e) {
            return Optional.empty();
        }
    }


    private JokeProvider getProviderOrThrowException(final String providerName) throws JokeDispatcherProviderNotFoundException {
        return findProviderByName(providerName)
                .orElseThrow(() -> new JokeDispatcherProviderNotFoundException("Given provider does not exists"));
    }

    private Set<Category> getCategoriesFromJokeProviderOrThrowException(final JokeProvider jokeProvider) throws JokeDispatcherException {
        return jokeProvider.getAvailableCategories()
                .orElseThrow(() -> new JokeDispatcherCategoryNotFoundException(
                        String.format("Failed to fetch categories from %s provider", jokeProvider.getProviderName())));
    }

    /**
     * JAVA 16  FEATURE - mapMulti
     */
    private Optional<Category> findProvidersCategoryByName(final JokeProvider jokeProvider,
                                                           final String categoryName) throws JokeDispatcherException {
        return getCategoriesFromJokeProviderOrThrowException(jokeProvider)
                .stream()
                .mapMulti((Category category, Consumer<Category> consumer) -> {
                    if (category.getValue().equalsIgnoreCase(categoryName)) {
                        String categoryNameFormatted = StringUtils.capitalize(categoryName.toLowerCase());
                        consumer.accept(new Category(categoryNameFormatted, jokeProvider.getProviderName()));
                    }
                })
                .findAny();
    }

    /**
     * JAVA 16  FEATURE - mapMulti
     */
    private Optional<JokeProvider> findProviderByName(final String providerName) {
        return jokeProviders
                .stream()
                .mapMulti((JokeProvider provider, Consumer<JokeProvider> consumer) -> {
                    if (provider.getProviderName().equals(providerName)) {
                        consumer.accept(provider);
                    }
                })
                .findAny();
    }

}
