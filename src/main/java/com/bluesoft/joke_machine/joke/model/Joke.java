package com.bluesoft.joke_machine.joke.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Joke {
    private String content;
    private Category category;
}
