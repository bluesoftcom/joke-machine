package com.bluesoft.joke_machine.joke.service;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.dispatcher.exception.JokeDispatcherException;

import java.util.Set;

public interface JokeService {

    Joke getRandomJoke(final String providerName,
                       final String category) throws JokeDispatcherException, JokeServiceException;

    Set<Category> getJokeCategories(final String providerName) throws JokeDispatcherException, JokeServiceException;

    Set<String> getJokeProviders();
}