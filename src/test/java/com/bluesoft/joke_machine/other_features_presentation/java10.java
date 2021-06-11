package com.bluesoft.joke_machine.other_features_presentation;

import com.bluesoft.joke_machine.joke.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class java10 {

    /**
     * JAVA 9 FEATURE - List.of(), Map.of(), Set.of()
     */
    public void varForComplexType() {
        var records = List.of(
                Map.of("r1", Set.of("a", "b", "c"),
                        "r2", Set.of("a", "b", "c")),
                Map.of("r1", Set.of("c", "b")),
                Map.of("r3", Set.of()));

        final List<Map<String, Set<String>>> recordsOldWay = List.of(
                Map.of("r1", Set.of("a", "b", "c"),
                        "r2", Set.of("a", "b", "c")),
                Map.of("r1", Set.of("c", "b")),
                Map.of("r3", Set.of()));
    }

    @Test
    public void copyingAList() {
        //given
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("a", "providerA"));
        categories.add(new Category("b", "providerB"));

        final List<Category> copyOfCategories = List.copyOf(categories);
        final List<Category> unmodifiableListOfCategories = Collections.unmodifiableList(categories);
        final List<Category> newArrayList = new ArrayList<>(categories);

        //when
        categories.add(new Category("c", "providerC"));

        //then
        assertNotEquals(categories.size(), copyOfCategories.size()); //shallowCopy
        assertEquals(categories.size(), unmodifiableListOfCategories.size()); //viewCopy
        assertNotEquals(categories.size(), newArrayList.size()); //shallowCopy

        Executable e = () -> copyOfCategories.add(new Category("asd", "asd"));
        assertThrows(UnsupportedOperationException.class, e);
    }
}
