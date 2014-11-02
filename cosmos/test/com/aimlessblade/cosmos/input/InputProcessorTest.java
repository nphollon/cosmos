package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lwjgl.input.Keyboard;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.aimlessblade.cosmos.input.KeyboardEvent.depress;
import static com.aimlessblade.cosmos.input.KeyboardEvent.press;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest(Keyboard.class)
@RunWith(PowerMockRunner.class)
public class InputProcessorTest {

    private Map<KeyboardEvent, Function<InputState, InputState>> keymap;
    private InputProcessor processor;
    private List<Movable> movables;

    @Before
    public void setup() {
        mockStatic(Keyboard.class);
        keymap = new HashMap<>();
        movables = Arrays.asList(mock(Movable.class));
        processor = new InputProcessor(keymap, movables);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldInvokeCommandForNextEventInQueue() {
        Function<InputState, InputState> command = mock(Function.class);
        keymap.put(press(Keyboard.KEY_A), command);

        when(Keyboard.next()).thenReturn(true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A);
        when(Keyboard.getEventKeyState()).thenReturn(true);

        processor.run();

        InputState state = new InputState(movables, 0);
        verify(command).apply(state);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDoNothingIfEventHasNoMapping() {
        Function<InputState, InputState> command = mock(Function.class);

        keymap.put(depress(Keyboard.KEY_B), command);

        when(Keyboard.next()).thenReturn(true, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();

        InputState state = new InputState(movables, 0);
        verify(command).apply(state);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProcessEventsUntilQueueIsEmpty() {
        InputState commandAOutput = new InputState(movables, 1);

        Function<InputState, InputState> commandA = s -> new InputState(s.getMovables(), s.getActiveIndex() + 1);
        Function<InputState, InputState> commandB = mock(Function.class);

        keymap.put(press(Keyboard.KEY_A), commandA);
        keymap.put(depress(Keyboard.KEY_B), commandB);

        when(Keyboard.next()).thenReturn(true, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();

        verify(commandB).apply(commandAOutput);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRememberInputStateBetweenMethodCalls() {
        InputState commandAOutput = new InputState(movables, 1);

        Function<InputState, InputState> commandA = s -> new InputState(s.getMovables(), s.getActiveIndex() + 1);
        Function<InputState, InputState> commandB = mock(Function.class);

        keymap.put(press(Keyboard.KEY_A), commandA);
        keymap.put(depress(Keyboard.KEY_B), commandB);

        when(Keyboard.next()).thenReturn(true, false, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();
        processor.run();

        verify(commandB).apply(commandAOutput);
    }
}