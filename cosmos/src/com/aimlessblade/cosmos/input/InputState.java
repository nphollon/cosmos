package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
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

    public static Consumer<InputState> impulse(final double vx, final double vy, final double vz) {
        return a -> a.activeMovable.impulse(vx, vy, vz);
    }

    public static Consumer<InputState> angularImpulse(final double vPitch, final double vYaw, final double vRoll) {
        return a -> a.activeMovable.angularImpulse(vPitch, vYaw, vRoll);
    }

    private void setActiveMovable(final int newIndex) {
        activeMovable = movables.get(newIndex);
    }
}
