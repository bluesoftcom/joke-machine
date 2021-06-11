package com.bluesoft.joke_machine.joke;

import com.bluesoft.joke_machine.joke.service.JokeServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class JokeControllerAdvice {

    @ExceptionHandler(JokeServiceException.class)
    public ResponseEntity<?> handleJobOfferException(final JokeServiceException e) {
        Map<String, String> body = Map.of(
                "status", HttpStatus.NOT_FOUND.toString(),
                "timestamp", LocalDateTime.now().toString(),
                "message", e.getCause().getLocalizedMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
