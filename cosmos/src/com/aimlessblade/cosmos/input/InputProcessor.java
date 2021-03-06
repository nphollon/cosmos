package com.aimlessblade.cosmos.input;

import com.aimlessblade.cosmos.processor.Processor;
import com.aimlessblade.cosmos.physics.Movable;
import lombok.AccessLevel;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.input.Keyboard.getEventKeyState;

@Getter(AccessLevel.PACKAGE)
public final class InputProcessor implements Processor {
    private final Map<KeyboardEvent, Consumer<InputState>> keymap;
    private final List<Movable> movables;
    private final InputState state;

    public InputProcessor(final Map<KeyboardEvent, Consumer<InputState>> keymap, final List<Movable> movables) {
        this.keymap = keymap;
        this.movables = movables;
        state = new InputState(movables, 0);
    }

    @Override
    public void run() {
        while (Keyboard.next()) {
            KeyboardEvent event = new KeyboardEvent(getEventKey(), getEventKeyState());
            if (keymap.containsKey(event)) {
                keymap.get(event).accept(state);
            }
        }
    }
}
