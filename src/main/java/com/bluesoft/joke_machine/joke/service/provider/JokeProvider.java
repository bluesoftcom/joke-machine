package com.bluesoft.joke_machine.joke.service.provider;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;

import java.util.Optional;
import java.util.Set;

public interface JokeProvider {

    Optional<Joke> getJokeFromCategory(final Category category);

    Optional<Set<Category>> getAvailableCategories();

    default String getProviderName() {
        return this.getClass().getSimpleName();
    }
}
