package com.bluesoft.joke_machine.joke.service.dispatcher.exception;

/**
 * JAVA 16 PREVIEW FEATURE - SEALED CLASSES
 */
public sealed class JokeDispatcherException extends Throwable
        permits JokeDispatcherJokeNotFoundException,
        JokeDispatcherProviderNotFoundException,
        JokeDispatcherCategoryNotFoundException {
    public JokeDispatcherException(String message) {
        super(message);
    }
}
