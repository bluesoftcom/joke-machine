package com.bluesoft.joke_machine.joke;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import com.bluesoft.joke_machine.joke.service.JokeServiceException;
import com.bluesoft.joke_machine.joke.service.JokeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/jokes")
public final class JokeController {

    private JokeServiceImpl jokeService;

    @GetMapping("/random")
    public ResponseEntity<Joke> getRandomJoke(
            @RequestParam("provider-name") @Nullable final String providerName,
            @RequestParam("category") @Nullable final String category) throws JokeServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jokeService.getRandomJoke(providerName, category));
    }

    @GetMapping("/categories")
    public ResponseEntity<Set<Category>> getJokeCategory(
            @RequestParam("provider-name") @Nullable final String providerName) throws JokeServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jokeService.getJokeCategories(providerName));
    }

    @GetMapping("/providers")
    public ResponseEntity<Set<String>> getJokeProviders() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(jokeService.getJokeProviders());
    }

}
