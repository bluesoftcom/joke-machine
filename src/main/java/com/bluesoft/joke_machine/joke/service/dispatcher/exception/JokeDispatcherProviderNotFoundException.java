package com.bluesoft.joke_machine.joke.service.dispatcher.exception;

/**
 * JAVA 16 PREVIEW FEATURE - SEALED CLASSES
 */
public final class JokeDispatcherProviderNotFoundException extends JokeDispatcherException {
    public JokeDispatcherProviderNotFoundException(final String message) {
        super(message);
    }
}
