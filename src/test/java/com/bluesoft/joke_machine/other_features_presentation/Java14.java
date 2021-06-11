package com.bluesoft.joke_machine.other_features_presentation;

import com.bluesoft.joke_machine.joke.service.provider.JokeProvider;
import com.bluesoft.joke_machine.joke.service.provider.external.ChuckNorrisJokeProvider;
import com.bluesoft.joke_machine.joke.service.provider.randomness.RandomnessProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Java14 {

    @Autowired
    private List<JokeProvider> allJokeProviders;

    @Autowired
    private RandomnessProvider randomnessProvider;

    /**
     * JAVA 16  FEATURE - instanceof pattern matching
     */
    @Test
    public void patternMatchingForInstanceOf() {
        final JokeProvider randomJokeProvider = getRandomJokeProvider();

        if (randomJokeProvider instanceof ChuckNorrisJokeProvider chuckNorrisJokeProvider) {
            chuckNorrisJokeProvider.getDeserializationConfig();
        }
    }

    private JokeProvider getRandomJokeProvider() {
        return allJokeProviders.get(randomnessProvider.getRandomInt(allJokeProviders.size()));
    }
}
