package com.bluesoft.joke_machine.other_features_presentation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Java16 {

    /**
     * JAVA 16  FEATURE - record type
     */
    record CategoryRecord(String value, String providerName){}
    record JokeRecord(String content, CategoryRecord category) { }

    @Test
    public void whatCanYouDoOnRecord() {
        //Creation of the record just like simple POJO
        final JokeRecord joke = new JokeRecord(
                "its not a joke",
                new CategoryRecord("not-funny", null));

        //Records after creation only allows to
        // use the accessors to its fields:
        final String s = joke.category().providerName();

        assertTrue(joke.category()
                .equals(new CategoryRecord("not-funny", null)));
        System.out.println(joke.toString());
    }


}
