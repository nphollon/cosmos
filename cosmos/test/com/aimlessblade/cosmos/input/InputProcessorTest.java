package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.geo.Movable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lwjgl.input.Keyboard;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest(Keyboard.class)
@RunWith(PowerMockRunner.class)
public class InputProcessorTest {

    private Keymap keymap;
    private InputProcessor processor;
    private List<Movable> movables;

    @Before
    public void setup() {
        mockStatic(Keyboard.class);
        keymap = mock(Keymap.class);
        movables = Arrays.asList(mock(Movable.class));
        processor = new InputProcessor(keymap, movables);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldInvokeCommandForNextEventInQueue() {
        Function<InputState, InputState> command = mock(Function.class);
        when(keymap.getCommand(Keyboard.KEY_A, true)).thenReturn(command);

        when(Keyboard.next()).thenReturn(true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A);
        when(Keyboard.getEventKeyState()).thenReturn(true);

        processor.run();

        InputState state = new InputState(movables, 0);
        verify(command).apply(state);
    }

    @Test
    public void shouldDoNothingIfEventQueueIsEmpty() {
        when(Keyboard.next()).thenReturn(false);

        processor.run();

        verifyZeroInteractions(keymap);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProcessEventsUntilQueueIsEmpty() {
        InputState commandAOutput = new InputState(movables, 1);

        Function<InputState, InputState> commandA = s -> new InputState(s.getMovables(), s.getActiveIndex() + 1);
        Function<InputState, InputState> commandB = mock(Function.class);

        when(keymap.getCommand(Keyboard.KEY_A, true)).thenReturn(commandA);
        when(keymap.getCommand(Keyboard.KEY_B, false)).thenReturn(commandB);

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

        when(keymap.getCommand(Keyboard.KEY_A, true)).thenReturn(commandA);
        when(keymap.getCommand(Keyboard.KEY_B, false)).thenReturn(commandB);

        when(Keyboard.next()).thenReturn(true, false, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();
        processor.run();

        verify(commandB).apply(commandAOutput);
    }
}