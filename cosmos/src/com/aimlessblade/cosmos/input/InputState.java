package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class InputState {
    private final List<Movable> movables;
    private final int activeIndex;
}
