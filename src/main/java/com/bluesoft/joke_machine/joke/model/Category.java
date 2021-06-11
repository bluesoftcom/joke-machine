package com.bluesoft.joke_machine.joke.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Category {
    private String value;
    private String provider;
}
