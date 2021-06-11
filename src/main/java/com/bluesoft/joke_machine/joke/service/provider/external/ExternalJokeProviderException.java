package com.bluesoft.joke_machine.joke.service.provider.external;

import com.bluesoft.joke_machine.joke.service.provider.exception.JokeProviderException;

public class ExternalJokeProviderException extends JokeProviderException {

    public ExternalJokeProviderException(final String message) {
        super(message);
    }

}
