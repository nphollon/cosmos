package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.physics.Movable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lwjgl.input.Keyboard;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.aimlessblade.cosmos.input.KeyboardEvent.lift;
import static com.aimlessblade.cosmos.input.KeyboardEvent.press;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest(Keyboard.class)
@RunWith(PowerMockRunner.class)
public class InputProcessorTest {

    private Map<KeyboardEvent, Consumer<InputState>> keymap;
    private InputProcessor processor;
    private List<Movable> movables;

    @Before
    public void setup() {
        mockStatic(Keyboard.class);
        keymap = new HashMap<>();
        movables = Arrays.asList(mock(Movable.class), mock(Movable.class));
        processor = new InputProcessor(keymap, movables);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldInvokeCommandForNextEventInQueue() {
        Consumer<InputState> command = mock(Consumer.class);
        keymap.put(press(Keyboard.KEY_A), command);

        when(Keyboard.next()).thenReturn(true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A);
        when(Keyboard.getEventKeyState()).thenReturn(true);

        processor.run();

        verify(command).accept(new InputState(movables, 0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDoNothingIfEventHasNoMapping() {
        Consumer<InputState> command = mock(Consumer.class);

        keymap.put(lift(Keyboard.KEY_B), command);

        when(Keyboard.next()).thenReturn(true, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();

        verify(command).accept(new InputState(movables, 0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProcessEventsUntilQueueIsEmpty() {
        Consumer<InputState> commandA = mock(Consumer.class);
        Consumer<InputState> commandB = mock(Consumer.class);
        InOrder inOrder = inOrder(commandA, commandB);

        keymap.put(press(Keyboard.KEY_A), commandA);
        keymap.put(lift(Keyboard.KEY_B), commandB);

        when(Keyboard.next()).thenReturn(true, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();

        inOrder.verify(commandA).accept(new InputState(movables, 0));
        inOrder.verify(commandB).accept(new InputState(movables, 0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldRememberInputStateBetweenMethodCalls() {
        Consumer<InputState> commandA = mock(Consumer.class);
        Consumer<InputState> commandB = mock(Consumer.class);
        InOrder inOrder = inOrder(commandA, commandB);

        keymap.put(press(Keyboard.KEY_A), commandA);
        keymap.put(lift(Keyboard.KEY_B), commandB);

        when(Keyboard.next()).thenReturn(true, false, true, false);
        when(Keyboard.getEventKey()).thenReturn(Keyboard.KEY_A, Keyboard.KEY_B);
        when(Keyboard.getEventKeyState()).thenReturn(true, false);

        processor.run();
        processor.run();

        inOrder.verify(commandA).accept(new InputState(movables, 0));
        inOrder.verify(commandB).accept(new InputState(movables, 0));
    }
}