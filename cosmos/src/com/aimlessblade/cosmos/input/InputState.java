package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
import com.aimlessblade.cosmos.geo.Velocity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Consumer;

@Getter(AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
public final class InputState {
    private final List<Movable> movables;
    private Movable activeMovable;

    InputState(final List<Movable> movables, final int activeIndex) {
        this.movables = movables;
        setActiveMovable(activeIndex);
    }

    public static Consumer<InputState> setActiveIndex(final int newIndex) {
        if (newIndex < 0) {
            throw new IndexOutOfBoundsException();
        }

        return a -> a.setActiveMovable(newIndex);
    }

    public static Consumer<InputState> impulse(final Velocity velocity) {
        return a -> a.activeMovable.impulse(velocity);
    }

    public static Consumer<InputState> angularImpulse(final Velocity angularVelocity) {
        return a -> a.activeMovable.angularImpulse(angularVelocity);
    }

    private void setActiveMovable(final int newIndex) {
        activeMovable = movables.get(newIndex);
    }
}
