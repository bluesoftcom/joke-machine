package com.bluesoft.joke_machine.joke.service.dispatcher.exception;

/**
 * JAVA 16 PREVIEW FEATURE - SEALED CLASSES
 */
public final class JokeDispatcherJokeNotFoundException extends JokeDispatcherException {
    public JokeDispatcherJokeNotFoundException(final String message) {
        super(message);
    }
}
