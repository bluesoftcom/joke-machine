package com.bluesoft.joke_machine.other_features_presentation;

import com.bluesoft.joke_machine.joke.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class java9 {

    @Test
    public void unmodifiableResultOfSetOf() {
        //given
        final Set<String> unmodifiableSet = Set.of("a", "b", "c");

        //when
        Executable e = () -> unmodifiableSet.add("g");

        //than
        assertThrows(UnsupportedOperationException.class, e);
    }

    @Test
    public void listOfCreationAttemptWithNullValue() {
        //given

        //when
        Executable e = () -> List.of("a", null, "c");

        //than
        assertThrows(NullPointerException.class, e);
    }


    /**
     * JAVA 9 FEATURE - List.of()
     */
    @Test
    public void collectorsFiltering() {
        //given
        final List<Category> categories = List.of(
                new Category("Political", "providerA"),
                new Category("Job", "providerA"),
                new Category("Political", "providerB"),
                new Category("School", "providerB"),
                new Category("Colors", "providerB"),
                new Category("Political", "providerC"),
                new Category("Sea", "providerD")
        );

        /**
         * JAVA 9  FEATURE - Collectors.filtering()
         */
        //when
        final Map<String, List<Category>> political = categories.stream().collect(Collectors.groupingBy(
                Category::getProvider,
                Collectors.filtering(e -> e.getValue().equalsIgnoreCase("political"), Collectors.toList())
        ));
        //then

        System.out.println(political);
    }


}
