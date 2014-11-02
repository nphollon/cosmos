package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InputStateTest {
    @Test
    public void settingActiveIndexShouldChangeInputState() {
        List<Movable> entities = Arrays.asList(mock(Movable.class), mock(Movable.class));
        InputState startState = new InputState(entities, 0);

        Function<InputState, InputState> setter = InputState.setActiveIndex(1);
        final InputState finalState = setter.apply(startState);

        assertThat(finalState, is(new InputState(entities, 1)));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void activeIndexCannotBeNegative() {
        InputState.setActiveIndex(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void activeIndexCannotBeGreaterThanEntityListLength() {
        List<Movable> entities = Arrays.asList(mock(Movable.class));

        final Function<InputState, InputState> setter = InputState.setActiveIndex(1);
        setter.apply(new InputState(entities, 0));
    }
}