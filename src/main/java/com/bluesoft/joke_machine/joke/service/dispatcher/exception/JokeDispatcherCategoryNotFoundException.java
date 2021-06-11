package com.bluesoft.joke_machine.joke.service.dispatcher.exception;

/**
 * JAVA 16 PREVIEW FEATURE - SEALED CLASSES
 */
public final class JokeDispatcherCategoryNotFoundException extends JokeDispatcherException {
    public JokeDispatcherCategoryNotFoundException(final String message) {
        super(message);
    }
}
