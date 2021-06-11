package com.bluesoft.joke_machine;

import com.bluesoft.joke_machine.http.BasicHttpClient;
import com.bluesoft.joke_machine.http.HttpClient;
import com.bluesoft.joke_machine.joke.service.provider.randomness.RandomnessProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FromJava8To16Application {

    public static void main(String[] args) {
        SpringApplication.run(FromJava8To16Application.class, args);
    }

    @Bean
    public HttpClient basicHttpClient() {
        return new BasicHttpClient(java.net.http.HttpClient.Version.HTTP_1_1, 5);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RandomnessProvider randomnessProvider() {
        return new RandomnessProvider() {
            @Override
            public Integer getRandomInt(final Integer cellingValue) {
                return RandomnessProvider.super.getRandomInt(cellingValue);
            }
        };
    }
}
