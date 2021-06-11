package com.bluesoft.joke_machine.other_features_presentation;

import com.bluesoft.joke_machine.joke.model.Category;
import com.bluesoft.joke_machine.joke.model.Joke;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Java12 {

    @Test
    public void teeingCollectorPresentation() {
        //given
        final List<Joke> jokes = provideListOfJokes();

        /**
         * JAVA 12  FEATURE - teeing Collector
         */
        //when
        final Map<String, Object> collect = jokes.stream().collect(Collectors.teeing(
                Collectors.groupingBy(e -> e.getCategory().getValue(), Collectors.toList()),
                Collectors.summarizingInt(e -> e.getContent().length()),
                (a, b) -> Map.of("category", a, "totalLength", b)
        ));

        //then

        System.out.println(collect);
    }

    /**
     * JAVA 15  FEATURE - TEXTBLOCK
     */
    private List<Joke> provideListOfJokes() {
        return List.of(
                new Joke("""
                        During World War II, my father often
                         found himself stuck with KP duty. One day,
                          convinced he could improve things, he told
                          the head cook, “If you give me a paring knife,
                           I could peel these potatoes faster.” The cook 
                           turned slowly to my father and said, 
                           “Son, you’re in the Army. You have plenty of time.
                         ” —Jack Girard
                        """, new Category("Army", null)),
                new Joke("""
                        Why do we tell actors to “break a leg?”
                                        
                                        Because every play has a cast.
                        """, new Category("TV", null)),
                new Joke("""
                        Hear about the new restaurant called Karma?
                                        
                                        There’s no menu: You get what you deserve.
                        """, new Category("Religious", null)),
                new Joke("""
                        A woman in labor suddenly shouted,
                        “Shouldn’t! Wouldn’t! Couldn’t! Didn’t! Can’t!”
                                        
                                        “Don’t worry,” said the doc. “Those are just contractions.”
                        """, new Category("Work", null))
        );
    }
}
