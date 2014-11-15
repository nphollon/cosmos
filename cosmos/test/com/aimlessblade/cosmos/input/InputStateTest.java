package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.physics.Movable;
import com.aimlessblade.cosmos.physics.Velocity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InputStateTest {
    @Test
    public void settingActiveIndexShouldChangeInputState() {
        final List<Movable> movables = movableList(2);
        final InputState state = new InputState(movables, 0);

        final Consumer<InputState> setter = InputState.setActiveIndex(1);
        setter.accept(state);

        assertThat(state.getActiveMovable(), is(movables.get(1)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void activeIndexCannotBeNegative() {
        InputState.setActiveIndex(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void activeIndexCannotBeGreaterThanEntityListLength() {
        final List<Movable> movables = movableList(1);

        final Consumer<InputState> setter = InputState.setActiveIndex(1);
        setter.accept(new InputState(movables, 0));
    }

    @Test
    public void impulseShouldBeAppliedToActiveMovable() {
        final Velocity velocity = Velocity.cartesian(0, 0, 0);
        final List<Movable> movables = movableList(1);

        final Consumer<InputState> impulseCommand = InputState.impulse(velocity);
        impulseCommand.accept(new InputState(movables, 0));

        verify(movables.get(0)).impulse(velocity);
    }

    @Test
    public void angularImpulseShouldBeAppliedToActiveMovable() {
        final Velocity angularVelocity = Velocity.cartesian(0, 0, 0);
        final List<Movable> movables = movableList(2);

        final Consumer<InputState> impulseCommand = InputState.angularImpulse(angularVelocity);
        impulseCommand.accept(new InputState(movables, 1));

        verify(movables.get(1)).angularImpulse(angularVelocity);
    }

    private List<Movable> movableList(final int length) {
        final List<Movable> movables = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            movables.add(mock(Movable.class));
        }
        return movables;
    }
}