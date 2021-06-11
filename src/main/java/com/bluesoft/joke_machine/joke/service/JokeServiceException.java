package com.bluesoft.joke_machine.joke.service;

public class JokeServiceException extends Throwable {
    public JokeServiceException(String message, Throwable e) {
        super(message, e);
    }
}
