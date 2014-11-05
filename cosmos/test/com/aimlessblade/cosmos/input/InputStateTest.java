package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
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
        final List<Movable> movables = movableList(1);

        final Consumer<InputState> impulseCommand = InputState.impulse(1, 2, 3);
        impulseCommand.accept(new InputState(movables, 0));

        verify(movables.get(0)).impulse(1, 2, 3);
    }

    @Test
    public void angularImpulseShouldBeAppliedToActiveMovable() {
        final List<Movable> movables = movableList(2);

        final Consumer<InputState> impulseCommand = InputState.angularImpulse(4, 5, 6);
        impulseCommand.accept(new InputState(movables, 1));

        verify(movables.get(1)).angularImpulse(4, 5, 6);
    }

    private List<Movable> movableList(final int length) {
        final List<Movable> movables = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            movables.add(mock(Movable.class));
        }
        return movables;
    }
}