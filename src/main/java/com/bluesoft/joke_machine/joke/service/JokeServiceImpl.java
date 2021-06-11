package com.bluesoft.joke_machine.joke.service;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.dispatcher.JokeDispatcher;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public final class JokeServiceImpl implements JokeService {

    private JokeDispatcher jokeDispatcher;

    private JokeServiceImpl(final JokeDispatcher jokeDispatcher) {
        this.jokeDispatcher = jokeDispatcher;
    }

    @Override
    public Joke getRandomJoke(String providerName, String category) throws JokeServiceException {

        final ValidationResult validationResult = validateInput(providerName, category);

        /**
         * JAVA 14 FEATURE - SWITCH EXPRESSION
         */
        try {
            return switch (validationResult) {
                case ALL_PARAMETERS_PRESENT -> jokeDispatcher.getJokeWithCategoryFromGivenProvider(providerName, category);
                case ONLY_PROVIDER_NAME_PRESENT -> jokeDispatcher.getJokeWithRandomCategoryFromGivenProvider(providerName);
                case ONLY_CATEGORY_NAME_PRESENT -> jokeDispatcher.getJokeFromAnyProviderServingThisCategory(category);
                default -> jokeDispatcher.getJokeFromAnyAvailableProvider();
            };
        } catch (JokeDispatcherException e) {
            throw new JokeServiceException("failed to fetch joke", e);
        }
    }

    @Override
    public Set<Category> getJokeCategories(String providerName) throws JokeServiceException {
        try {
            boolean providerNameIsPresent = isNotNullOrBlank(providerName);
            if (providerNameIsPresent) {
                return jokeDispatcher.getAllCategoriesFromGivenProvider(providerName);
            }
            return jokeDispatcher.getAllCategories();
        } catch (JokeDispatcherException e) {
            throw new JokeServiceException("Failed to fetch joke categories", e);
        }
    }

    @Override
    public Set<String> getJokeProviders() {
        return jokeDispatcher.getAvailableJokeProvidersNames();
    }

    private ValidationResult validateInput(final String providerName, final String category) {
        boolean providerNameIsPresent = isNotNullOrBlank(providerName);
        boolean categoryIsPresent = isNotNullOrBlank(category);

        if (providerNameIsPresent && categoryIsPresent) {
            return ValidationResult.ALL_PARAMETERS_PRESENT;
        }
        if (providerNameIsPresent) {
            return ValidationResult.ONLY_PROVIDER_NAME_PRESENT;
        }
        if (categoryIsPresent) {
            return ValidationResult.ONLY_CATEGORY_NAME_PRESENT;
        }
        return ValidationResult.NONE_PARAMETERS_PRESENT;
    }

    private boolean isNotNullOrBlank(String value) {
        return value != null && !value.isBlank();
    }


    private enum ValidationResult {
        NONE_PARAMETERS_PRESENT, ALL_PARAMETERS_PRESENT, ONLY_CATEGORY_NAME_PRESENT, ONLY_PROVIDER_NAME_PRESENT;
    }
}
