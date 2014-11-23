package com.aimlessblade.cosmos.graphics.vertex;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Attribute {
    private final String name;
    private final int length;
}
