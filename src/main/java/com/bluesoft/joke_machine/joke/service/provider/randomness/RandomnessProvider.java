package com.bluesoft.joke_machine.joke.service.provider.randomness;

import java.util.Random;

public interface RandomnessProvider {
    default Integer getRandomInt(Integer cellingValue) {
        return new Random().nextInt(cellingValue);
    }
}
