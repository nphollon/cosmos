package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Function;

@Getter
@EqualsAndHashCode
@ToString
public class InputState {
    private final List<Movable> movables;
    private final Movable activeMovable;

    public InputState(final List<Movable> movables, final int activeIndex) {
        this.movables = movables;
        this.activeMovable = movables.get(activeIndex);
    }

    public static Function<InputState, InputState> setActiveIndex(final int newIndex) {
        if (newIndex < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return a -> new InputState(a.movables, newIndex);
    }
}
