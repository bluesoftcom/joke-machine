package com.bluesoft.joke_machine.joke.service.dispatcher;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherException;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherProviderNotFoundException;

import java.util.Set;

public interface JokeDispatcher {
    Joke getJokeWithCategoryFromGivenProvider(String providerName, String category) throws JokeDispatcherException;

    Joke getJokeWithRandomCategoryFromGivenProvider(String providerName) throws JokeDispatcherException;

    Joke getJokeFromAnyProviderServingThisCategory(String category) throws JokeDispatcherException;

    Joke getJokeFromAnyAvailableProvider() throws JokeDispatcherProviderNotFoundException;

    Set<Category> getAllCategoriesFromGivenProvider(String providerName) throws JokeDispatcherException;

    Set<Category> getAllCategories();

    Set<String> getAvailableJokeProvidersNames();
}
